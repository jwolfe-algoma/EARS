import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/*
Purpose:          Controller for the Add Applicant Form
Collaboration:    Created by SearchInfoDeptHeadController upon attempting to add a new applicant to the search
                  Calls methods from SearchApplicantsDBController and ApplicantAssignmentsDBController when submitting a new applicant
Author:           Jacob Wolfe
 */

public class AddApplicantFormController extends UIFormController {

    @FXML
    private TextField tfLName;

    @FXML
    private TextField tfResume;

    @FXML
    private Button btReturn;

    @FXML
    private Button btSubmit;

    @FXML
    private TextField tfFName;

    @FXML
    private Text txSearch;

    private FacultySearch search;

    /*
    Function:       Creates a new AddApplicantFormController
    Parameters:     FacultySearch search - contains the info for the search to which the applicant is being added
    Preconditions:  User has attempted to open the Add Applicant Form
     */
    public AddApplicantFormController(FacultySearch search) {
        this.search = search;
    }

    /*
    Function:       Initializes the Add Applicant Form
    Parameters:     none
    Preconditions:  Add Applicant Form has been requested
    Postconditions: Add Applicant Form has been loaded
     */
    @FXML
    public void initialize() {
        // update the txSearch text to the position of the search
        txSearch.setText(search.getPosition());
    }

    /*
    Function:       Adds applicant to the SearchApplicants and ApplicantAssignments databases
    Parameters:     ActionEvent event - the user clicking on the btSubmit Button
    Preconditions:  Applicant information has been filled out and submit button has been clicked
    Postconditions: Applicant has been entered into the database
     */
    public void addApplicantEntry(ActionEvent event) {
        // initialize applicant and error variables
        SearchApplicant applicant = new SearchApplicant();
        String errorMsg = "The following fields must be filled in: ";
        Boolean hasError = false;

        // confirm that information has been entered in all fields and builds error message if not
        if (tfFName.getText().equals("")) {
            errorMsg += "First Name, ";
            hasError = true;
        }
        if (tfLName.getText().equals("")) {
            errorMsg += "Last Name, ";
            hasError = true;
        }
        if (tfResume.getText().equals("")) {
            errorMsg += "Link to Resume, ";
            hasError = true;
        }

        if (hasError) {
            // opens error popup
            errorMsg = errorMsg.substring(0, errorMsg.length() - 2);
            createErrorMessage(errorMsg);
        } else {
            // wrap information into SearchApplicant object, submit to databases, close the form
            applicant.setFirstName(tfFName.getText());
            applicant.setLastName(tfLName.getText());
            applicant.setLinkToResume(tfResume.getText());
            applicant.setStatus("Review Incomplete");
            applicant = SearchApplicantsDBController.createApplicant(applicant);
            ApplicantAssignmentsDBController.createApplicantAssignment(applicant, search);
            cancel(event);
        }
    }
}
