package dev.shoangenes;

import java.util.Optional;

public class BSClient {
    private final BankingSystem bankingSystem;
    private final InputReader reader;

    /**
     * Constructs a new BSClient with the specified BankingSystem and InputReader.
     *
     * @param bankingSystem the banking system to interact with
     * @param reader        the input reader for user input
     */
    public BSClient(BankingSystem bankingSystem, InputReader reader) {
        this.bankingSystem = bankingSystem;
        this.reader = reader;
    }

    /**
     * Starts the main application loop, displaying menus and handling user input.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            String choice = menu(this::printMainMenu);
            switch (choice) {
                case "1" -> createAccountFlow();
                case "2" -> loginFlow();
                case "3" -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Exiting application...");
    }

    /**
     * Handles the account creation flow, displaying the new account details or an error message.
     */
    private void createAccountFlow() {
        try {
            System.out.println("Your account: " + bankingSystem.createAccount());
        } catch (DatabaseException e) {
            System.out.println("Failed to create account: " + e.getMessage());
        }
    }

    /**
     * Handles the login flow, prompting for card number and PIN, and navigating to the account menu upon success.
     */
    private void loginFlow() {
        Optional<String> cardOpt = reader.readCardNumber("Enter card number:");
        Optional<String> pinOpt = reader.readPIN("Enter PIN:");
        if (cardOpt.isEmpty() || pinOpt.isEmpty()) return;

        try {
            Account current = bankingSystem.login(cardOpt.get(), pinOpt.get());
            System.out.println("Logged in successfully!");
            accountMenu(current);
        } catch (IllegalArgumentException e) {
            System.out.println("Login failed: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Displays the account menu and handles user actions for the logged-in account.
     *
     * @param current the currently logged-in account
     */
    private void accountMenu(Account current) {
        boolean logout = false;
        while (!logout) {
            String choice = menu(this::printLoggedInMenu);
            switch (choice) {
                case "1" -> showBalance(current);
                case "2" -> addIncome(current);
                case "3" -> transferFunds(current);
                case "4" -> logout = closeAccount(current);
                case "5", "0" -> logout = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays the balance of the current account.
     *
     * @param current the currently logged-in account
     */
    private void showBalance(Account current) {
        System.out.println("Balance: " + current.getBalance());
    }

    /**
     * Prompts the user to add income to the current account and processes the addition.
     *
     * @param current the currently logged-in account
     */
    private void addIncome(Account current) {
        reader.readAmount("Enter income:").ifPresent(amount -> {
            try {
                bankingSystem.addIncome(current, amount);
                System.out.println("Income added!");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid amount: " + e.getMessage());
            } catch (DatabaseException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        });
    }

    /**
     * Prompts the user to transfer funds from the current account to another account and processes the transfer.
     *
     * @param current the currently logged-in account
     */
    private void transferFunds(Account current) {
        Optional<String> toCard = reader.readCardNumber("Enter destination card number:");
        toCard.ifPresent(to -> {
            reader.readAmount("Enter amount:").ifPresent(amount -> {
                try {
                    bankingSystem.transferFunds(current, to, amount);
                    System.out.println("Transfer successful!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Transfer failed: " + e.getMessage());
                } catch (DatabaseException e) {
                    System.out.println("Database error: " + e.getMessage());
                }
            });
        });
    }

    /**
     * Closes the current account and logs out the user.
     *
     * @param current the currently logged-in account
     * @return true if the account was closed successfully, false otherwise
     */
    private boolean closeAccount(Account current) {
        try {
            bankingSystem.closeAccount(current);
            System.out.println("Account closed.");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Close failed: " + e.getMessage());
            return false;
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays a menu using the provided printer function and reads the user's choice.
     *
     * @param printer a Runnable that prints the menu options
     * @return the user's choice as a String
     */
    private String menu(Runnable printer) {
        printer.run();
        System.out.println("Enter your choice:");
        return reader.readLine("");
    }

    /**
     * Prints the main menu options to the console.
     */
    private void printMainMenu() {
        System.out.println("=== Main menu ===");
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("3. Exit");
    }

    /**
     * Prints the logged-in account menu options to the console.
     */
    private void printLoggedInMenu() {
        System.out.println("=== Account menu ===");
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }
}