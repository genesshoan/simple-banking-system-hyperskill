package dev.shoangenes;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BankingSystem {
    private final DatabaseManager db = new DatabaseManager();
    private final CardGenerator cg = new CardGenerator();

    // Functional interfaces for validation
    private final BiFunction<Account, Double, Boolean> hasFunds = (account, amount) -> account.getBalance() >= amount;
    private final Predicate<Double> isPositiveAmount = amount -> amount > 0;

    /**
     * Creates a new account with a unique card number and PIN, and stores it in the database.
     *
     * @return the newly created Account
     */
    public Account createAccount() {
        String cardNumber = cg.generateCardNumber(db);
        String pin = cg.generatePin();

        Account newAccount = new Account(cardNumber, pin, 0.0);
        db.insertAccount(newAccount);
        return newAccount;
    }

    /**
     * Authenticates a user by card number and PIN.
     *
     * @param cardNumber the card number of the account
     * @param pin        the PIN code for authentication
     * @throws IllegalArgumentException if the account does not exist or the PIN is incorrect
     */
    public void login(String cardNumber, String pin) {
        Account account = db.getAccount(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("The account does not exist."));
        if (!account.getPin().equals(pin)) {
            throw new IllegalArgumentException("Wrong PIN.");
        }
    }

    /**
     * Transfers funds from one account to another.
     *
     * @param fromAccount the account to transfer funds from
     * @param toAccount   the card number of the account to transfer funds to
     * @param amount      the amount to transfer
     * @throws IllegalArgumentException if the destination account does not exist,
     *                                  if the amount is not positive,
     *                                  or if there are insufficient funds
     * @throws DatabaseException        if the database operation fails
     */
    public void transferFunds(Account fromAccount, String toAccount, double amount) {
        Account toAccountObj = db.getAccount(toAccount)
                .orElseThrow(() -> new IllegalArgumentException(
                        "The account with card " + toAccount + " does not exist."));

        if (!isPositiveAmount.test(amount)) {
            throw new IllegalArgumentException("The amount must be positive.");
        }

        if (!hasFunds.apply(fromAccount, amount)) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        // Update in-memory balance for the logged-in account
        fromAccount.setBalance(fromAccount.getBalance() - amount);

        try {
            // Persist the transfer in the database
            db.transfer(fromAccount, toAccountObj, amount);
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to transfer funds.", e);
        }
    }

    /**
     * Subtracts income from the account balance and updates the database.
     *
     * @param account the account to subtract income from
     * @param amount  the amount to subtract
     * @return the updated account balance
     * @throws IllegalArgumentException if the amount is not positive or if there are insufficient funds
     * @throws DatabaseException        if the database operation fails
     */
    public double subtractIncome(Account account, double amount) {
        if (!isPositiveAmount.test(amount)) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        if (!hasFunds.apply(account, amount)) {
            throw new IllegalArgumentException("Insufficient funds for transfer.");
        }

        account.setBalance(account.getBalance() - amount);

        try {
            db.updateBalance(account.getNumber(), account.getBalance());
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to subtract income.", e);
        }

        return account.getBalance();
    }

    /**
     * Adds income to the account balance and updates the database.
     *
     * @param account the account to add income to
     * @param amount  the amount to add
     * @return the updated account balance
     * @throws IllegalArgumentException if the amount is not positive
     * @throws DatabaseException        if the database operation fails
     */
    public double addIncome(Account account, double amount) {
        if (!isPositiveAmount.test(amount)) {
            throw new IllegalArgumentException("Income amount must be positive.");
        }

        account.setBalance(account.getBalance() + amount);

        try {
            db.updateBalance(account.getNumber(), account.getBalance());
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to add income.", e);
        }

        return account.getBalance();
    }

    /**
     * Closes the specified account by removing it from the database.
     *
     * @param account the account to close
     * @throws IllegalArgumentException if the account does not exist
     */
    public void closeAccount(Account account) {
        db.getAccount(account.getNumber())
                .orElseThrow(() -> new IllegalArgumentException("The account does not exist."));
        db.deleteAccount(account.getNumber());
    }
}