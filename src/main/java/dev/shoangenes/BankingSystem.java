package dev.shoangenes;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BankingSystem {
    private final DatabaseManager db;
    private final CardGenerator cg;

    /**
     * Constructs a new BankingSystem with a DatabaseManager and CardGenerator.
     *
     * @throws DatabaseException if a database access error occurs during initialization
     */
    public BankingSystem() {
        try {
            db = new DatabaseManager();
            cg = new CardGenerator();
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to initialize the banking system.", e);
        }
    }

    // Functional interfaces for validation
    private final BiFunction<Account, Double, Boolean> hasFunds = (account, amount) -> account.getBalance() >= amount;
    private final Predicate<Double> isPositiveAmount = amount -> amount > 0;

    /**
     * Creates a new account with a unique card number and PIN, and stores it in the database.
     *
     * @return the newly created Account
     * @throws DatabaseException if a database access error occurs during account creation
     */
    public Account createAccount() {
        try {
            String cardNumber = cg.generateCardNumber(db);
            String pin = cg.generatePin();
            Account newAccount = new Account(cardNumber, pin, 0.0);
            db.insertAccount(newAccount);
            return newAccount;
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to create account.", e);
        }
    }

    /**
     * Logs in to an account using the provided card number and PIN.
     *
     * @param cardNumber the card number of the account
     * @param pin        the PIN of the account
     * @return the Account if login is successful
     * @throws IllegalArgumentException if the account does not exist or the PIN is incorrect
     * @throws DatabaseException        if a database access error occurs during login
     */
    public Account login(String cardNumber, String pin) {
        try {
            Account account = db.getAccount(cardNumber)
                    .orElseThrow(() -> new IllegalArgumentException("The account does not exist."));
            if (!account.getPin().equals(pin)) {
                throw new IllegalArgumentException("Wrong PIN.");
            }
            return account;
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to login.", e);
        }
    }

    /**
     * Transfers funds from one account to another.
     *
     * @param fromAccount the account to transfer funds from
     * @param toAccount   the card number of the account to transfer funds to
     * @param amount      the amount to transfer
     * @throws IllegalArgumentException if the destination account does not exist, if transferring to the same account,
     *                                  if the amount is not positive, or if there are insufficient funds
     * @throws DatabaseException        if a database access error occurs during the transfer
     */
    public void transferFunds(Account fromAccount, String toAccount, double amount) {
        try {
            Account toAccountObj = db.getAccount(toAccount)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "The account with card " + toAccount + " does not exist."));

            if (fromAccount.getNumber().equals(toAccount)) {
                throw new IllegalArgumentException("You can't transfer money to the same account.");
            }

            if (!isPositiveAmount.test(amount)) {
                throw new IllegalArgumentException("The amount must be positive.");
            }

            if (!hasFunds.apply(fromAccount, amount)) {
                throw new IllegalArgumentException("Insufficient funds.");
            }

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            db.transfer(fromAccount, toAccountObj, amount);

        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to transfer funds.", e);
        }
    }

    /**
     * Subtracts income from the specified account.
     *
     * @param account the account to subtract income from
     * @param amount  the amount to subtract
     * @return the new balance of the account
     * @throws IllegalArgumentException if the amount is not positive or if there are insufficient funds
     * @throws DatabaseException        if a database access error occurs during the operation
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
            return account.getBalance();
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to subtract income.", e);
        }
    }

    /**
     * Adds income to the specified account.
     *
     * @param account the account to add income to
     * @param amount  the amount to add
     * @return the new balance of the account
     * @throws IllegalArgumentException if the amount is not positive
     * @throws DatabaseException        if a database access error occurs during the operation
     */
    public double addIncome(Account account, double amount) {
        if (!isPositiveAmount.test(amount)) {
            throw new IllegalArgumentException("Income amount must be positive.");
        }

        account.setBalance(account.getBalance() + amount);

        try {
            db.updateBalance(account.getNumber(), account.getBalance());
            return account.getBalance();
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to add income.", e);
        }
    }

    /**
     * Closes the specified account by removing it from the database.
     *
     * @param account the account to close
     * @throws IllegalArgumentException if the account does not exist
     * @throws DatabaseException        if a database access error occurs during the operation
     */
    public void closeAccount(Account account) {
        try {
            db.getAccount(account.getNumber())
                    .orElseThrow(() -> new IllegalArgumentException("The account does not exist."));
            db.deleteAccount(account.getNumber());
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to close account.", e);
        }
    }
}
