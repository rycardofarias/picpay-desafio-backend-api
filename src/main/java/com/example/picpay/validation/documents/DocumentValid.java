package com.example.picpay.validation.documents;

public class DocumentValid {

    public static boolean isCPFValid(String cpf) {
        // Removes all non-digit characters
        cpf = cpf.replaceAll("[^0-9]", "");

        // CPF's formed by a sequence of equal numbers are considered errors
        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") ||
                cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") ||
                cpf.equals("99999999999") || (cpf.length() != 11)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }

        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int sum1 = 0;
        for (int i = 0; i < 9; i++) {
            sum1 += numbers[i] * (10 - i);
        }
        int remainder1 = sum1 % 11;
        int digit1 = (remainder1 < 2) ? 0 : (11 - remainder1);

        int sum2 = 0;
        for (int i = 0; i < 10; i++) {
            sum2 += numbers[i] * (11 - i);
        }
        int remainder2 = sum2 % 11;
        int digit2 = (remainder2 < 2) ? 0 : (11 - remainder2);

        return (digit1 == numbers[9]) && (digit2 == numbers[10]);

    }

    public static boolean isCNPJValid(String cnpj) {
        // Removes all non-digit characters
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // CNPJ's formed by a sequence of equal numbers are considered errors
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
                cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
                cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
                cnpj.equals("88888888888888") || cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
            throw new IllegalArgumentException("CNPJ inválido: " + cnpj);
        }

        int[] numbers = new int[14];
        for (int i = 0; i < 14; i++) {
            numbers[i] = Character.getNumericValue(cnpj.charAt(i));
        }

        int sum1 = 0;
        int factor1 = 5;
        for (int i = 0; i < 12; i++) {
            sum1 += numbers[i] * factor1;
            factor1 = (factor1 == 2) ? 9 : factor1 - 1;
        }
        int remainder1 = sum1 % 11;
        int digit1 = (remainder1 < 2) ? 0 : (11 - remainder1);

        int sum2 = 0;
        int factor2 = 6;
        for (int i = 0; i < 13; i++) {
            sum2 += numbers[i] * factor2;
            factor2 = (factor2 == 2) ? 9 : factor2 - 1;
        }
        int remainder2 = sum2 % 11;
        int digit2 = (remainder2 < 2) ? 0 : (11 - remainder2);

        return (digit1 == numbers[12]) && (digit2 == numbers[13]);
    }
}
