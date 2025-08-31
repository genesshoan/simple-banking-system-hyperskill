package dev.shoangenes;

import java.util.Random;

public class CardGenerator {
    private final String BIN = "400000";
    private final Random rand = new Random();

    /**
     * Generates a new card number using the BIN and the next available account number.
     * @param dbManager the database manager to retrieve the last card number
     * @return a new valid card number
     */
    public String generateCardNumber(DatabaseManager dbManager) {
        long nextAccountNumber = extractAccountNumber(dbManager.getLastCardNumber()) + 1;
        String accountNumber = String.format("%09d", nextAccountNumber);
        String fullNumber = BIN + accountNumber;
        int checkDigit = LuhnValidator.calculateCheckSum(fullNumber);
        return fullNumber + checkDigit;
    }

    /**
     * Generates a random 4-digit PIN.
     * @return a 4-digit PIN as a String
     */
    public String generatePin() {
        return String.format("%04d", rand.nextInt(10000));
    }

    /**
     * Extracts the account number part from a full card number.
     * @param cardNumber the full card number
     * @return the account number as a long
     */
    private long extractAccountNumber(String cardNumber) {
        // cardNumber: "4000001234567890"
        // extract:    "123456789" (posición 6-14)
        String accountPart = cardNumber.substring(BIN.length(), cardNumber.length() - 1);
        return Long.parseLong(accountPart);
    }
}
