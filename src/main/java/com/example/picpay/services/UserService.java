package com.example.picpay.services;

import com.example.picpay.dtos.UserDTO;
import com.example.picpay.entities.User;
import com.example.picpay.enums.UserType;
import com.example.picpay.exceptions.DocumentValidationException;
import com.example.picpay.exceptions.TransactionValidationException;
import com.example.picpay.exceptions.UserValidationException;
import com.example.picpay.exceptions.messages.ExceptionMessages;
import com.example.picpay.repository.UserRepository;
import com.example.picpay.validation.documents.DocumentValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws TransactionValidationException {

        if(sender.getUserType() == UserType.MERCHANT) {
            throw new TransactionValidationException(ExceptionMessages.USER_NOT_AUTHORIZED, HttpStatus.FORBIDDEN);
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new TransactionValidationException(ExceptionMessages.INSUFFICIENT_FOUNDS, HttpStatus.BAD_REQUEST);
        }
    }
    public User findUserById(Long id) throws UserValidationException {

        if (id == null || id <= 0) {
            throw new UserValidationException(ExceptionMessages.INVALID_USER_ID, id, HttpStatus.BAD_REQUEST);
        }

        return this.userRepository.findUserById(id)
                .orElseThrow(() -> new UserValidationException(ExceptionMessages.USER_CANNOT_BE_NULL, HttpStatus.BAD_REQUEST));
    }

    public User createUser(UserDTO userDTO) throws UserValidationException, DocumentValidationException {

        User user = new User(userDTO);
        validateUser(user);
        return saveUser(user);
    }

    public User saveUser(User user) {

        return this.userRepository.save(user);
    }

    private void validateUser(User user) throws UserValidationException, DocumentValidationException {

        if (user == null) {
            throw new UserValidationException(ExceptionMessages.USER_CANNOT_BE_NULL, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByDocument(user.getDocument())) {
            throw new DocumentValidationException(ExceptionMessages.USER_DOCUMENT_ALREADY_REGISTERED + user.getDocument(), HttpStatus.CONFLICT);
        }

        if (!DocumentValid.isCPFValid(user.getDocument())) {
            throw new DocumentValidationException(ExceptionMessages.USER_DOCUMENT_INVALID + user.getDocument(), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserValidationException(ExceptionMessages.USER_EMAIL_ALREADY_REGISTERED + user.getEmail(), HttpStatus.CONFLICT);
        }
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

}
