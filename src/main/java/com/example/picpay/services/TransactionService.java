package com.example.picpay.services;

import com.example.picpay.config.variables.AuthorizeTransaction;
import com.example.picpay.config.variables.ConstantVariables;
import com.example.picpay.dtos.TransactionDTO;
import com.example.picpay.entities.Transaction;
import com.example.picpay.entities.User;
import com.example.picpay.exceptions.TransactionValidationException;
import com.example.picpay.exceptions.messages.ExceptionMessages;
import com.example.picpay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception{
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        userService.validateTransaction(sender, transactionDTO.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transactionDTO.value());
        if(!isAuthorized){
            throw new TransactionValidationException(ExceptionMessages.TRANSACTION_NOT_AUTHORIZED);
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

        this.transactionRepository.save(transaction);
        this.transactionRepository.save(transaction);

        this.notificationService.sendNotification(sender, ConstantVariables.TRANSACTION_COMPLETED_SUCCESSFULLY);
        this.notificationService.sendNotification(receiver, ConstantVariables.TRANSACTION_RECEIVED_SUCCESSFULLY);

        return transaction;
    }

//    public boolean authorizeTransaction(User sender, BigDecimal value) {
//        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(
//                EnvironmentVariable.VAR_AUTHORIZED, Map.class);
//
//        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
//            String message = (String) authorizationResponse.getBody().get("message");
//            return ConstantVariables.AUTHORIZED.equalsIgnoreCase(message);
//        } else{
//            return false;
//        }
//    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {

        String authorizationResponse = AuthorizeTransaction.checksAuthorization();

        return ConstantVariables.AUTHORIZED.equalsIgnoreCase(authorizationResponse);
    }


}
