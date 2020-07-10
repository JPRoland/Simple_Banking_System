package banking;

import java.sql.*;

public class Database {
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private DatabaseMetaData metaData;

    public Database(String url) throws SQLException {
        conn = DriverManager.getConnection(url);
        metaData = conn.getMetaData();
        rs = metaData.getTables(null, null, "card", null);
        if (!rs.next()) {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE card (\n"
                    + "     id INTEGER PRIMARY KEY,\n"
                    + "     number TEXT,\n"
                    + "     pin TEXT,\n"
                    + "     balance INTEGER DEFAULT 0\n"
                    + ");";
            stmt.executeUpdate(sql);
        }
        rs.close();
    }

    public void insertAccount(String cardNumber, String pin) throws SQLException {
        String sql = "INSERT INTO card(number, pin) VALUES (?,?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cardNumber);
        pstmt.setString(2, pin);
        pstmt.executeUpdate();
    }

    public boolean accountExists(String cardNumber, String pin) throws SQLException {
        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cardNumber);
        pstmt.setString(2, pin);
        rs = pstmt.executeQuery();
        return rs.next();
    }

    public boolean accountExists(String cardNumber) throws SQLException {
        String sql = "SELECT * FROM card WHERE number = ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cardNumber);

        rs = pstmt.executeQuery();
        return rs.next();
    }

    public Account retrieveAccount(String cardNumber, String pin) throws SQLException {
        Account account = null;
        String sql = "SELECT number, pin, balance "
                + "FROM card WHERE number == ? AND pin == ?";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cardNumber);
        pstmt.setString(2, pin);
        rs =  pstmt.executeQuery();
        if (rs.next()) {
            String dbCardNumber = rs.getString("number");
            String dbPin = rs.getString("pin");
            int balance = rs.getInt("balance");
            account = new Account(dbCardNumber, dbPin, balance);
        }

        return account;
    }

    public Account retrieveAccount(String cardNumber) throws SQLException {
        Account account = null;
        String sql = "SELECT number, balance, pin "
                + "FROM card WHERE number == ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cardNumber);
        rs =  pstmt.executeQuery();
        if (rs.next()) {
            String dbCardNumber = rs.getString("number");
            String dbPin = rs.getString("pin");
            int balance = rs.getInt("balance");
            account = new Account(dbCardNumber, dbPin, balance);
        }

        return account;
    }

    public void updateBalance(String cardNumber, int amount) throws SQLException {
        String sql = "UPDATE card SET balance = ? "
                + "WHERE number = ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, amount);
        pstmt.setString(2, cardNumber);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void deleteAccount(String cardnumber) throws SQLException {
        String sql = "DELETE FROM card WHERE number = ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cardnumber);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}
