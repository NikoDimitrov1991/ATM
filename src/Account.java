import java.util.ArrayList;

public class Account {
    private  String name; //Name of the Account
    private String uuid; // Account ID Number
    private User holder; // The user object that owns this account
    private ArrayList<Transaction> transactions; // The list of transactions for this account.

    public Account(String name, User holder, Bank theBank){
     this.name=name;// Set account name
     this.holder=holder;// Set account holder
     this.uuid=theBank.getNewAccountUUID(); // Get New Account UUID
     this.transactions=new ArrayList<Transaction>();
    }
    public String getUUID() {
        return uuid;
    }

    public String getSummaryLine(){ //Get the account`s balance
    double balance = this.getBalance();

    //format the summary line, depending whether the balance is negative
        if (balance>=0){
            return String.format("%s : €%.02f : %s", this.uuid, balance, this.name);
        }else{
            return String.format("%s : €(%.02f) : %s", this.uuid, balance, this.name);
        }
    }
    public double getBalance(){
        double balance = 0;
        for (Transaction t : this.transactions){
            balance+= t.getAmount();
        }
        return balance;
    }

    public  void printTransHistory(){ // Print the transaction history of the account
        System.out.printf("(\nTransaction history for account %s\n",this.uuid);
        for (int t = this.transactions.size()-1;t>=0; t--){
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount, String memo){ // Add a new transaction in this account
        // Create transaction object and add it to our list
        Transaction newTrans = new Transaction(amount,memo,this);
        this.transactions.add(newTrans);
    }

}
