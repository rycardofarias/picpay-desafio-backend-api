package com.example.picpay.dtos;

import com.example.picpay.enums.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document,
                      BigDecimal balance, String email, String password, UserType userType) {
}
