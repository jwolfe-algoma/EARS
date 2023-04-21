import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/*
Purpose:          Controller for the Account Management Page
Collaboration:    Created by LogInController if account type is administrator
Author:           Jacob Wolfe
 */

public class AccountManagementController extends UIPageController {

    @FXML
    private TableView<UserAccount> tvAccounts;

    @FXML
    private TableColumn<UserAccount, String> tcFName;

    @FXML
    private Button btProfile;

    @FXML
    private TableColumn<UserAccount, String> tcAccountLevel;

    @FXML
    private TableColumn<UserAccount, String> tcPassword;

    @FXML
    private TableColumn<UserAccount, String> tcEmail;

    @FXML
    private Button btSubmit;

    @FXML
    private TableColumn<UserAccount, String> tcLName;

    @FXML
    private Button btLogOut;

    /*
    Function:       Creates a new AccountManagementController
    Parameters:     InformationPackager packager - contains user's account information
    Preconditions:  User has logged in with an administrator account
     */
    public AccountManagementController(InformationPackager packager) {

    }

    /*
    Function:       Initializes the account management page
    Parameters:     none
    Preconditions:  Account management page has been requested
    Postconditions: Account management page has been loaded
     */
    @FXML
    public void initialize() {
        // gets all the user accounts in the EARS DB and adds them to the Accounts TableView
        ObservableList<UserAccount> accounts = UserAccountsDBController.getAllUserAccounts();
        tcFName.setCellValueFactory(user -> user.getValue().getFirstNameProperty());
        tcLName.setCellValueFactory(user -> user.getValue().getLastNameProperty());
        tcEmail.setCellValueFactory(user -> user.getValue().getEmailProperty());
        tcPassword.setCellValueFactory(user -> user.getValue().getPasswordProperty());
        tcAccountLevel.setCellValueFactory(user -> user.getValue().getAccountLevelProperty());
        tvAccounts.setItems(accounts);
    }

    /*
    Function:       Opens the Edit Account form
    Parameters:     MouseEvent event - user clicking on the tvAccounts TableView
    Preconditions:  A user account has been clicked inside the tvAccounts TableView
    Postconditions: Edit Account form has been opened for the specific account
    */
    public void openEditAccountForm(MouseEvent event) {

        // ensures a row in the user accounts table has been double clicked
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && event.getTarget().getClass() != TableColumnHeader.class) {
            UserAccount account = tvAccounts.getSelectionModel().getSelectedItem();
            if (account != null) {

                // opens the edit account form for the selected account
                try {
                    FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/EditAccountForm.fxml")));
                    EditAccountFormController controller = new EditAccountFormController(account);
                    loader.setController(controller);
                    Parent parent = loader.load();

                    Stage stage = new Stage();
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Edit Account Form - " + account.getEmail());

                    // updates account list after the modification
                    stage.setOnCloseRequest((e) -> {
                        initialize();
                    });

                    stage.show();
                } catch (IOException e) {
                    createErrorMessage("Cannot find UIPagesAndForms/EditAccountForm.fxml");
                }
            }
        }
    }

    /*
    Function:       Opens the Edit Account form
    Parameters:     MouseEvent event - user clicking on the btSubmit Button
    Preconditions:  The Add Account button has been clicked
    Postconditions: The Add Account form has been opened
    */
    public void openAddAccountForm() {
        try {
            // load the Add Account Form
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/AddAccountForm.fxml")));
            Parent parent = loader.load();

            // open the Add Account Form
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Add Account Form");
            stage.initModality(Modality.APPLICATION_MODAL);

            // updates account list with the new account after form submission
            stage.setOnCloseRequest((e) -> {
                initialize();
            });

            stage.show();

        } catch (IOException e) {
            createErrorMessage("Cannot find UIPagesAndForms/AddAccountForm.fxml");
        }
    }


    /*
    Function:       Opens the User Information Form
    Parameters:     MouseEvent event - user clicking on the btProfile Button
    Preconditions:  The gear icon has been clicked
    Postconditions: The User Information form has been opened
    */
    @Override
    public void openUserAccountForm(ActionEvent event) {
        try {
            // grabs current user account
            Node node = (Node) event.getSource();
            Stage curr = (Stage) node.getScene().getWindow();
            InformationPackager packager = (InformationPackager) curr.getUserData();
            UserAccount account = packager.getAccount();

            // loads User Information Form
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/UserInformationForm.fxml")));
            UserInformationFormController controller = new UserInformationFormController(account);
            loader.setController(controller);
            Parent parent = loader.load();

            // opens form window
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("User Information Form - " + account.getEmail());

            // reloads the page on account edit
            stage.setOnCloseRequest(e -> {
                initialize();
            });

            stage.show();
        } catch (IOException e) {
            createErrorMessage("Cannot find UIPagesAndForms/UserInformationForm.fxml");
        }
    }
}
