import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Init Scanner

        Bank theBank = new Bank("UniCredit Bank"); // Init Bank

        User aUser = theBank.addUser("Nikolay", "Dimitrov", "1234"); // Add user, witch also creates a savings account

        Account newAccount = new Account("Checking", aUser, theBank); // Add a checking account for the existing user
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {
            // Stay in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // Stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc) { //
        String userID;
        String pin;
        User authUser;
        do {
            // prompt the user for userID/pin combo until a correct one is reached
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID:    ");
            userID = sc.nextLine();
            System.out.print("Enter PIN:    ");
            pin = sc.nextLine();
            authUser = theBank.userLogin(userID, pin); // Try to get the user object corresponding to the ID and pin combo
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combination.Please try again");
            }
        } while (authUser == null);// Looping until successful login
        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc) {// Print a summary of the user`s accounts
        theUser.printAccountSummary();
        int choice;
        //user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n ", theUser.getFirstName());
            System.out.println("   (1) Show transaction history");
            System.out.println("    (2) Withdrawal");
            System.out.println("    (3) Deposit");
            System.out.println("    (4) Transfer");
            System.out.println("    (5) Quit");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid Input. Please choose 1-5");
            }
        } while (choice < 1 || choice > 5);
        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
        }
        //Redisplay this menu unless the user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc); // Recursive calling
        }
    }

    public static void showTransHistory(User theUser, Scanner sc) { // Show the transaction history for an account
        int theAcct;
        // get account whose transaction history to look at
        do {
            System.out.printf("Enter the number(1-%d) of the account whose transactions you want to see:", theUser.numAccounts());
            theAcct = sc.nextInt() - 1; // Indexing starts from 0;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());
        //print the transaction history
        theUser.printAcctTransHistory(theAcct);

    }

    public static void transferFunds(User theUser, Scanner sc) {
        //Inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // Get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from:   ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
        // Get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer to:   ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max €%.02f):   €", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than\n the account balance of €%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        //Finally, do the transfer
        theUser.addAccountTransaction(fromAcct, -1 * amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAccountTransaction(toAcct, amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(fromAcct)));
    }

    public static void withdrawFunds(User theUser, Scanner sc) { //Process a fund withdraw from an account
        //Inits
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        // Get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account/n" +
                    "to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max €%.02f): € ", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than\n the account balance of €%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        // Gabble up rest of previous input
        sc.nextLine();

        // Get a memo
        System.out.print("Enter a memo:");
        memo = sc.nextLine();

        //Do the withdrawal
        theUser.addAccountTransaction(fromAcct, -1 * amount, memo);
    }

    public static void depositFunds(User theUser, Scanner sc) { // Process a fund deposit to an account

        //Inits
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        // Get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to deposit in:  ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);
        // get the amount to transfer
        do {
            System.out.print("Enter the € amount to deposit :  ");
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);
        // Gabble up rest of previous input
        sc.nextLine();

        // Get a memo
        System.out.println("Enter a memo:");
        memo = sc.nextLine();

        // Do the deposit
        theUser.addAccountTransaction(toAcct, amount, memo);
    }
}
// This project is a console-based application with five different classes (account holder, account, bank transaction, bank and particular ATM of the bank). If you start running the program, you will be prompted with user id and user pin. If you entered it successfully, then you unlock all the functionalities which typically exist in an ATM. The operations you can perform in this project are show transactions history, withdraw, deposit, transfer and quit.

