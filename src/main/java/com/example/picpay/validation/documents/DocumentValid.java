package com.example.picpay.validation.documents;

import com.example.picpay.exceptions.DocumentValidationException;
import com.example.picpay.exceptions.messages.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class DocumentValid {

    private static String removeNonDigits(String input) {
        return input.replaceAll("[^0-9]", "");
    }

    private static boolean hasRepeatedDigits(String input) {
        String pattern = "^(\\d)\\1+$";
        return input.matches(pattern);
    }

    private static int calculateDigit(int[] numbers, int[] factors) {
        int sum = 0;
        for (int i = 0; i < factors.length; i++) {
            sum += numbers[i] * factors[i];
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : (11 - remainder);
    }

    public static boolean isCPFValid(String cpf) throws DocumentValidationException {
        cpf = removeNonDigits(cpf);

        if (cpf.length() != 11 || hasRepeatedDigits(cpf)) {
            throw new DocumentValidationException(ExceptionMessages.USER_DOCUMENT_INVALID + cpf, HttpStatus.BAD_REQUEST);
        }

        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int[] factors1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] factors2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        int digit1 = calculateDigit(numbers, factors1);
        int digit2 = calculateDigit(numbers, factors2);

        return (digit1 == numbers[9]) && (digit2 == numbers[10]);
    }

    public static boolean isCNPJValid(String cnpj) throws DocumentValidationException {
        cnpj = removeNonDigits(cnpj);

        if (cnpj.length() != 14 || hasRepeatedDigits(cnpj)) {
            throw new DocumentValidationException(ExceptionMessages.USER_DOCUMENT_INVALID + cnpj, HttpStatus.BAD_REQUEST);
        }

        int[] numbers = new int[14];
        for (int i = 0; i < 14; i++) {
            numbers[i] = Character.getNumericValue(cnpj.charAt(i));
        }

        int[] factors1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] factors2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int digit1 = calculateDigit(numbers, factors1);
        int digit2 = calculateDigit(numbers, factors2);

        return (digit1 == numbers[12]) && (digit2 == numbers[13]);
    }
}
