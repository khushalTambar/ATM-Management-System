
package model;

import java.time.LocalDateTime;

public class Transaction {
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final String note;
    private final String counterpartyAccountNumber; // for transfers

    public Transaction(TransactionType type, double amount, String note, String counterpartyAccountNumber) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.counterpartyAccountNumber = counterpartyAccountNumber;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }
    public String getCounterpartyAccountNumber() { return counterpartyAccountNumber; }

    @Override
    public String toString() {
        String cp = (counterpartyAccountNumber == null) ? "" : (" | Counterparty: " + counterpartyAccountNumber);
        return timestamp + " | " + type + " | Amount: " + amount + cp + " | " + (note == null ? "" : note);
    }
}
