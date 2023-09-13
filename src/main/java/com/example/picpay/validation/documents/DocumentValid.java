package com.example.picpay.validation.documents;

public class DocumentValid {

    public static boolean isCPFValid(String cpf) {
        // Remova caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifique se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifique se todos os dígitos são iguais (ex. 000.000.000-00)
        if (cpf.matches("(\\d)\\1*")) {
            return false;
        }

        // Cálculo do dígito verificador
        int[] numbers = cpf.chars().map(Character::getNumericValue).toArray();
        int sum1 = 0;
        int sum2 = 0;

        for (int i = 0; i < 9; i++) {
            sum1 += numbers[i] * (10 - i);
            sum2 += numbers[i] * (11 - i);
        }

        int remainder1 = sum1 % 11;
        int remainder2 = sum2 % 11;

        if (remainder1 < 2) {
            if (numbers[9] != 0) {
                return false;
            }
        } else if (numbers[9] != 11 - remainder1) {
            return false;
        }

        if (remainder2 < 2) {
            if (numbers[10] != 0) {
                return false;
            }
        } else if (numbers[10] != 11 - remainder2) {
            return false;
        }

        return true;
    }

    public static boolean isCNPJValid(String cnpj) {
        // Remova caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifique se o CNPJ possui 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Cálculo do dígito verificador
        int[] numbers = cnpj.chars().map(Character::getNumericValue).toArray();
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;

        for (int i = 0; i < 12; i++) {
            int factor = (i % 8) + 2;
            sum1 += numbers[i] * factor;
            sum2 += numbers[i] * (9 - factor);
        }

        sum1 %= 11;
        sum2 %= 11;

        int digit1 = (sum1 < 2) ? 0 : (11 - sum1);
        int digit2 = (sum2 < 2) ? 0 : (11 - sum2);

        return numbers[12] == digit1 && numbers[13] == digit2;
    }
}
