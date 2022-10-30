import java.util.Date;

public class Transaction {
    private double amount; // Amount of the transaction.
    private Date timestamp; //Time and date of this transaction.
    private String memo; // A memo for this transaction.
    private Account inAccount; // The account in which the transaction was performed.

    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    public Transaction(double amount, String memo, Account inAccount) {
        // Constructor overloading calling the constructor with 2 arguments
        this(amount, inAccount);
        this.memo = memo; // Set the memo
    }

    public double getAmount() {
        return this.amount;
    }

    public String getSummaryLine() { // Gets a string summarizing the transaction
        if (this.amount >= 0) {
            return String.format("%s : €%.02f : %s",
                    this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : €(%.02f) : %s",
                    this.timestamp.toString(), -this.amount, this.memo);

        }
    }
}
