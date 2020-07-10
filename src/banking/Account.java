package banking;

import java.util.Random;

public class Account {
    private static final int PIN_LENGTH = 4;
    private static final int ACCOUNT_NUMBER_LENGTH = 9;

    private String accountNumber;
    private String pin;
    private int balance = 0;

    Account() {
        this.accountNumber = generateAccountNumber();
        this.pin = generatePIN();
    }

    Account(String accountNumber, String pin, int balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    int getBalance() {
        return balance;
    }

    void setBalance(int balance) {
        this.balance = balance;
    }

    String getAccountNumber() {
        return accountNumber;
    }

    String getPinCode() {
        return pin;
    }

    static String generateAccountNumber() {
        String bin = "400000";

        StringBuilder builder = new StringBuilder(bin);
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }

        int checkDigit = generateCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    static String generatePIN() {
        Random random = new Random();
        String pin = "";
        for (int i = 0; i < PIN_LENGTH; i++) {
            int digit = random.nextInt(10);
            pin = pin.concat(String.valueOf(digit));
        }
        return pin;
    }

    private static int generateCheckDigit(String number) {
        int sum = 0;
        for(int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        int mod = sum % 10;
        return mod == 0 ? 0 : 10 - mod;
    }

    public static boolean isValid(String number) {
        int sum = 0;
        for(int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        return sum % 10 == 0;
    }
}
