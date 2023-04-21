import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/*
Purpose:          Controller class for the Edit Applicant Form
Collaboration:    Created by SearchInfoDeptHeadController
Author:           Jacob Wolfe
 */

public class EditApplicantFormController extends UIFormController {

    @FXML
    private Label lbFName;

    @FXML
    private GridPane gpForm;

    @FXML
    private TextField tfResume;

    @FXML
    private Button btDelete;

    @FXML
    private Button btSubmit;

    @FXML
    private Label lbLName;

    @FXML
    private TextField tfLName;

    @FXML
    private Label lbCurrent;

    @FXML
    private Label lbNew;

    @FXML
    private Label lbResume;

    @FXML
    private Text txResume;

    @FXML
    private Text txFName;

    @FXML
    private Button btReturn;

    @FXML
    private Text txLName;

    @FXML
    private TextField tfFName;

    private SearchApplicant applicant;
    private FacultySearch search;

    /*
    Function:       Creates a new EditApplicantFormController
    Parameters:     InformationPackager packager - contains applicant and search information
    Preconditions:  EditApplicantForm has been requested
     */
    public EditApplicantFormController(InformationPackager packager) {
        this.applicant = packager.getApplicant();
        this.search = packager.getSearch();
    }

    /*
    Function:       Initializes the Edit Applicant Form
    Parameters:     none
    Preconditions:  Edit Applicant Form has been requested
    Postconditions: Edit Applicant Form has been loaded
     */
    @FXML
    public void initialize() {
        // load applicant information
        txFName.setText(applicant.getFirstName());
        txLName.setText(applicant.getLastName());
        txResume.setText(applicant.getLinkToResume());
    }

    /*
    Function:       update a search applicant with new information
    Parameters:     ActionEvent event: user clicking the submit button
    Preconditions:  applicant information has been entered and submit button has been clicked
    Postconditions: applicant has been updated in the database
     */
    public void updateApplicantEntry(ActionEvent event) {
        // only updates fields that are not blank
        if (!tfFName.getText().equals(""))
            applicant.setFirstName(tfFName.getText());
        if (!tfLName.getText().equals(""))
            applicant.setLastName(tfLName.getText());
        if (!tfResume.getText().equals(""))
            applicant.setLinkToResume(tfResume.getText());

        // submit changes to database
        SearchApplicantsDBController.updateApplicant(applicant);
        cancel(event);
    }

    /*
    Function:       delete a search applicant
    Parameters:     ActionEvent event: user clicking the delete button
    Preconditions:  delete applicant button has been clicked
    Postconditions: applicant has been removed from the database
     */
    public void deleteApplicantEntry(ActionEvent event) {
        // delete applicant from databsae
        SearchApplicantsDBController.deleteApplicant(applicant);
        cancel(event);
    }
}
