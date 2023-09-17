package com.example.picpay.services;

import com.example.picpay.config.variables.AuthorizeTransaction;
import com.example.picpay.config.variables.ConstantVariables;
import com.example.picpay.dtos.TransactionDTO;
import com.example.picpay.entities.Transaction;
import com.example.picpay.entities.User;
import com.example.picpay.enums.UserType;
import com.example.picpay.exceptions.NotificationException;
import com.example.picpay.exceptions.TransactionValidationException;
import com.example.picpay.exceptions.messages.ExceptionMessages;
import com.example.picpay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = userService.findUserById(transactionDTO.senderId());
        User receiver = userService.findUserById(transactionDTO.receiverId());

        validateTransaction(sender, transactionDTO.value());

        authorizeTransactionOrThrow(sender, transactionDTO.value());

        Transaction transaction = createAndSaveTransaction(sender, receiver, transactionDTO.value());

        updateSenderAndReceiverBalances(sender, receiver, transactionDTO.value());

        sendNotifications(sender, receiver);

        return transaction;
    }

    private void validateTransaction(User sender, BigDecimal value) throws TransactionValidationException {

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new TransactionValidationException(ExceptionMessages.USER_NOT_AUTHORIZED, HttpStatus.FORBIDDEN);
        }

        if (sender.getBalance().compareTo(value) < 0) {
            throw new TransactionValidationException(ExceptionMessages.INSUFFICIENT_FOUNDS, HttpStatus.BAD_REQUEST);
        }
    }

    private void authorizeTransactionOrThrow(User sender, BigDecimal value) throws Exception {

        boolean isAuthorized = authorizeTransaction(sender, value);
        if (!isAuthorized) {
            throw new TransactionValidationException(ExceptionMessages.TRANSACTION_NOT_AUTHORIZED, HttpStatus.FORBIDDEN);
        }
    }

    private Transaction createAndSaveTransaction(User sender, User receiver, BigDecimal value) {

        Transaction transaction = new Transaction();
        transaction.setAmount(value);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    private void updateSenderAndReceiverBalances(User sender, User receiver, BigDecimal value) {

        sender.setBalance(sender.getBalance().subtract(value));
        receiver.setBalance(receiver.getBalance().add(value));

        userService.saveUser(sender);
        userService.saveUser(receiver);
    }

    private void sendNotifications(User sender, User receiver) throws NotificationException {

        notificationService.sendNotification(sender, ConstantVariables.TRANSACTION_COMPLETED_SUCCESSFULLY);
        notificationService.sendNotification(receiver, ConstantVariables.TRANSACTION_RECEIVED_SUCCESSFULLY);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) throws Exception {

        try {
            ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(
                    AuthorizeTransaction.checksAuthorization(), Map.class);

            if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
                String message = (String) authorizationResponse.getBody().get("message");
                return ConstantVariables.AUTHORIZED.equalsIgnoreCase(message);
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }
}
