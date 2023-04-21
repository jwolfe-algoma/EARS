import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/*
Purpose:          Parent class for all UIPages
Collaboration:    Extended by all UIPages
Author:           Jacob Wolfe
 */

public abstract class UIPageController {

    protected UserAccount account;

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd");

    /*
    Function:       logs the user out and returns to the login screen
    Parameters:     ActionEvent event: The user clicks on the logout icon
    Preconditions:  user has clicked the logout icon on a page
    Postconditions: login form is opened and user is logged out
     */
    public void logOut(ActionEvent event) {
        // gets current stage
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        // return to login page
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("UIPagesAndForms/LoginPage.fxml"));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       opens the user information form
    Parameters:     ActionEvent event: The user clicks on the gear icon
    Preconditions:  user has clicked the gear icon on a page
    Postconditions: user information form is opened
     */
    public void openUserAccountForm(ActionEvent event) {
        try {
            // grabs current user account
            Node node = (Node) event.getSource();
            Stage curr = (Stage) node.getScene().getWindow();
            InformationPackager packager = (InformationPackager) curr.getUserData();
            UserAccount account = packager.getAccount();

            // loads form
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/UserInformationForm.fxml")));
            UserInformationFormController controller = new UserInformationFormController(account);
            loader.setController(controller);
            Parent parent = loader.load();

            // opens form window
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("User Information Form - " + account.getEmail());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       creates an error message popup
    Parameters:     String message: Error message to be displayed in the popup
    Preconditions:  user has interacted with the software in a way that generates an error
    Postconditions: error message popup is created and locks the rest of the application until it is closed
     */
    public void createErrorMessage(String message) {

        try {
            // load error message page
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/ErrorMessage.fxml")));
            ErrorMessageController controller = new ErrorMessageController(message);
            loader.setController(controller);
            Parent parent = loader.load();

            // open error message
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Error");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
