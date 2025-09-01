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
        long nextAccountNumber = Long.parseLong(dbManager.getLastId()) + 1;
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
}
