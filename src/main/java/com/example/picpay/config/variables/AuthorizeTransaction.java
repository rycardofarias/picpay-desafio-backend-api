package com.example.picpay.config.variables;

import java.util.Random;

public class AuthorizeTransaction {

    public static String checksAuthorization() {
        int randomNumber = new Random().nextInt(9);

        if(randomNumber <= 7) return ConstantVariables.AUTHORIZED;
        else return ConstantVariables.UNAUTHORIZED;
    }
}
