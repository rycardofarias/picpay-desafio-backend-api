package com.example.picpay.services;

import com.example.picpay.config.variables.ConstantVariables;
import com.example.picpay.config.variables.EnvironmentVariable;
import com.example.picpay.dtos.TransactionDTO;
import com.example.picpay.entities.Transaction;
import com.example.picpay.entities.User;
import com.example.picpay.exceptions.TransactionValidationException;
import com.example.picpay.exceptions.messages.ExceptionMessages;
import com.example.picpay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public void createTransaction(TransactionDTO transactionDTO) throws Exception{
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
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authoriResponse = restTemplate.getForEntity(
                EnvironmentVariable.VAR_AUTHORIZER, Map.class);

        if (authoriResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authoriResponse.getBody().get("message");
            return ConstantVariables.AUTHOTIZED.equalsIgnoreCase(message);
        } else{
            return false;
        }
    }
}
