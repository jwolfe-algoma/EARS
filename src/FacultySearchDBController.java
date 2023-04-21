import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/*
Purpose:          Controller for the Faculty Searches database
Collaboration:    Called by addFacultySearchEntry() in CreateSearchFormController,
                  both updateSearchEntry() and deleteSearchEntry() in EditSearchFormController,
                  initialize() in both FacultySearchesDeptHeadController and FacultySearchesRegularController,
                  and checkSearchExists() in SearchInfoDeptHeadController
Author:           Ethan Skyler
 */

public class FacultySearchDBController {

    // returns a list of all the faculty searches in the EARS DB
    public static ObservableList<FacultySearch> getAllFacultySearches() {
        // create a new ObservableList
        ObservableList<FacultySearch> result = FXCollections.observableArrayList();

        // get all searches in the database
        String query = "SELECT * FROM FacultySearches;";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all search data into FacultySearch objects and add to list
            while (rs.next()) {
                result.add(new FacultySearch(rs.getString("Search_Position"), rs.getDate("Start_Date").toLocalDate(), rs.getDate("End_Date").toLocalDate(),
                        rs.getString("Search_Description"), rs.getString("Requirements"), rs.getInt("SearchID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of all searches
        return result;
    }

    // add a faculty search to the database
    public static void createFacultySearch(FacultySearch search) {

        // build MySQL statement
        String query = "INSERT INTO FacultySearches (Start_Date, End_Date, Search_Position, Search_Description, Requirements) " +
                "VALUES ('" + Date.valueOf(search.getStartDate()) + "', " +
                "'" + Date.valueOf(search.getEndDate()) + "', " +
                "'" + DBProperties.sanitize(search.getPosition()) + "', " +
                "'" + DBProperties.sanitize(search.getDescription()) + "', " +
                "'" + DBProperties.sanitize(search.getRequirements()) + "');";

        // execute statement
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete a faculty search form the database
    public static void deleteFacultySearch(FacultySearch search) {
        // collect all other entries related to search
        ObservableList<SearchApplicant> applicants = ApplicantAssignmentsDBController.applicantsLinkedToSearch(search);
        ObservableList<Comment> comments = FXCollections.observableArrayList();
        ObservableList<UserAccount> accounts = CommitteeAssignmentsDBController.getSearchCommitteeAccounts(search);

        // delete all applicants related to search
        for (SearchApplicant applicant : applicants) {
            comments.addAll(CommentsDBController.getApplicantComments(applicant));
            ApplicantAssignmentsDBController.deleteApplicantAssignment(applicant, search);
            SearchApplicantsDBController.deleteApplicant(applicant);
        }

        // delete all search committee assingnments
        for (UserAccount account : accounts) {
            CommitteeAssignmentsDBController.deleteCommitteeAssignment(account, search);
        }

        // delete all comments related to search
        for (Comment comment : comments) {
            CommentsDBController.deleteComment(comment);
        }

        // build search deletion query
        String query = "DELETE FROM FacultySearches WHERE SearchID = " + search.getSearchID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            // execute search deletion
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update search information in database
    public static void updateFacultySearch(FacultySearch search) {
        // build MySQL query
        String query = "UPDATE FacultySearches " +
                "SET Start_Date = '" + Date.valueOf(search.getStartDate()) + "', " +
                "End_Date = '" + Date.valueOf(search.getEndDate()) + "', " +
                "Search_Position = '" + DBProperties.sanitize(search.getPosition()) + "', " +
                "Search_Description = '" + DBProperties.sanitize(search.getDescription()) + "', " +
                "Requirements = '" + DBProperties.sanitize(search.getRequirements()) + "' " +
                "WHERE SearchID = " + search.getSearchID() + ";";

        // execute query
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get a list of all faculty searches which a user is on the committee of
    public static ObservableList<FacultySearch> getUsersSearches(UserAccount account) {
        ObservableList<FacultySearch> result = FXCollections.observableArrayList();

        // get searches assigned to user from the database
        String query = "SELECT * FROM FacultySearches WHERE SearchID IN (SELECT SearchID FROM CommitteeAssignments WHERE UserID = " + account.getUserID() + ");";

        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap search data into FacultySearch objects and add to list
            while (rs.next()) {
                result.add(new FacultySearch(rs.getString("Search_Position"), rs.getDate("Start_Date").toLocalDate(), rs.getDate("End_Date").toLocalDate(),
                        rs.getString("Search_Description"), rs.getString("Requirements"), rs.getInt("SearchID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of searches
        return result;
    }

    // get all the searches that the user is not on the committee for
    public static ObservableList<FacultySearch> getNotUsersSearches(UserAccount account) {
        ObservableList<FacultySearch> result = FXCollections.observableArrayList();

        // get searches not assigned to user from the database
        String query = "SELECT * FROM FacultySearches WHERE SearchID NOT IN (SELECT SearchID FROM CommitteeAssignments WHERE UserID = " + account.getUserID() + ");";

        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap search data into FacultySearch objects and add to list
            while (rs.next()) {
                result.add(new FacultySearch(rs.getString("Search_Position"), rs.getDate("Start_Date").toLocalDate(), rs.getDate("End_Date").toLocalDate(),
                        rs.getString("Search_Description"), rs.getString("Requirements"), rs.getInt("SearchID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of searches
        return result;
    }

    // confirm a search entry still exists in the database
    public static Boolean getSearchExists(FacultySearch search) {

        Boolean result = false;

        // get all searches in the database
        String query = "SELECT * FROM FacultySearches WHERE SearchID = " + search.getSearchID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all search data into FacultySearch objects and add to list
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of all searches
        return result;
    }
}
