import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteManager {

    private static final String DATABASE_URL = "jdbc:sqlite:contacts.db";

    // Save contacts to SQLite database
    public static void saveToSQLite(ListOfContacts contactListApp) {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS contacts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    age INTEGER NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    address TEXT NOT NULL
                );
                """;

        String insertSQL = "INSERT INTO contacts (name, age, email, phone, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            if (conn != null) {
                // Create table if it doesn't exist
                conn.createStatement().execute(createTableSQL);

                // Insert contacts into the database
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    for (Person person : contactListApp.getContactList()) {
                        pstmt.setString(1, person.getName());
                        pstmt.setInt(2, person.getAge());
                        pstmt.setString(3, person.getMail());
                        pstmt.setString(4, person.getPhone());
                        pstmt.setString(5, person.getAddress());
                        pstmt.addBatch(); // Add to batch
                    }
                    pstmt.executeBatch(); // Execute all insertions as a batch
                }
                System.out.println("Contacts successfully saved to SQLite.");
            }
        } catch (SQLException e) {
            System.err.println("Error interacting with SQLite database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get a connection to the SQLite database
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Driver not found. Please add it to your project.", e);
        }
        return DriverManager.getConnection(DATABASE_URL);
    }
}
