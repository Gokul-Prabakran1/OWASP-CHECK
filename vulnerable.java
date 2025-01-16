import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.io.IOException;

public class VulnerableApp {

    public static void main(String[] args) {
        // Using an old version of commons-io library (vulnerable to directory traversal)
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            String content = IOUtils.toString(fis, "UTF-8");
            System.out.println("File Content: " + content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // SQL Injection Vulnerability
        String userInput = "'; DROP TABLE users; --"; // Simulated malicious input
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println("User: " + resultSet.getString("username"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
