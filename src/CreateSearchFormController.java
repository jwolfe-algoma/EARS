import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/*
Purpose:          Controller for the Create Search Form
Collaboration:    Created by FacultySearchesDeptHeadController
Author:           Jacob Wolfe
 */

public class CreateSearchFormController extends UIFormController {

    @FXML
    private Label lbDescription;

    @FXML
    private TextArea taDescription;

    @FXML
    private GridPane gpForm;

    @FXML
    private TextField tfPosition;

    @FXML
    private DatePicker dpStart;

    @FXML
    private Label lbPosition;

    @FXML
    private Button btSubmit;

    @FXML
    private Label lbStart;

    @FXML
    private Label lbRequirements;

    @FXML
    private Label lbEnd;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private Button btReturn;

    @FXML
    private TextArea taRequirements;

    @FXML
    private Pane pnMain;

    /*
    Function:       Adds a Faculty Search to the database
    Parameters:     none
    Preconditions:  search info has been entered and the submit button has been clicked
    Postconditions: search has been added to the database
     */
    public void addFacultySearchEntry(ActionEvent e) {
        // initialize search and error variables
        FacultySearch search = new FacultySearch();
        String errorMsg = "The following fields must be filled in: ";
        Boolean hasError = false;

        // checks if any fields have been left blank
        if (tfPosition.getText().equals("")) {
            errorMsg += "Position, ";
            hasError = true;
        }
        if (dpStart.getValue() == null) {
            errorMsg += "Start Date, ";
            hasError = true;
        }
        if (dpEnd.getValue() == null) {
            errorMsg += "End Date, ";
            hasError = true;
        }
        if (taDescription.getText().equals("")) {
            errorMsg += "Description, ";
            hasError = true;
        }
        if (taRequirements.getText().equals("")) {
            errorMsg += "Requirements, ";
            hasError = true;
        }

        if (hasError) {
            // create error popup
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
            createErrorMessage(errorMsg);
        } else {
            // wrap data info FacultySearch object, add to database, close form
            search.setPosition(tfPosition.getText());
            search.setStartDate(dpStart.getValue());
            search.setEndDate(dpEnd.getValue());
            search.setDescription(taDescription.getText());
            search.setRequirements(taRequirements.getText());
            FacultySearchDBController.createFacultySearch(search);
            cancel(e);
        }
    }

}
