import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private String firstName; // First name of the user
    private String lastName;  // Last name of the user
    private String uuid; // Unique Universal Identifier
    private byte[] pinHash; //MD5 hash for user`s pin for security reasons
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;

        // For security reasons we are storing the Pin in MD5 hash, rather than the original value.
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID(); // get a new, unique ID for the user

        this.accounts = new ArrayList<Account>(); // create a empty list of accounts
        System.out.printf("New user %s, %s with ID %s created. \n", lastName, firstName, this.uuid);
    }

    public void addAccount(Account anAcct) { // Since the account array is public with encapsulation we are reaching it
        this.accounts.add(anAcct);           //@param anAcc the account to add
    }

    public String getUUID() {
        return uuid;
    }

    // Check whether a given pin matches the true User pin
    public boolean validatePin(String aPin) { // @param aPin the pin to check
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public String getFirstName() {
        return firstName;
    }

    public void printAccountSummary() {
        System.out.printf("\n\n%s`s accounts summary\n", this.firstName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, this.accounts.get(i).getSummaryLine()); // each account implements own summary line
        }
        System.out.println();
    }

    public int numAccounts() { // Get the number of the accounts of the user
        return this.accounts.size();
    }

    public void printAcctTransHistory(int accIdx) {
        this.accounts.get(accIdx).printTransHistory();
    }

    public double getAcctBalance(int acctIdx) { // Get the balance of a particular account
        return this.accounts.get(acctIdx).getBalance();
    }

    public String getAcctUUID(int acctIdx) { // Get theUUID of a particular account
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAccountTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
