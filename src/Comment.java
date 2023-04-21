import javafx.beans.property.*;

import java.time.LocalDate;

/*
Purpose:          Wrapper object for comment data to transfer data between database and UI
Collaboration:    Used in ApplicantInfoRegularController, ApplicantInfoChairController, and CommentsDBController
Author:           Jacob Wolfe
 */

public class Comment {

    IntegerProperty userID = new SimpleIntegerProperty();
    IntegerProperty applicantID = new SimpleIntegerProperty();
    StringProperty comment = new SimpleStringProperty();
    ObjectProperty<LocalDate> date = new SimpleObjectProperty<LocalDate>();
    IntegerProperty commentID = new SimpleIntegerProperty();

    /*
    Function:       Creates a new Comment
    Parameters:     int userID: userID of the user leaving the comment
                    int applicantID: applicantID of the applicant that the comment is made for
                    String comment: text of the comment
                    LocalDate date: date the comment was made
     */
    public Comment(int userID, int applicantID, String comment, LocalDate date) {
        this.userID.set(userID);
        this.applicantID.set(applicantID);
        this.comment.set(comment);
        this.date.set(date);
    }

    // getter and setter methods for all comment data
    public int getCommentID() {
        return commentID.get();
    }

    public IntegerProperty getCommentIDProperty() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID.set(commentID);
    }

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty getUserIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public int getApplicantID() {
        return applicantID.get();
    }

    public IntegerProperty getApplicantIDProperty() {
        return applicantID;
    }

    public void setApplicantID(int applicantID) {
        this.applicantID.set(applicantID);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty getCommentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> getDateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }
}
