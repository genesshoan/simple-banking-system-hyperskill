package dev.shoangenes;

import java.sql.*;
import org.sqlite.SQLiteDataSource;

public class DatabaseManager {
    private Connection conn;
    private final String URL = "jdbc:sqlite:java/database/cards.s3db";

    /**
     * Constructs a new DatabaseManager and establishes a connection to the database.
     *
     * @throws DatabaseException if a database access error occurs
     */
    public DatabaseManager() {
        connect();
        createCardTable();
    }

    /**
     * Creates the 'cards' table in the database if it does not already exist.
     *
     * @throws DatabaseException if a database access error occurs
     */
    private void createCardTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS cards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                card_number TEXT NOT NULL UNIQUE,
                pin TEXT NOT NULL,
                balance FLOAT NOT NULL
            );
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create 'cards' table.");
        }
    }

    /**
     * Establishes a connection to the SQLite database.
     *
     * @throws DatabaseException if a database access error occurs
     */
    private void connect() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL);

        try {
            conn = dataSource.getConnection();
            if (!conn.isValid(5)) {
                throw new SQLException("Database connection is invalid.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to establish database connection.");
        }
    }

    /**
     * Closes the connection to the SQLite database.
     *
     * @throws DatabaseException if a database access error occurs
     */
    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close database connection.");
        }
    }

    /**
     * Retrieves the last inserted ID from the 'cards' table.
     *
     * @return the last inserted ID as a String
     * @throws DatabaseException if a database access error occurs
     */
    public String getLastId() {
        String query = "SELECT MAX(id) AS 'lastId' FROM cards";
        int lastId = 0;

        try (Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                lastId = rs.getInt("lastId");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve last inserted ID.");
        }
        return lastId + "";
    }

    /**
     * Inserts a new account into the database.
     *
     * @param account the Account object to insert
     * @throws DatabaseException if a database access error occurs
     */
    public void insertAccount(Account account) {
        String insert = "INSERT INTO cards (card_number, pin, balance) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(insert)){
            statement.setString(1, account.getNumber());
            statement.setString(2, account.getPin());
            statement.setDouble(3, account.getBalance());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert account into database.");
        }
    }

    /**
     * Retrieves an account from the database by card number.
     *
     * @param cardNumber the card number of the account to retrieve
     * @return the Account object if found, otherwise null
     * @throws DatabaseException if a database access error occurs
     */
    public Account getAccount(String cardNumber) {
        String query = "SELECT * FROM cards WHERE card_number = ?";
        Account account = null;

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, cardNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                account = new Account(
                        rs.getString("card_number"),
                        rs.getString("pin"),
                        rs.getDouble("balance"));
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve account from database.");
        }
        return account;
    }

    /**
     * Updates the balance of the account with the specified card number.
     *
     * @param cardNumber the card number of the account to update
     * @param newBalance the new balance to set
     * @throws DatabaseException if a database access error occurs
     */
    public void updateBalance(String cardNumber, double newBalance) {
        String update = "UPDATE cards SET balance = ? WHERE card_number = ?";
        try (PreparedStatement statement = conn.prepareStatement(update)) {
            statement.setDouble(1, newBalance);
            statement.setString(2, cardNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update account balance.");
        }
    }

    /**
     * Deletes the account with the specified card number from the database.
     *
     * @param cardNumber the card number of the account to delete
     * @throws DatabaseException if a database access error occurs
     */
    public void deleteAccount(String cardNumber) {
        String delete = "DELETE FROM cards WHERE card_number = ?";
        try (PreparedStatement statement = conn.prepareStatement(delete)) {
            statement.setString(1, cardNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete account from database.");
        }
    }
}
