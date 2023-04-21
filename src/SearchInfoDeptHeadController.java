import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/*
Purpose:          Controller for the Search Info Dept Head Page
Collaboration:    Created by FacultySearchesDeptHeadPage
Author:           Jacob Wolfe
 */

public class SearchInfoDeptHeadController extends UIPageController {

    @FXML
    private Label lbApplicants;

    @FXML
    private Label lbDescription;

    @FXML
    private TableColumn<SearchApplicant, String> tcStatus;

    @FXML
    private Text txPosition;

    @FXML
    private Label lbCommittee;

    @FXML
    private Label lbRequirements;

    @FXML
    private Button btAddApplicant;

    @FXML
    private TableView<UserAccount> tvCommittee;

    @FXML
    private Label lbEnd;

    @FXML
    private TableColumn<UserAccount, String> tcMemberName;

    @FXML
    private Text txDescription;

    @FXML
    private Button btReturn;

    @FXML
    private TableColumn<UserAccount, String> tcRanking;

    @FXML
    private Text txRequirements;

    @FXML
    private Text txStart;

    @FXML
    private Button btLogOut;

    @FXML
    private TableView<SearchApplicant> tvApplicants;

    @FXML
    private TableColumn<SearchApplicant, Boolean> tcReview;

    @FXML
    private Label lbPosition;

    @FXML
    private GridPane gpInfo;

    @FXML
    private TableColumn<SearchApplicant, String> tcApplicantName;

    @FXML
    private Button btAddMember;

    @FXML
    private Text txEnd;

    @FXML
    private Label lbStart;

    @FXML
    private Button btEditSearch;

    @FXML
    private Button btProfile;

    private FacultySearch search;

    /*
    Function:       Creates a new SearchInfoDeptHeadController
    Parameters:     InformationPackager packager - contains account and search information
    Preconditions:  SearchInfoDeptHead page has been requested
     */
    public SearchInfoDeptHeadController(InformationPackager packager) {
        this.account = packager.getAccount();
        this.search = packager.getSearch();
    }

    /*
    Function:       Initializes the Search Info Dept Head Page
    Parameters:     none
    Preconditions:  Search Info Dept Head Page has been requested
    Postconditions: Search Info Dept Head Page has been loaded
     */
    @FXML
    public void initialize() {
        // update info text
        txPosition.setText(search.getPosition());
        txDescription.setText(search.getDescription());
        txRequirements.setText(search.getRequirements());
        txStart.setText(search.getStartDate().format(formatter));
        txEnd.setText(search.getEndDate().format(formatter));

        // update applicant table
        ObservableList<SearchApplicant> applicants = ApplicantAssignmentsDBController.applicantsLinkedToSearch(search);
        tcApplicantName.setCellValueFactory(applicant -> new SimpleStringProperty(applicant.getValue().getFirstName() + " " + applicant.getValue().getLastName()));
        tcReview.setCellValueFactory(applicant -> new SimpleBooleanProperty(!applicant.getValue().getStatus().equals("Review Incomplete")));
        tcStatus.setCellValueFactory(applicant -> applicant.getValue().getStatusProperty());
        tvApplicants.setItems(applicants);

        // update committee table
        ObservableList<UserAccount> committee = CommitteeAssignmentsDBController.getSearchCommitteeAccounts(search);
        tcMemberName.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getFirstName() + " " + user.getValue().getLastName()));
        tcRanking.setCellValueFactory(user -> user.getValue().getCommitteeLevelProperty());
        tvCommittee.setItems(committee);
    }

    /*
    Function:       redirects to the faculty searches page
    Parameters:     ActionEvent event: user clicking the return to faculty searches button
    Preconditions:  user has clicked the return to faculty searches button
    Postconditions: user returns to the faculty searches page
     */
    public void returnToFacultySearches(ActionEvent event) {

        try {
            // clear search from userdata
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            InformationPackager packager = (InformationPackager) stage.getUserData();
            packager.setSearch(null);

            // return to faculty searches page
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/FacultySearchesDeptHeadPage.fxml")));
            loader.setController(new FacultySearchesDeptHeadController(packager));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Faculty Searches");
            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       opens the edit search information form
    Parameters:     none
    Preconditions:  user has clicked the edit search button
    Postconditions: user has opened the edit search information form
     */
    public void openEditSearchInformationForm() {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/EditSearchForm.fxml")));
            EditSearchFormController controller = new EditSearchFormController(search);
            loader.setController(controller);
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Search Form - " + search.getPosition());
            stage.initModality(Modality.APPLICATION_MODAL);

            // updates account list with the new account
            stage.setOnCloseRequest((e) -> {
                initialize();
                checkSearchExists();
            });

            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       opens the add applicant form
    Parameters:     none
    Preconditions:  user has clicked the add applicant button
    Postconditions: user has opened the add applicant form
     */
    public void openAddApplicantForm() {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/AddApplicantForm.fxml")));
            AddApplicantFormController controller = new AddApplicantFormController(search);
            loader.setController(controller);
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Applicant Form - " + search.getPosition());
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnCloseRequest((e) -> {
                initialize();
            });

            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       opens the add committee members form
    Parameters:     none
    Preconditions:  user has clicked the edit committee button
    Postconditions: user has opened the add committee members form
     */
    public void openAddCommitteeMembersForm() {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/AddMembersForm.fxml")));
            AddMembersFormController controller = new AddMembersFormController(search);
            loader.setController(controller);
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Members Form - " + search.getPosition());
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnCloseRequest((e) -> {
                initialize();
            });

            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       opens the edit applicant form
    Parameters:     MouseEvent event: user clicking on an applicant
    Preconditions:  user has double clicked on an applicant
    Postconditions: user has opened edit applicant form for that applicant
     */
    public void openEditApplicantForm(MouseEvent event) {
        // ensures a row in the applicant table has been double clicked
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && event.getTarget().getClass() != TableColumnHeader.class) {
            SearchApplicant applicant = tvApplicants.getSelectionModel().getSelectedItem();
            if (applicant != null) {

                InformationPackager packager = new InformationPackager();
                packager.setSearch(search);
                packager.setApplicant(applicant);

                // opens the edit applicant form for the selected applicant
                try {
                    FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/EditApplicantForm.fxml")));
                    EditApplicantFormController controller = new EditApplicantFormController(packager);
                    loader.setController(controller);
                    Parent parent = loader.load();

                    Stage stage = new Stage();
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.setTitle("Edit Applicant Form - " + applicant.getFirstName() + " " + applicant.getLastName());
                    stage.initModality(Modality.APPLICATION_MODAL);

                    stage.setOnCloseRequest(e -> {
                        initialize();
                    });

                    stage.show();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /*
    Function:       confirms a search has not been deleted in the edit search form
    Parameters:     none
    Preconditions:  user has closed the edit search form
    Postconditions: nothing if the search exists, otherwise the user is redirected to the faculty searches page
     */
    private void checkSearchExists() {
        if (!FacultySearchDBController.getSearchExists(search)) {

            Stage stage = (Stage) txStart.getScene().getWindow();

            InformationPackager packager = new InformationPackager();
            packager.setAccount(account);
            FacultySearchesDeptHeadController controller = new FacultySearchesDeptHeadController(packager);

            try {
                // packages userdata and passes to next stage
                stage.setUserData(packager);

                // creates stage
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UIPagesAndForms/FacultySearchesDeptHeadPage.fxml"));
                loader.setController(controller);
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Faculty Searches");
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
