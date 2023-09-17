package com.example.picpay.config.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private DateUtils() {
        // Para garantir que a classe não seja instanciada
    }

    public static String formatTimestamp(Date date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return formatter.format(date);
        } catch (Exception e) {
            return date.toString();
        }
    }
}