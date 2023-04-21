import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/*
Purpose:          Controller class for Login Page
Collaboration:    Created by Main
Author:           Jacob Wolfe
 */

public class LogInController {

    @FXML
    private PasswordField tfPassword;

    @FXML
    private Label lbPassword;

    @FXML
    private Text txDepartment;

    @FXML
    private VBox vbEmail;

    @FXML
    private TextField tfEmail;

    @FXML
    private ImageView ivBackground;

    @FXML
    private Text txEARS;

    @FXML
    private Button btSubmit;

    @FXML
    private VBox vbSubtitle;

    @FXML
    private VBox vbPassword;

    @FXML
    private ImageView ivAlgomaU;

    @FXML
    private Label lbEmail;

    @FXML
    private VBox vbLogInForm;

    @FXML
    private Text txError;

    /*
    Function:       attempts to log the user in to the appropriate page for their account level
    Parameters:     ActionEvent event: the user clicking the login button
    Preconditions:  user has entered correct login information
    Postconditions: user has been redirected to either the Account Management page, Faculty Searches Dept Head page, or Faculty Searches Regular page
     */
    public void logIn(ActionEvent event) {

        UserAccount account = validateAccount();

        if (account != null) {
            // gets current stage
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            String fileName = "";
            String title = "";
            InformationPackager packager = new InformationPackager();
            packager.setAccount(account);
            UIPageController controller;

            // determines which page to open based on account type
            switch (account.getAccountLevel()) {
                case "department head":
                    fileName = "FacultySearchesDeptHeadPage.fxml";
                    title = "Faculty Searches";
                    controller = new FacultySearchesDeptHeadController(packager);
                    break;
                case "administrator":
                    fileName = "AccountManagementPage.fxml";
                    title = "Account Management";
                    controller = new AccountManagementController(packager);
                    break;
                case "regular":
                    fileName = "FacultySearchesRegularPage.fxml";
                    title = "Faculty Searches";
                    controller = new FacultySearchesRegularController(packager);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + account.getAccountLevel());
            }

            try {
                // packages userdata and passes to next stage
                stage.setUserData(packager);

                // creates stage and opens
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UIPagesAndForms/" + fileName));
                loader.setController(controller);
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle(title);
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /*
    Function:       confirms that the email exists in the database and if the entered password is correct
    Parameters:     none
    Preconditions:  user has attempted to login to the software
    Postconditions: user information has been retrieved from the database if the login credentials are correct
     */
    private UserAccount validateAccount() {

        String email = tfEmail.getText();
        String password = tfPassword.getText();
        UserAccount result = UserAccountsDBController.getUserAccount(email, password);

        if (!UserAccountsDBController.checkEmail(email)) {
            // email does not exist in database
            txError.setText("Incorrect email");
        } else if (result == null) {
            // password is incorrect for email
            txError.setText("Incorrect password");
        }
        return result;
    }
}

