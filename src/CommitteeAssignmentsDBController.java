import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/*
Purpose:          Controller for the Committee Assignments database
Collaboration:    Called by initialize() and confirmChanges() in AddMembersFormController,
                  deleteFacultySearch() in FacultySearchDBController,
                  openFacultySearchPage() in FacultySearchesRegularController,
                  initialize() in SearchInfoChairController,
                  initialize() in SearchInfoDeptHeadController
Author:           Ethan Skyler
 */

public class CommitteeAssignmentsDBController {

    // get all the accounts on the search committee of a search
    public static ObservableList<UserAccount> getSearchCommitteeAccounts(FacultySearch search) {

        ObservableList<UserAccount> result = FXCollections.observableArrayList();

        // build SQL query and get result
        String query = "SELECT a.*, b.Committee_Rank FROM UserAccounts a, CommitteeAssignments b WHERE b.SearchID = " + search.getSearchID() + " AND a.UserID = b.UserID;";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all user accounts into observable array list
            while (rs.next()) {
                UserAccount account = new UserAccount(rs.getString("Email"), rs.getString("First_Name"),
                        rs.getString("Last_Name"), rs.getString("Pword"), rs.getString("Account_Type"), rs.getInt("UserID"),
                        rs.getString("Title"));
                account.setCommitteeLevel(rs.getString("Committee_Rank"));

                result.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of user accounts 
        return result;
    }

    // get all the accounts not on the search committee of a search
    public static ObservableList<UserAccount> getNotSearchCommitteeAccounts(FacultySearch search) {

        ObservableList<UserAccount> result = FXCollections.observableArrayList();

        // build SQL query and get the result
        String query = "SELECT * FROM UserAccounts WHERE UserID NOT IN (SELECT UserID FROM CommitteeAssignments WHERE SearchID = " + search.getSearchID() + ") AND Account_Type != 'administrator' AND Account_Type != 'department head';";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all user accounts into observable array list
            while (rs.next()) {
                UserAccount account = new UserAccount(rs.getString("Email"), rs.getString("First_Name"),
                        rs.getString("Last_Name"), rs.getString("Pword"), rs.getString("Account_Type"), rs.getInt("UserID"),
                        rs.getString("Title"));

                result.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of user accounts 
        return result;
    }

    // create a link between a user and a search committee
    public static void createCommitteeAssignment(UserAccount account, FacultySearch search) {
        // build SQL query
        String query = "INSERT INTO CommitteeAssignments (SearchID, UserID, Committee_Rank) VALUES (" + search.getSearchID() + ", " + account.getUserID() + ", '" + account.getCommitteeLevel() + "');";

        // execute update
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete a link between a user and a search committee
    public static void deleteCommitteeAssignment(UserAccount account, FacultySearch search) {

        // build SQL query
        String query = "DELETE FROM CommitteeAssignments WHERE UserID = " + account.getUserID() + " AND SearchID = " + search.getSearchID() + "; " +
                "DELETE FROM Comments WHERE UserID = " + account.getUserID() + " AND ApplicantID IN (SELECT ApplicantID FROM ApplicantAssignments WHERE SearchID = " + search.getSearchID() + ");";
        // execute update
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // check if the user is the chair of the search committee
    public static boolean isChair(UserAccount account, FacultySearch search) {
        boolean result = true;

        // build SQL query and get result
        String query = "SELECT Committee_Rank FROM CommitteeAssignments WHERE UserID = " + account.getUserID() + " AND SearchID = " + search.getSearchID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // check if user is chair
            if (rs.next() && rs.getString(1).equals("member")) {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the result of the check
        return result;
    }
}

