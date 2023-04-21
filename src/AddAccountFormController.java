import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/*
Purpose:          Controller for the Add Account Form
Collaboration:    Created by AccountManagementController upon attempting to add a new user account
                  Calls methods from UserAccountsDBController when submitting a new user account
Author:           Jacob Wolfe
 */

public class AddAccountFormController extends UIFormController {

    @FXML
    private Pane pnMain;

    @FXML
    private Label lbFName;

    @FXML
    private TextField tfPassword;

    @FXML
    private GridPane gpForm;

    @FXML
    private Label lbPassword;

    @FXML
    private TextField tfEmail;

    @FXML
    private Label lbAccountLevel;

    @FXML
    private Button btSubmit;

    @FXML
    private Label lbLName;

    @FXML
    private Label lbTitle;

    @FXML
    private TextField tfLName;

    @FXML
    private ChoiceBox<String> cbAccountLevel;

    @FXML
    private TextField tfTitle;

    @FXML
    private Label lbCurrent;

    @FXML
    private Label lbNew;

    @FXML
    private Button btReturn;

    @FXML
    private Label lbEmail;

    @FXML
    private TextField tfFName;

    /*
    Function:       Initializes the Add Account Form
    Parameters:     none
    Preconditions:  Add Account Form has been requested
    Postconditions: Add Account Form has been loaded
     */
    @FXML
    public void initialize() {
        // adds the account level options to the choice box
        cbAccountLevel.getItems().addAll("administrator", "department head", "regular");
    }

    /*
    Function:       Submits the entered account details to the User Accounts Database
    Parameters:     ActionEvent e - the user clicking the btSubmit Button
    Preconditions:  The user has entered information and clicked on the Submit button
    Postconditions: The account has been added to the database
     */
    public void addUserAccountEntry(ActionEvent e) {

        // initialize account and error variables
        UserAccount account = new UserAccount();
        String errorMsg = "The following fields must be filled in: ";
        Boolean hasError = false;

        // check if any fields have been left blank and builds error message
        if (tfFName.getText().equals("")) {
            errorMsg += "First Name, ";
            hasError = true;
        }
        if (tfLName.getText().equals("")) {
            errorMsg += "Last Name, ";
            hasError = true;
        }
        if (tfTitle.getText().equals("")) {
            errorMsg += "Title, ";
            hasError = true;
        }
        if (tfEmail.getText().equals("")) {
            errorMsg += "Login Email, ";
            hasError = true;
        }
        if (tfPassword.getText().equals("")) {
            errorMsg += "Password, ";
            hasError = true;
        }
        if (cbAccountLevel.getValue() == null) {
            errorMsg += "Account Level, ";
            hasError = true;
        }

        if (hasError) {
            // create error popup
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
            createErrorMessage(errorMsg);
        } else {
            // package account data, submit to database, close the form
            account.setFirstName(tfFName.getText());
            account.setLastName(tfLName.getText());
            account.setTitle(tfTitle.getText());
            account.setEmail(tfEmail.getText());
            account.setPassword(tfPassword.getText());
            account.setAccountLevel(cbAccountLevel.getValue());
            UserAccountsDBController.createAccount(account);
            cancel(e);
        }
    }
}
