
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
                        System.out.print("Desired account number: ");
                        String accNum = sc.nextLine().trim();
                        System.out.print("PIN (min 4 digits): ");
                        String pin = sc.nextLine().trim();
                        System.out.print("Initial deposit (can be 0): ");
                        double init = Double.parseDouble(sc.nextLine().trim());
                        String created = service.signup(name, accNum, pin, init);
                        System.out.println("Account created: " + created);
                        break;
                    }
                    case "2": { // Login + session loop
                        System.out.print("Account number: ");
                        String accNum = sc.nextLine().trim();
                        System.out.print("PIN: ");
                        String pin = sc.nextLine().trim();
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
                                        System.out.print("Amount to deposit: ");
                                        double amt = Double.parseDouble(sc.nextLine().trim());
                                        System.out.print("Note (optional): ");
                                        String note = sc.nextLine();
                                        double bal = service.deposit(current, amt, note);
                                        System.out.println("Deposited. New balance: " + bal);
                                        break;
                                    }
                                    case "3": {
                                        System.out.print("Amount to withdraw: ");
                                        double amt = Double.parseDouble(sc.nextLine().trim());
                                        System.out.print("Note (optional): ");
                                        String note = sc.nextLine();
                                        double bal = service.withdraw(current, amt, note);
                                        System.out.println("Withdrawn. New balance: " + bal);
                                        break;
                                    }
                                    case "4": {
                                        System.out.print("Destination account: ");
                                        String to = sc.nextLine().trim();
                                        System.out.print("Amount to transfer: ");
                                        double amt = Double.parseDouble(sc.nextLine().trim());
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
