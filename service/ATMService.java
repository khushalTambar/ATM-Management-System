
package service;

import dao.AccountDAO;
import model.Account;
import model.Transaction;
import model.TransactionType;
import util.SimplePasswordUtil;

import java.util.List;
import java.util.Optional;

public class ATMService {
    private final AccountDAO accountDAO;

    public ATMService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public String signup(String holderName, String accountNumber, String rawPin, double initialDeposit) throws IllegalArgumentException {
        if (holderName == null || holderName.isBlank()) throw new IllegalArgumentException("Holder name required");
        if (accountNumber == null || accountNumber.isBlank()) throw new IllegalArgumentException("Account number required");
        if (accountDAO.existsByAccountNumber(accountNumber)) throw new IllegalArgumentException("Account number already exists");
        if (rawPin == null || rawPin.length() < 4) throw new IllegalArgumentException("PIN must be at least 4 digits");
        if (initialDeposit < 0) throw new IllegalArgumentException("Initial deposit cannot be negative");

        String pinHash = SimplePasswordUtil.hash(rawPin);
        Account acc = new Account(accountNumber, holderName, pinHash, initialDeposit);
        acc.addTransaction(new Transaction(TransactionType.SIGNUP, initialDeposit, "Account created", null));
        accountDAO.save(acc);
        return accountNumber;
    }

    public Account login(String accountNumber, String rawPin) throws SecurityException {
        Optional<Account> opt = accountDAO.findByAccountNumber(accountNumber);
        if (opt.isEmpty()) throw new SecurityException("Invalid account number");
        Account acc = opt.get();
        if (!SimplePasswordUtil.matches(rawPin, acc.getPinHash())) throw new SecurityException("Invalid PIN");
        return acc;
    }

    public double checkBalance(Account acc) {
        acc.addTransaction(new Transaction(TransactionType.BALANCE_CHECK, 0, "Balance check", null));
        return acc.getBalance();
    }

    public double deposit(Account acc, double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        acc.setBalance(acc.getBalance() + amount);
        acc.addTransaction(new Transaction(TransactionType.DEPOSIT, amount, note, null));
        accountDAO.save(acc);
        return acc.getBalance();
    }

    public double withdraw(Account acc, double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive");
        if (amount > acc.getBalance()) throw new IllegalArgumentException("Insufficient funds");
        acc.setBalance(acc.getBalance() - amount);
        acc.addTransaction(new Transaction(TransactionType.WITHDRAW, amount, note, null));
        accountDAO.save(acc);
        return acc.getBalance();
    }

    public void transfer(Account from, String toAccountNumber, double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Transfer amount must be positive");
        if (toAccountNumber.equals(from.getAccountNumber())) throw new IllegalArgumentException("Cannot transfer to same account");
        Account to = accountDAO.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));
        if (from.getBalance() < amount) throw new IllegalArgumentException("Insufficient funds");

        // debit from
        from.setBalance(from.getBalance() - amount);
        from.addTransaction(new Transaction(TransactionType.TRANSFER_OUT, amount, note, toAccountNumber));
        accountDAO.save(from);

        // credit to
        to.setBalance(to.getBalance() + amount);
        to.addTransaction(new Transaction(TransactionType.TRANSFER_IN, amount, note, from.getAccountNumber()));
        accountDAO.save(to);
    }

    public List<Account> listAccounts() {
        return accountDAO.findAll();
    }
}
