package ui;

import dao.InMemoryAccountDAO;
import service.ATMService;
import model.Account;

import java.util.Scanner;

public class ATMApp {

    private static void printMainMenu() {
        System.out.println("\n=== ATM Menu ===");
        System.out.println("1. Sign Up");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose: ");
    }

    private static void printSessionMenu(String holder, String acct) {
        System.out.println("\n=== Welcome, " + holder + " (Acc: " + acct + ") ===");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Transaction History");
        System.out.println("6. Logout");
        System.out.print("Choose: ");
    }

    // Helper method to safely read numeric (double) input
    private static double readDouble(Scanner sc, String prompt) {
        double value = 0;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Enter a non-negative number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }
        return value;
    }

    // Helper method to read numeric PIN
    private static String readNumericPin(Scanner sc, String prompt) {
        String pin;
        while (true) {
            System.out.print(prompt);
            pin = sc.nextLine().trim();
            if (pin.matches("\\d{4,}")) {
                break;
            } else {
                System.out.println("PIN must be at least 4 digits and numeric only.");
            }
        }
        return pin;
    }

    // Helper method to read numeric account number
    private static String readAccountNumber(Scanner sc, String prompt) {
        String acc;
        while (true) {
            System.out.print(prompt);
            acc = sc.nextLine().trim();
            if (acc.matches("\\d+")) {
                break;
            } else {
                System.out.println("Account number must contain digits only.");
            }
        }
        return acc;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATMService service = new ATMService(new InMemoryAccountDAO());

        boolean running = true;
        while (running) {
            try {
                printMainMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1": { // Sign Up
                        System.out.print("Holder name: ");
                        String name = sc.nextLine().trim();
                        String accNum = readAccountNumber(sc, "Desired account number: ");
                        String pin = readNumericPin(sc, "PIN (min 4 digits): ");
                        double init = readDouble(sc, "Initial deposit (can be 0): ");
                        String created = service.signup(name, accNum, pin, init);
                        System.out.println("Account created: " + created);
                        break;
                    }
                    case "2": { // Login + session loop
                        String accNum = readAccountNumber(sc, "Account number: ");
                        String pin = readNumericPin(sc, "PIN: ");
                        Account current = service.login(accNum, pin);
                        boolean inSession = true;
                        while (inSession) {
                            try {
                                printSessionMenu(current.getHolderName(), current.getAccountNumber());
                                String c = sc.nextLine().trim();
                                switch (c) {
                                    case "1": {
                                        double bal = service.checkBalance(current);
                                        System.out.println("Balance: " + bal);
                                        break;
                                    }
                                    case "2": {
                                        double amt = readDouble(sc, "Amount to deposit: ");
                                        System.out.print("Note (optional): ");
                                        String note = sc.nextLine();
                                        double bal = service.deposit(current, amt, note);
                                        System.out.println("Deposited. New balance: " + bal);
                                        break;
                                    }
                                    case "3": {
                                        double amt = readDouble(sc, "Amount to withdraw: ");
                                        System.out.print("Note (optional): ");
                                        String note = sc.nextLine();
                                        double bal = service.withdraw(current, amt, note);
                                        System.out.println("Withdrawn. New balance: " + bal);
                                        break;
                                    }
                                    case "4": {
                                        String to = readAccountNumber(sc, "Destination account: ");
                                        double amt = readDouble(sc, "Amount to transfer: ");
                                        System.out.print("Note (optional): ");
                                        String note = sc.nextLine();
                                        service.transfer(current, to, amt, note);
                                        System.out.println("Transfer successful.");
                                        break;
                                    }
                                    case "5": {
                                        System.out.println("=== Transactions ===");
                                        current.getTransactions().forEach(t -> System.out.println(t));
                                        break;
                                    }
                                    case "6": {
                                        inSession = false;
                                        System.out.println("Logged out.");
                                        break;
                                    }
                                    default:
                                        System.out.println("Invalid choice.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;
                    }
                    case "3": {
                        running = false;
                        System.out.println("Goodbye.");
                        break;
                    }
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }
}
