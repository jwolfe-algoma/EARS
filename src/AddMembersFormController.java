import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;

/*
Purpose:          Controller for the Add Members Form
Collaboration:    Created by SearchInfoDeptHeadController upon attempting to add committee members to the search
                  Calls methods from UserAccountsDBController and CommitteeAssignmentsDBController
Author:           Jacob Wolfe
 */

public class AddMembersFormController extends UIFormController {

    @FXML
    private TableColumn<UserAccount, String> tcFacultyName;

    @FXML
    private Button btRemoveMember;

    @FXML
    private TableView<UserAccount> tvFaculty;

    @FXML
    private TableColumn<UserAccount, String> tcCurrName;

    @FXML
    private TableView<UserAccount> tvCurrCommittee;

    @FXML
    private RadioButton rbChair;

    @FXML
    private Button btAddMember;

    @FXML
    private RadioButton rbMember;

    @FXML
    private ToggleGroup tgRanking;

    @FXML
    private TableColumn<UserAccount, String> tcRanking;

    @FXML
    private TableColumn<UserAccount, String> tcEmail;

    @FXML
    private Button btSubmit;

    private ObservableList<UserAccount> committee;
    private ObservableList<UserAccount> faculty;
    private Boolean hasChair = false;
    private ArrayList<Object[]> changeLog = new ArrayList<>();
    private FacultySearch search;

    /*
    Function:       Creates a new AddMembersFormController
    Parameters:     FacultySearch search - contains the info for the search to which the committee will be added
    Preconditions:  User has attempted to open the Add Members Form
     */
    public AddMembersFormController(FacultySearch search) {
        this.search = search;
    }

    /*
    Function:       Initializes the Add Members Form
    Parameters:     none
    Preconditions:  Add Members Form has been requested
    Postconditions: Add Members Form has been loaded
     */
    @FXML
    public void initialize() {

        // get current committee and the rest of the faculty
        committee = CommitteeAssignmentsDBController.getSearchCommitteeAccounts(search);
        faculty = CommitteeAssignmentsDBController.getNotSearchCommitteeAccounts(search);

        // check if the committee has a chair
        if (committee != null) {
            for (UserAccount account : committee) {
                if (account.getCommitteeLevel().equals("chair")) {
                    hasChair = true;
                }
            }
        }

        // add entries to committee and faculty table
        tcCurrName.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getFirstName() + " " + user.getValue().getLastName()));
        tcRanking.setCellValueFactory(user -> user.getValue().getCommitteeLevelProperty());
        tcEmail.setCellValueFactory(user -> user.getValue().getEmailProperty());
        tcFacultyName.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getFirstName() + " " + user.getValue().getLastName()));
        tvCurrCommittee.setItems(committee);
        tvFaculty.setItems(faculty);
    }

    /*
    Function:       Adds members to the committee
    Parameters:     none
    Preconditions:  Faculty has been selected and Add Member button has been clicked
    Postconditions: Member has been added to the committee
     */
    public void addCommitteeAssignmentEntry() {
        // gets selected faculty
        UserAccount user = tvFaculty.getSelectionModel().getSelectedItem();

        if (user != null) {
            if (hasChair && rbChair.isSelected()) {
                // stops user from adding a second chair
                createErrorMessage("A search committee can only have one chairperson");
            } else {
                // adds as a chair
                if (rbChair.isSelected()) {
                    user.setCommitteeLevel("chair");
                    hasChair = true;
                }
                // adds as a member
                else {
                    user.setCommitteeLevel("member");
                }
                tvFaculty.getItems().remove(user);
                tvCurrCommittee.getItems().add(user);
                changeLog.add(new Object[]{user, 1});
            }
        }
    }

    /*
    Function:       Removes members from the committee
    Parameters:     none
    Preconditions:  Committee member has been selected and Remove Member button has been clicked
    Postconditions: Member has been removed from the committee
     */
    public void deleteCommitteeAssignmentEntry() {
        // get selected user
        UserAccount user = tvCurrCommittee.getSelectionModel().getSelectedItem();

        if (user != null) {
            // updates the committee to have no chairperson
            if (user.getCommitteeLevel().equals("chair")) {
                hasChair = false;
            }
            // remove the user from the committee
            tvCurrCommittee.getItems().remove(user);
            tvFaculty.getItems().add(user);
            changeLog.add(new Object[]{user, 2});
        }
    }

    /*
    Function:       Submits changes to teh database
    Parameters:     none
    Preconditions:  Submit changes button has been clicked
    Postconditions: Changes are entered into the database
     */
    public void confirmChanges(ActionEvent event) {

        for (Object[] change : changeLog)
            if (change[1].equals(1)) {
                // add the member
                CommitteeAssignmentsDBController.createCommitteeAssignment((UserAccount) change[0], search);
            } else {
                // remove the member
                CommitteeAssignmentsDBController.deleteCommitteeAssignment((UserAccount) change[0], search);
            }

        // close the window
        cancel(event);
    }
}

