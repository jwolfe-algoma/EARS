import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
Purpose:          Wrapper object for user account data to transfer data between database and UI
Collaboration:    Used in all UIPageControllers and UIFormControllers
Author:           Jacob Wolfe
 */

public class UserAccount {

    private StringProperty email = new SimpleStringProperty();
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty accountLevel = new SimpleStringProperty();
    private IntegerProperty userID = new SimpleIntegerProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty committeeLevel = new SimpleStringProperty();

    // default constructor
    public UserAccount() {

    }

    /*
    Function:       Creates a new UserAccount
    Parameters:     String email: login email for the user
                    String firstName: first name of the user
                    String lastName: last name of the user
                    String password: login password for the user
                    String accountLevel: Account level of the user
                    int userID: ID of the user account
                    String title: Title of the user
     */
    public UserAccount(String email, String firstName, String lastName, String password, String accountLevel, int userID, String title) {
        this.email.set(email);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.password.set(password);
        this.accountLevel.set(accountLevel);
        this.userID.set(userID);
        this.title.set(title);
    }

    // getter and setter methods for attributes
    public String getCommitteeLevel() {
        return committeeLevel.get();
    }

    public StringProperty getCommitteeLevelProperty() {
        return committeeLevel;
    }

    public void setCommitteeLevel(String committeeLevel) {
        this.committeeLevel.set(committeeLevel);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
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

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getAccountLevel() {
        return accountLevel.get();
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel.set(accountLevel);
    }

    public StringProperty getFirstNameProperty() {
        return firstName;
    }

    public StringProperty getLastNameProperty() {
        return lastName;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public StringProperty getAccountLevelProperty() {
        return accountLevel;
    }
}
