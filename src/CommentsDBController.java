import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/*
Purpose:          Controller for the Comments database
Collaboration:    Called by initialize() and addNewComment() in both ApplicantInfoChairController and ApplicantInfoRegularController,
                  deleteComment() and updateComment() in EditCommentFormController,
                  deleteFacultySearch() in FacultySearchDBController,
                  deleteApplicant() in SearchApplicantsDBController
Author:           Ethan Skyler
 */

public class CommentsDBController {

    public static void createNewComment(UserAccount account, SearchApplicant applicant, Comment comment) {
        // creates a new comment
        String query = "INSERT INTO Comments (UserID, ApplicantID, Comment_Date, Comment_text) VALUES (" + account.getUserID() + ", " +
                applicant.getApplicantID() + ", '" + comment.getDate() + "', '" + DBProperties.sanitize(comment.getComment()) + "');";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            int i = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Used to retrieve the comments on a specific applicant
    public static ObservableList<Comment> getApplicantComments(SearchApplicant applicant) {

        ObservableList<Comment> result = FXCollections.observableArrayList();

        // get all comments on a specific applicant in the database
        String query = "SELECT * FROM Comments WHERE ApplicantID = " + applicant.getApplicantID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all comment data into observable array list
            while (rs.next()) {
                Comment comment = new Comment(rs.getInt("UserID"), rs.getInt("ApplicantID"), rs.getString("Comment_Text"),
                        rs.getDate("Comment_Date").toLocalDate());
                comment.setCommentID(rs.getInt("CommentID"));

                result.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of comments 
        return result;
    }

    //Used to retrieve the comments a specific user has created
    public static ObservableList<Comment> getMemberComments(UserAccount account, SearchApplicant applicant) {

        ObservableList<Comment> result = FXCollections.observableArrayList();

        // get all comments by a specific user in the database
        String query = "SELECT * FROM Comments WHERE UserID = " + account.getUserID() + " " +
                "AND ApplicantID = " + applicant.getApplicantID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // wrap all comment data into observable array list
            while (rs.next()) {
                Comment comment = new Comment(rs.getInt("UserID"), rs.getInt("ApplicantID"), rs.getString("Comment_Text"),
                        rs.getDate("Comment_Date").toLocalDate());
                comment.setCommentID(rs.getInt("CommentID"));

                result.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the list of comments 
        return result;
    }

    // update a comment in the database
    public static void updateComment(Comment comment) {
        // build MySQL query
        String query = "UPDATE Comments " +
                "SET UserID = '" + comment.getUserID() + "', " +
                "ApplicantID = '" + comment.getApplicantID() + "', " +
                "Comment_Date = '" + comment.getDate() + "', " +
                "Comment_Text = '" + DBProperties.sanitize(comment.getComment()) + "' " +
                "WHERE CommentID = " + comment.getCommentID() + ";";

        // execute update
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete a comment from the database
    public static void deleteComment(Comment comment) {
        // build MySQL query
        String query = "DELETE FROM Comments WHERE " +
                "CommentID = " + comment.getCommentID() + ";";

        // execute update
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get the author of a comment
    public static String getAuthor(Comment comment) {

        String result = "";

        // build MySQL query
        String query = "SELECT * FROM UserAccounts WHERE UserID = " + comment.getUserID() + ";";
        try (Connection con = DBProperties.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)
        ) {
            // get author name
            if (rs.next()) {
                result = rs.getString("First_Name") + " " + rs.getString("Last_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the name of the author
        return result;
    }
}