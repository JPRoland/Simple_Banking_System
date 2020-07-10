package banking;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        String dbFileName = "";

        if (!"-fileName".equals(args[0])) {
            System.out.println("DB filename '-fileName {filename}.db' must be provided, exiting...");
            System.exit(1);
        } else {
            dbFileName = args[1];
        }

        Database db = new Database("jdbc:sqlite:" + dbFileName);
        Bank bank = new Bank(db);
        bank.showMenu();
        bank.db.closeConnection();
    }
}
