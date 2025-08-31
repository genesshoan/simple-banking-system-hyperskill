package dev.shoangenes;

/**
 * Represents a bank account with a number, pin, and balance.
 */
public class Account {
    private String number;
    private String pin;
    private double balance;

    /**
     * Constructs a new Account with the specified number, pin, and balance.
     *
     * @param number the account number
     * @param pin the account pin
     * @param balance the initial balance
     */
    public Account(String number, String pin, double balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    /**
     * Gets the account number.
     * @return the account number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Gets the account pin.
     * @return the account pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * Gets the account balance.
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }
}
