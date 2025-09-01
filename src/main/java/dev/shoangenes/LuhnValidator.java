package dev.shoangenes;

public class LuhnValidator {

    /**
     * Validates a credit card number using the Luhn algorithm.
     *
     * @param creditNumber the credit card number to validate
     * @return true if the credit card number is valid, false otherwise
     */
    public static boolean validate(String creditNumber) {
        String cleaned = creditNumber.replaceAll("\\s+", "");
        if (cleaned.length() < 2) return false;

        int checkDigit = cleaned.charAt(cleaned.length() - 1) - '0';
        int sum = luhnSum(cleaned.substring(0, cleaned.length() - 1));
        return (sum + checkDigit) % 10 == 0;
    }

    /**
     * Calculates the check digit for a given credit card number using the Luhn algorithm.
     *
     * @param creditNumberWithoutCheck the credit card number without the check digit
     * @return the calculated check digit
     */
    public static int calculateCheckDigit(String creditNumberWithoutCheck) {
        int sum = luhnSum(creditNumberWithoutCheck);
        int mod = sum % 10;
        return mod == 0 ? 0 : 10 - mod;
    }

    /**
     * Generates a valid credit card number given the base digits.
     *
     * @param baseDigits the digits without the check digit
     * @return a valid credit card number including the check digit
     */
    public static String generateCardNumber(String baseDigits) {
        int checkDigit = calculateCheckDigit(baseDigits);
        return baseDigits + checkDigit;
    }

    /**
     * Calculates the Luhn sum for a credit card number (excluding the check digit).
     *
     * @param digits the credit card number without the check digit
     * @return the Luhn sum
     */
    private static int luhnSum(String digits) {
        int sum = 0;
        boolean alternate = true; // alterna desde el último dígito antes del check

        for (int i = digits.length() - 1; i >= 0; i--) {
            int digit = digits.charAt(i) - '0';
            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum;
    }
}
