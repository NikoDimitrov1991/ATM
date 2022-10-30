import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    // Create a new Bank object with empty lists of users and accounts
    public Bank(String name) { //@param name is the name of the bank
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    // Generate a new universally unique ID for a user
    public String getNewUserUUID() {
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        //Continuing looping until we get a unique ID
        do {
            // Generate the number
            uuid = "";
            for (int i = 0; i < len; i++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }

            //Making sure its unique
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }

    public String getNewAccountUUID() {
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        //Continuing looping until we get a unique ID
        do {
            // Generate the number
            uuid = "";
            for (int i = 0; i < len; i++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }

            //Making sure its unique
            nonUnique = false;
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }

    public void addAccount(Account anAcct) { // Since the account array is public with encapsulation we are reaching it
        this.accounts.add(anAcct);          //@param anAcc the account to add
    }

    public User addUser(String firstName, String lastName, String pin) {
        //Create a new User object and add it to our list
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);
        //Create a savings account for the user
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);
        return newUser;
    }

    //Get the User object associated with a particular userID and pin, if they are valid
    public User userLogin(String userID, String pin) {
        //Search through the list of users
        for (User u : this.users) {
            //Check if the User ID is correct
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }
}




