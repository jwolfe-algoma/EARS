import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/*
Purpose:          Controller class for the Edit Account Form
Collaboration:    Created by AccountManagementController
Author:           Jacob Wolfe
 */

public class EditAccountFormController extends UIFormController {

    @FXML
    private Pane pnMain;

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
    private Text txAccountLevel;

    @FXML
    private Label lbAccountLevel;

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
    private ChoiceBox<String> cbAccountLevel;

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
    Function:       Creates a new EditAccountFormController
    Parameters:     InformationPackager packager - contains user's account information
    Preconditions:  EditAccountForm has been requested
     */
    public EditAccountFormController(UserAccount account) {
        this.account = account;
    }

    /*
    Function:       Initializes the Edit Account Form
    Parameters:     none
    Preconditions:  Edit Account Form has been requested
    Postconditions: Edit Account Form has been loaded
     */
    @FXML
    public void initialize() {
        // update the account details
        txFName.setText(account.getFirstName());
        txLName.setText(account.getLastName());
        txTitle.setText(account.getTitle());
        txEmail.setText(account.getEmail());
        txPassword.setText(account.getPassword());
        txAccountLevel.setText(account.getAccountLevel());
        // load the account level options
        cbAccountLevel.getItems().addAll("administrator", "department head", "regular");
    }

    /*
    Function:       update a user account with new information
    Parameters:     ActionEvent event: user clicking the submit button
    Preconditions:  user information has been entered and submit button has been clicked
    Postconditions: account has been updated in the database
     */
    public void updateUserAccount(ActionEvent event) {
        // only changes information if it has been entered
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
        if (cbAccountLevel.getValue() != null) {
            account.setAccountLevel(cbAccountLevel.getValue());
        }

        // submit wrapped data to database
        try {
            UserAccountsDBController.updateAccount(account);
            cancel(event);
        } catch (AdminDeletionException e) {
            createErrorMessage("The system must have at least one administrator account");
        }
    }

    /*
    Function:       delete a user account
    Parameters:     ActionEvent event: user clicking the delete button
    Preconditions:  delete account button has been clicked
    Postconditions: account has been removed from the database
     */
    public void deleteUserAccount(ActionEvent event) {

        // delete the account from the database
        try {
            UserAccountsDBController.deleteAccount(account);
            cancel(event);
        } catch (AdminDeletionException e) {
            createErrorMessage("The system must have at least one administrator account");
        }
    }
}
