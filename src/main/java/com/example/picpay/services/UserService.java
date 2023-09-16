package com.example.picpay.services;

import com.example.picpay.entities.User;
import com.example.picpay.enums.UserType;
import com.example.picpay.exceptions.DocumentValidationException;
import com.example.picpay.exceptions.TransactionValidationException;
import com.example.picpay.exceptions.UserValidationException;
import com.example.picpay.exceptions.messages.ExceptionMessages;
import com.example.picpay.repository.UserRepository;
import com.example.picpay.validation.documents.DocumentValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws TransactionValidationException {

        if(sender.getUserType() == UserType.MERCHANT) {
            throw new TransactionValidationException(ExceptionMessages.USER_NOT_AUTHORIZED);
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new TransactionValidationException(ExceptionMessages.INSUFFICIENT_FOUNDS);
        }
    }
    public User findUserById(Long id) throws UserValidationException {

        if (id == null || id <= 0) {
            throw new UserValidationException(ExceptionMessages.INVALID_USER_ID, id);
        }

        return this.userRepository.findUserById(id)
                .orElseThrow(() -> new UserValidationException(ExceptionMessages.USER_CANNOT_BE_NULL));
    }

    public User  saveUser(User user) throws Exception {

        if (user == null) {
            throw new UserValidationException(ExceptionMessages.USER_CANNOT_BE_NULL);
        }

        if (userRepository.existsByDocument(user.getDocument())) {
            throw new DocumentValidationException(ExceptionMessages.USER_DOCUMENT_ALREADY_REGISTERED + user.getDocument());
        }
        if(!DocumentValid.isCPFValid(user.getDocument())){
            throw new DocumentValidationException(ExceptionMessages.USER_DOCUMENT_INVALID + user.getDocument());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserValidationException(ExceptionMessages.USER_EMAIL_ALREADY_REGISTERED + user.getEmail());
        }

        return this.userRepository.save(user);
    }
}
