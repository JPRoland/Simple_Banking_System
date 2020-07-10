package banking;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Scanner;

public class Bank {
    Database db;
    private Scanner sc = new Scanner(System.in);
    private boolean looping = true;
    private boolean loggedIn = false;

    public Bank(Database db) {
        this.db = db;
    }

    public void showMenu() throws SQLException {
        while (looping) {
            System.out.println("1. Create an account\n"
                    + "2. Log into account\n"
                    + "0. Exit");
            int action = sc.nextInt();
            switch (action) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 0:
                    System.out.println("Bye!");
                    looping = false;
                    break;
            }
        }
    }

    public void createAccount() throws SQLException {
        Account account = new Account();
        db.insertAccount(account.getAccountNumber(), account.getPinCode());
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(account.getAccountNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getPinCode() + "\n");
    }

    public void login() throws SQLException {
        System.out.println("Enter your account number:");
        String accountNumber = sc.next();
        System.out.println("Enter your PIN:");
        String pin = sc.next();
        if (db.accountExists(accountNumber, pin)) {
            System.out.println("You have successfully logged in!\n");
            loggedIn = true;
            Account customerAccount = db.retrieveAccount(accountNumber, pin);
            customerMenu(customerAccount);
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }

    public void customerMenu(Account customerAccount) throws SQLException {
        while(loggedIn) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            int action = sc.nextInt();
            switch (action) {
                case 1:
                    System.out.printf("Balance: %d", customerAccount.getBalance());
                    break;
                case 2:
                    System.out.println("Enter income:");
                    int income = sc.nextInt();
                    int newBalance = customerAccount.getBalance() + income;
                    customerAccount.setBalance(newBalance);
                    db.updateBalance(customerAccount.getAccountNumber(), newBalance);
                    System.out.println("Income was added!");
                    break;
                case 3:
                    transferFunds(customerAccount);
                    break;
                case 4:
                    db.deleteAccount(customerAccount.getAccountNumber());
                    System.out.println("The account has been closed!\n");
                    loggedIn = false;
                    break;
                case 5:
                    System.out.println("You have successfully logged out!\n");
                    loggedIn = false;
                    break;
                case 0:
                    loggedIn = false;
                    looping = false;
                    break;
            }
        }
    }

    private void transferFunds(Account fromAccount) throws SQLException {
        System.out.println("Enter card number:");
        String toAccountNum = sc.next();

        if (!Account.isValid(toAccountNum)) {
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
            return;
        }

        if (!db.accountExists(toAccountNum)) {
            System.out.println("Such a card does not exist.\n");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int transferAmount = sc.nextInt();

        if (fromAccount.getBalance() < transferAmount) {
            System.out.println("Not enough money!\n");
            return;
        }

        Account toAccount = db.retrieveAccount(toAccountNum);
        db.updateBalance(toAccountNum, toAccount.getBalance() + transferAmount);

        int fromAccountBalance = fromAccount.getBalance() - transferAmount;
        fromAccount.setBalance(fromAccountBalance);
        db.updateBalance(fromAccount.getAccountNumber(), fromAccountBalance);
        System.out.println("Success!\n");
    }
}
