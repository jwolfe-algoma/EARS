import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/*
Purpose:          Controller class for the Edit Search Form
Collaboration:    Created by FacultySearchesRegularController
Author:           Jacob Wolfe
 */

public class EditSearchFormController extends UIFormController {

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
    private Text txPosition;

    @FXML
    private Button btDelete;

    @FXML
    private Text txEnd;

    @FXML
    private Button btSubmit;

    @FXML
    private Label lbStart;

    @FXML
    private Label lbRequirements;

    @FXML
    private Label lbCurrent;

    @FXML
    private Label lbNew;

    @FXML
    private Label lbEnd;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private Text txDescription;

    @FXML
    private Button btReturn;

    @FXML
    private Text txRequirements;

    @FXML
    private Text txStart;

    @FXML
    private TextArea taRequirements;

    private FacultySearch search;

    /*
    Function:       Creates a new EditSearchFormController
    Parameters:     InformationPackager packager - contains search information
    Preconditions:  EditSearchForm has been requested
     */
    public EditSearchFormController(FacultySearch search) {
        this.search = search;
    }

    /*
    Function:       Initializes the Edit Search Form
    Parameters:     none
    Preconditions:  Edit Search Form has been requested
    Postconditions: Edit Search Form has been loaded
     */
    @FXML
    public void initialize() {
        // load the search information
        txPosition.setText(search.getPosition());
        txDescription.setText(search.getDescription());
        txRequirements.setText(search.getRequirements());
        txStart.setText(search.getStartDate().format(formatter));
        txEnd.setText(search.getEndDate().format(formatter));
    }

    /*
    Function:       update a search with new information
    Parameters:     ActionEvent event: user clicking the submit button
    Preconditions:  search information has been entered and submit button has been clicked
    Postconditions: search has been updated in the database
     */
    public void updateSearchEntry(ActionEvent event) {

        // only update entries that are not blank
        if (!tfPosition.getText().equals(""))
            search.setPosition(tfPosition.getText());
        if (dpStart.getValue() != null)
            search.setStartDate(dpStart.getValue());
        if (dpEnd.getValue() != null)
            search.setEndDate(dpEnd.getValue());
        if (!taDescription.getText().equals(""))
            search.setDescription(taDescription.getText());
        if (!taRequirements.getText().equals(""))
            search.setRequirements(taRequirements.getText());

        //  submit changes and close form
        FacultySearchDBController.updateFacultySearch(search);
        cancel(event);
    }

    /*
    Function:       delete a search
    Parameters:     ActionEvent event: user clicking the delete button
    Preconditions:  delete search button has been clicked
    Postconditions: search has been removed from the database
     */
    public void deleteSearchEntry(ActionEvent event) {
        // delete search and close form
        FacultySearchDBController.deleteFacultySearch(search);
        cancel(event);
    }

}
