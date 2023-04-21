import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/*
Purpose:          Parent class for all UIForms
Collaboration:    Extended by all UIForms
Author:           Jacob Wolfe
 */

public abstract class UIFormController {

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd");

    /*
    Function:       closes a form
    Parameters:     ActionEvent e: user clicking a button
    Preconditions:  user attempts to close a form by clicking a cancel/delete/confirm button
    Postconditions: stage is closed and a close request is fired
     */
    public void cancel(ActionEvent e) {
        // get current stage
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        // close the stage and fire a close request
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /*
    Function:       creates an error message popup
    Parameters:     String message: Error message to be displayed in the popup
    Preconditions:  user has interacted with the software in a way that generates an error
    Postconditions: error message popup is created and locks the rest of the application until it is closed
     */
    public void createErrorMessage(String message) {

        try {
            // load the error message page
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/ErrorMessage.fxml")));
            ErrorMessageController controller = new ErrorMessageController(message);
            loader.setController(controller);
            Parent parent = loader.load();

            // open the error message page
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
