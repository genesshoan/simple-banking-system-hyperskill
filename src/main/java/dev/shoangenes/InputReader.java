package dev.shoangenes;

import java.util.Optional;
import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;

    /**
     * Constructs an InputReader with the given Scanner.
     *
     * @param scanner the Scanner to read input from
     */
    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads a card number from the user and validates it using the Luhn algorithm.
     *
     * @param prompt the prompt message to display to the user
     * @return an Optional containing the valid card number, or an empty Optional if invalid
     */
    public Optional<String> readCardNumber(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        if (!LuhnValidator.validate(input)) {
            System.out.println("Invalid card number.");
            return Optional.empty();
        }
        return Optional.of(input);
    }

    /**
     * Reads a PIN from the user.
     *
     * @param prompt the prompt message to display to the user
     * @return an Optional containing the PIN, or an empty Optional if input is empty
     */
    public Optional<String> readPIN(String prompt) {
        System.out.println(prompt);
        String pin = scanner.nextLine();
        if (pin.isEmpty()) {
            System.out.println("PIN cannot be empty.");
            return Optional.empty();
        }
        return Optional.of(pin);
    }

    /**
     * Reads a monetary amount from the user.
     *
     * @param prompt the prompt message to display to the user
     * @return an Optional containing the amount as a Double, or an empty Optional if invalid
     */
    public Optional<Double> readAmount(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine().replace(",", ".");
        try {
            double amount = Double.parseDouble(input);
            return Optional.of(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return Optional.empty();
        }
    }

    /**
     * Reads a line of input from the user after displaying a prompt.
     *
     * @param prompt the prompt message to display to the user
     * @return the line of input entered by the user
     */
    public String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }
}
