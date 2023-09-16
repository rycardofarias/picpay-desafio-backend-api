package com.example.picpay.config.variables;

import java.util.Random;

public class AuthorizeTransaction {

    public static String checksAuthorization() {
        int randomNumber = new Random().nextInt(2);

        if(randomNumber == 0) return ConstantVariables.AUTHOTIZED;
        else return ConstantVariables.UNAUTHORIZED;
    }
}
