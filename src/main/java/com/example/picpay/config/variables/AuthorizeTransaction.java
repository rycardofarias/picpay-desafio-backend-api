package com.example.picpay.config.variables;

import java.util.Random;

public class AuthorizeTransaction {

    public static String checksAuthorization() {
        int randomNumber = new Random().nextInt(9);

        if(randomNumber <= 7) return EnvironmentVariable.VAR_AUTHORIZED;
        else return EnvironmentVariable.VAR_UNAUTHORIZED;
    }
}
