import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
Purpose:          Controller class for the Error Message
Collaboration:    Created by any controller that may have a user input error
Author:           Jacob Wolfe
 */

public class ErrorMessageController {

    @FXML
    private Pane pnMain;

    @FXML
    private Text txErrorPopup;

    @FXML
    private Text txErrorTitle;

    private String message;

    /*
    Function:       Creates a new ErrorMessageController
    Parameters:     String message - String containing a description of the error
    Preconditions:  ErrorMessage has been requested
     */
    public ErrorMessageController(String message) {
        this.message = message;
    }

    /*
    Function:       Initializes the Error Message
    Parameters:     none
    Preconditions:  Error Message has been requested
    Postconditions: Error Message has been loaded
     */
    @FXML
    public void initialize() {
        // load message
        txErrorPopup.setText(message);
    }

    /*
    Function:       closes the error message
    Parameters:     ActionEvent event - the user clicking the OK button
    Preconditions:  ok button has been clicked
    Postconditions: Error Message had been closed
     */
    public void cancel(ActionEvent event) {
        // get this stage and close
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
