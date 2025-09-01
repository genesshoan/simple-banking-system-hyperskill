package dev.shoangenes;

import java.util.Scanner;

public class Main {
    public  static void main(String[] args) {
        try {
            BankingSystem system = new BankingSystem();
            InputReader reader = new InputReader(new Scanner(System.in));
            BSClient client = new BSClient(system, reader);

            client.run();
        } catch (DatabaseException e) {
            System.out.println("Critical error: " + e.getMessage());
        }
    }
}
