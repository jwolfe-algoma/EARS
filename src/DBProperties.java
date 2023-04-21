import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*
Purpose:          Handles connecting to the EARS DB and sanitization of submitted information
Collaboration:    User by Main and all DB Controllers
Author:           Jacob Wolfe
 */

public class DBProperties {

    /*
    Function:       gets MySQL connection information from the properties.txt file
    Parameters:     none
    Returns:        Properties object containing all the MySQL server connection data
     */
    public static Properties connectionData() {
        Properties result = new Properties();
        String file = "src/properties.txt";

        // reads data from the text file and adds to result
        try (FileInputStream input = new FileInputStream(file)) {
            result.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
    Function:       attempts to connect to a MySQL database
    Parameters:     none
    Returns:        Connection object connecting to the MySQL database
     */
    public static Connection getConnection() throws SQLException {
        // build connection info
        Properties props = connectionData();
        String url = "jdbc:mysql://";
        url += props.get("IP") + ":";
        url += props.get("port") + "/EARS?useSSL=false&allowMultiQueries=true";
        String user = (String) props.get("user");
        String password = (String) props.get("password");

        // return connection
        return DriverManager.getConnection(url, user, password);
    }

    /*
    Function:       creates the EARS database on the MySQL server unless it already exists
    Parameters:     none
    Postconditions: the EARS database has been setup on the MySQL server
     */
    public static void createEARSDB() throws SQLException {

        // create the EARS database if needed
        String query = "CREATE DATABASE IF NOT EXISTS EARS";

        Properties props = DBProperties.connectionData();
        String url = "jdbc:mysql://";
        url += props.get("IP") + ":";
        url += props.get("port") + "?useSSL=false";
        String user = (String) props.get("user");
        String password = (String) props.get("password");


        Connection con = DriverManager.getConnection(url, user, password);
        Statement st = con.createStatement();
        st.executeUpdate(query);

        // read the database setup SQL script
        try {
            query = Files.readString(Path.of("src/database_setup.sql"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // connect to the EARS database and create all the EARS Tables if needed
        con = getConnection();
        st = con.createStatement();

        st.executeUpdate(query);
    }

    /*
    Function:       sanitizes any text entry to ensure that any apostrophes do not interfere with MySQL syntax
    Parameters:     String string: string to be sanitized for compatibility with MySQL
    Returns:        String where all apostrophes have been escaped
     */
    public static String sanitize(String string) {
        return string.replaceAll("'", "\\\\'");
    }
}
