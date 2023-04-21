import javafx.collections.ObservableList;

import java.sql.*;

/*
Purpose:          Controller for the Search Applicants database
Collaboration:    Called by addApplicantEntry() in AddApplicantFormController,
                  updateApplicantEntry() and deleteApplicantEntry() in EditApplicantFormController,
                  deleteFacultySearch() in FacultySearchDBController
Author:           Ethan Skyler
 */

public class SearchApplicantsDBController {

    // add a new applicant to the database
    public static SearchApplicant createApplicant(SearchApplicant applicant) {
        // build MySQL statement
        String queryUpdate = "INSERT INTO SearchApplicants (First_Name, Last_Name, Link_To_Resume) " +
                "VALUES ('" + DBProperties.sanitize(applicant.getFirstName()) + "', " +
                "'" + DBProperties.sanitize(applicant.getLastName()) + "', " +
                "'" + DBProperties.sanitize(applicant.getLinkToResume()) + "');";

        String queryID = "SELECT LAST_INSERT_ID();";

        // execute statement
        try (Connection con = DBProperties.getConnection();
             Statement stUpdate = con.createStatement();
             Statement stID = con.createStatement();
        ) {
            int i = stUpdate.executeUpdate(queryUpdate);
            ResultSet rs = stID.executeQuery(queryID);
            if (rs.next()) {
                applicant.setApplicantID(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return applicant;
    }

    // delete an applicant from the database
    public static void deleteApplicant(SearchApplicant applicant) {

        ObservableList<Comment> comments = CommentsDBController.getApplicantComments(applicant);

        // delete all comments associated with applicant
        for (Comment comment : comments) {
            CommentsDBController.deleteComment(comment);
        }

        // build SQL query
        String query = "DELETE FROM ApplicantAssignments WHERE ApplicantID = " + applicant.getApplicantID() + "; " +
                "DELETE FROM SearchApplicants WHERE ApplicantID = " + applicant.getApplicantID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            // execute deletion
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update the information of a search applicant
    public static void updateApplicant(SearchApplicant applicant) {
        // build MySQL statement
        String query = "UPDATE SearchApplicants " +
                "SET First_Name = '" + DBProperties.sanitize(applicant.getFirstName()) + "', " +
                "Last_Name = '" + DBProperties.sanitize(applicant.getLastName()) + "', " +
                "Link_To_Resume = '" + DBProperties.sanitize(applicant.getLinkToResume()) + "'" +
                "WHERE ApplicantID = " + applicant.getApplicantID() + ";";

        // execute statement
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
