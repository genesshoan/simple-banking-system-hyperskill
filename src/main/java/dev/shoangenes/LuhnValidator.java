package dev.shoangenes;

import java.util.Arrays;

public class LuhnValidator {
    /**
     * Validates a credit card number using the Luhn algorithm.
     *
     * @param creditCardNumber the credit card number to validate
     * @return true if the credit card number is valid, false otherwise
     */
    public static boolean validate(String creditCardNumber) {
        String cleaned = creditCardNumber.replaceAll("\\s+", "");
        int checkDigit = cleaned.charAt(cleaned.length() - 1) - '0';
        int sum = luhnSum(cleaned.substring(0, cleaned.length() - 1));
        return (sum + checkDigit) % 10 == 0;
    }

    /**
     * Calculates the check digit for a given credit card number using the Luhn algorithm.
     *
     * @param creditCardNumber the credit card number without the check digit
     * @return the calculated check digit
     */
    public static int calculateCheckSum(String creditCardNumber) {
        int sum = luhnSum(creditCardNumber);
        int nextMultipleOf10 = ((sum + 9) / 10) * 10;
        return nextMultipleOf10 - sum;
    }

    /**
     * Calculates the Luhn sum for a given credit card number (excluding the check digit).
     * @param creditCardNumber the credit card number without the check digit
     * @return the Luhn sum
     */
    private static int luhnSum(String creditCardNumber) {
        int sum = 0;
        int[] digits = Arrays.stream(creditCardNumber.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        for (int i = 0; i < digits.length - 1; i++) {
            int posFromRight = digits.length - 1 - i;
            if (posFromRight % 2 == 0) {
                digits[i] *= 2;
            }
            sum += digits[i] > 9 ? digits[i] - 9 : digits[i];
        }
        return sum;
    }
}
