import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/*
Purpose:          Controller for the User Accounts database
Collaboration:    Called by validateAccount() in LogInController,
                  initialize() in AccountManagementController,
                  addUserAccountEntry() in AddAccountFormController,
                  updateUserAccount() and deleteUserAccount() in EditAccountFormController,
                  updateUserAccount() in UserInformationFormController
Author:           Ethan Skyler
 */

public class UserAccountsDBController {

    // checks if an email address exists in the database
    public static boolean checkEmail(String email) {
        String query = "SELECT * FROM UserAccounts WHERE Email = '" + DBProperties.sanitize(email) + "';";
        Boolean result = false;

        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            if (rs.next()) {
                result = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // returns the UserAccount object specific to the user who logged in
    public static UserAccount getUserAccount(String email, String password) {
        // default is null if the user does not exist in the database
        UserAccount result = null;

        // check if the email and password exist in the database
        String query = "SELECT * FROM UserAccounts WHERE Email = '" + DBProperties.sanitize(email) + "' AND Pword = '" + DBProperties.sanitize(password) + "';";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // only generates a UserAccount if the account exists in the database
            if (rs.next()) {
                result = new UserAccount(rs.getString("Email"), rs.getString("First_Name"),
                        rs.getString("Last_Name"), rs.getString("Pword"), rs.getString("Account_Type"), rs.getInt("UserID"),
                        rs.getString("Title"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // returns null if the account does not exist or the UserAccount if it does exist
        return result;
    }

    // returns a list of all the user accounts in the EARS DB
    public static ObservableList<UserAccount> getAllUserAccounts() {
        // create a new ObservableList
        ObservableList<UserAccount> result = FXCollections.observableArrayList();

        // get all accounts in the database
        String query = "SELECT * FROM UserAccounts;";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all account data into UserAccount objects and add to list
            while (rs.next()) {
                result.add(new UserAccount(rs.getString("Email"), rs.getString("First_Name"),
                        rs.getString("Last_Name"), rs.getString("Pword"), rs.getString("Account_Type"), rs.getInt("UserID"),
                        rs.getString("Title")));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of all accounts
        return result;
    }

    public static void createAccount(UserAccount account) {
        // build MySQL statement
        String query = "INSERT INTO UserAccounts (First_Name, Last_Name, Email, Pword, Title, Account_Type) " +
                "VALUES ('" + DBProperties.sanitize(account.getFirstName()) + "', " +
                "'" + DBProperties.sanitize(account.getLastName()) + "', " +
                "'" + DBProperties.sanitize(account.getEmail()) + "', " +
                "'" + DBProperties.sanitize(account.getPassword()) + "', " +
                "'" + DBProperties.sanitize(account.getTitle()) + "', " +
                "'" + DBProperties.sanitize(account.getAccountLevel()) + "');";

        // connect to DB and execute statement
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update the UserAccounts DB entry with matching userID with the new details
    public static void updateAccount(UserAccount account) throws AdminDeletionException {

        boolean isAdmin = false;

        // check if user is an administrator
        String query = "SELECT Account_Type FROM UserAccounts WHERE UserID = " + account.getUserID();

        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query);
        ) {
            if (rs.next()) {
                if (rs.getString(1).equals("administrator")) {
                    isAdmin = true;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // prevents the only administrator account from having its account type changed
        if (isAdmin && !account.getAccountLevel().equals("administrator")) {
            query = "SELECT * FROM UserAccounts WHERE Account_Type = 'administrator';";
            int count = 0;

            try (Connection con = DBProperties.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query);
            ) {
                while (rs.next()) {
                    count++;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            if (count == 1) {
                throw new AdminDeletionException();
            }
        }

        // build MySQL query
        query = "UPDATE UserAccounts " +
                "SET First_Name = '" + DBProperties.sanitize(account.getFirstName()) + "', " +
                "Last_Name = '" + DBProperties.sanitize(account.getLastName()) + "', " +
                "Email = '" + DBProperties.sanitize(account.getEmail()) + "', " +
                "Pword = '" + DBProperties.sanitize(account.getPassword()) + "', " +
                "Title = '" + DBProperties.sanitize(account.getTitle()) + "', " +
                "Account_Type = '" + DBProperties.sanitize(account.getAccountLevel()) + "' " +
                "WHERE UserID = " + account.getUserID() + ";";

        // execute update
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete user account and associated data from database
    public static void deleteAccount(UserAccount account) throws AdminDeletionException {

        String query;

        // checks if account is the only administrator
        if (account.getAccountLevel().equals("administrator")) {
            query = "SELECT * FROM UserAccounts WHERE Account_Type = 'administrator';";
            int count = 0;

            try (Connection con = DBProperties.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query);
            ) {
                while (rs.next()) {
                    count++;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            if (count == 1) {
                throw new AdminDeletionException();
            }
        }

        // build query
        query = "DELETE FROM Comments WHERE UserID = " + account.getUserID() + "; " +
                "DELETE FROM CommitteeAssignments WHERE UserID = " + account.getUserID() + "; " +
                "DELETE FROM UserAccounts WHERE UserID = " + account.getUserID() + ";";

        // execute query
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            int i = st.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
