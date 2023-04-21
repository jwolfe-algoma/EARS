import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/*
Purpose:          Controller for UserInformationForm
Collaboration:    Created by and UIFPageController
Author:           Jacob Wolfe
 */

public class UserInformationFormController extends UIFormController {

    @FXML
    private Label lbFName;

    @FXML
    private Text txPassword;

    @FXML
    private TextField tfPassword;

    @FXML
    private GridPane gpForm;

    @FXML
    private Label lbPassword;

    @FXML
    private TextField tfEmail;

    @FXML
    private Button btSubmit;

    @FXML
    private Label lbLName;

    @FXML
    private Label lbTitle;

    @FXML
    private Text txEmail;

    @FXML
    private TextField tfLName;

    @FXML
    private TextField tfTitle;

    @FXML
    private Label lbCurrent;

    @FXML
    private Label lbNew;

    @FXML
    private Text txFName;

    @FXML
    private Button btReturn;

    @FXML
    private Label lbEmail;

    @FXML
    private Text txTitle;

    @FXML
    private Text txLName;

    @FXML
    private TextField tfFName;

    UserAccount account;

    /*
    Function:       Creates a new UserInformationFormController
    Parameters:     InformationPackager packager - contains account information
    Preconditions:  UserInformation form has been requested
     */
    public UserInformationFormController(UserAccount account) {
        this.account = account;
    }

    /*
    Function:       Initializes the User Information Form
    Parameters:     none
    Preconditions:  Search Info Chair Page has been requested
    Postconditions: Search Info Chair Page has been loaded
     */
    @FXML
    public void initialize() {
        // load account details
        txFName.setText(account.getFirstName());
        txLName.setText(account.getLastName());
        txTitle.setText(account.getTitle());
        txEmail.setText(account.getEmail());
        txPassword.setText(account.getPassword());
    }

    /*
    Function:       modifies user information in the EARS database
    Parameters:     ActionEvent event: user clicking the submit changes button
    Preconditions:  user has entered new information and clicked submit
    Postconditions: user information is updated in the databse
     */
    public void updateUserAccount(ActionEvent event) {
        // get only fields that are not empty
        if (!tfFName.getText().equals("")) {
            account.setFirstName(tfFName.getText());
        }
        if (!tfLName.getText().equals("")) {
            account.setLastName(tfLName.getText());
        }
        if (!tfTitle.getText().equals("")) {
            account.setTitle(tfTitle.getText());
        }
        if (!tfEmail.getText().equals("")) {
            account.setEmail(tfEmail.getText());
        }
        if (!tfPassword.getText().equals("")) {
            account.setPassword(tfPassword.getText());
        }

        // submit changes
        try {
            UserAccountsDBController.updateAccount(account);
            cancel(event);
        } catch (AdminDeletionException e) {
            createErrorMessage("The system must have at least one administrator account");
        }
    }
}
