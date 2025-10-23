
package model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String accountNumber;
    private final String holderName;
    private final String pinHash;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String holderName, String pinHash, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pinHash = pinHash;
        this.balance = initialBalance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public String getPinHash() { return pinHash; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public List<Transaction> getTransactions() { return transactions; }

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }
}
