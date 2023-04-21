import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
Purpose:          Controller for the Applicant Assignments database
Collaboration:    Called by addApplicantEntry() in AddApplicantFormController,
                  updateStatus() in ApplicantInfoChairController,
                  deleteFacultySearch() in FacultySearchDBController,
                  initialize() in SearchInfoChairController, SearchInfoRegularController, and SearchInfoDeptHeadController,
Author:           Ethan Skyler
 */

public class ApplicantAssignmentsDBController {

    /*
    Function:       Gets all searches on which the user is a member
    Parameters:     UserAccount account - the user currently using the software
    Returns:        ObservableList<FacultySearch> containing all the searches linked to this user
     */
    public static ObservableList<FacultySearch> userLinkedToSearches(UserAccount account) {

        ObservableList<FacultySearch> result = FXCollections.observableArrayList();

        // build SQL query
        String query = "SELECT SearchID FROM CommitteeAssignments WHERE UserID = " + account.getUserID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all search data into a FacultySearch object and add to result list
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

    /*
    Function:       Gets all applicants for a specific search
    Parameters:     FacultySearch search - the search in question
    Returns:        ObservableList<SearchApplicant> containing all the applicants to this search
     */
    public static ObservableList<SearchApplicant> applicantsLinkedToSearch(FacultySearch search) {

        ObservableList<SearchApplicant> result = FXCollections.observableArrayList();

        // build SQL query
        String query = "SELECT a.*, b.Applicant_Status FROM SearchApplicants a, ApplicantAssignments b WHERE a.ApplicantID = b.ApplicantID AND b.SearchID = " + search.getSearchID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap applicant data into observable array list
            while (rs.next()) {
                SearchApplicant applicant = new SearchApplicant(rs.getInt("ApplicantID"), rs.getString("First_Name"),
                        rs.getString("Last_Name"), rs.getString("Link_To_Resume"));

                applicant.setStatus(rs.getString("Applicant_Status"));

                result.add(applicant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of applicants
        return result;
    }

    /*
    Function:       Creates a link between an applicant and a search
    Parameters:     FacultySearch search - the search in question
                    SearchApplicant applicant - the applicant in question
    Postconditions: Link between applicant and search has been created
     */
    public static void createApplicantAssignment(SearchApplicant applicant, FacultySearch search) {

        // build SQL query
        String query = "INSERT INTO ApplicantAssignments (SearchID, ApplicantID, Review_Completed, Applicant_Status) VALUES (" + search.getSearchID() + ", " + applicant.getApplicantID() + ", false, '" + applicant.getStatus() + "');";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            // enter link into database
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Function:       Remove the connection between an applicant and a search
    Parameters:     FacultySearch search - the search in question
                    SearchApplicant applicant - the applicant in question
    Postconditions: Link between applicant and search has been deleted
     */
    public static void deleteApplicantAssignment(SearchApplicant applicant, FacultySearch search) {

        // build SQL query
        String query = "DELETE FROM ApplicantAssignments WHERE SearchID = " + search.getSearchID() + " " +
                "AND ApplicantID = " + applicant.getApplicantID() + ";";

        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            // execute deletion
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Function:       Update the status of an applicant to a search
    Parameters:     FacultySearch search - the search in question
                    SearchApplicant applicant - the applicant in question
    Postconditions: Applicant status has been updated
     */
    public static void updateApplicantStatus(SearchApplicant applicant, FacultySearch search) {

        // build SQL query
        String query = "UPDATE ApplicantAssignments " +
                "SET Applicant_Status = '" + applicant.getStatus() + "' " +
                "WHERE ApplicantID = " + applicant.getApplicantID() + " " +
                "AND SearchID = " + search.getSearchID() + ";";

        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            // execute update
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}