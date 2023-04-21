import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
Purpose:          Wrapper object for Search Applicant data to transfer data between database and UI
Collaboration:    Used in Search Information pages, ApplicantInfoRegularController, ApplicantInfoChairController, CommentsDBController, SearchApplicantsDBController, ApplicantAssignmentsDBController
Author:           Jacob Wolfe
 */

public class SearchApplicant {

    IntegerProperty applicantID = new SimpleIntegerProperty();
    StringProperty firstName = new SimpleStringProperty();
    StringProperty lastName = new SimpleStringProperty();
    StringProperty linkToResume = new SimpleStringProperty();
    StringProperty status = new SimpleStringProperty();

    // default constructor
    public SearchApplicant() {

    }

    /*
    Function:       Creates a new SearchApplicant
    Parameters:     int applicantID: ID of the search applicant
                    String firstName: first name of the applicant
                    String lastName: last name of the applicant
                    String linkToResume: link to the applicant's resume
     */
    public SearchApplicant(int applicantID, String firstName, String lastName, String linkToResume) {
        this.applicantID.set(applicantID);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.linkToResume.set(linkToResume);
        this.status.set("Review Incomplete");
    }

    // getter and setters for all attributes
    public String getStatus() {
        return status.get();
    }

    public StringProperty getStatusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
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

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty getFirstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty getLastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getLinkToResume() {
        return linkToResume.get();
    }

    public StringProperty getLinkToResumeProperty() {
        return linkToResume;
    }

    public void setLinkToResume(String linkToResume) {
        this.linkToResume.set(linkToResume);
    }
}
