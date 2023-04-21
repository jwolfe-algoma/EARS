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
import javafx.stage.Stage;

import java.io.IOException;

/*
Purpose:          Controller class for SearchInfoRegularPage
Collaboration:    Created by ApplicantInfoRegularPage and FacultySearchesRegularPage
Author:           Jacob Wolfe
 */

public class SearchInfoRegularController extends UIPageController {

    @FXML
    private Label lbDescription;

    @FXML
    private Label lbApplicants;

    @FXML
    private TableView<SearchApplicant> tvApplicants;

    @FXML
    private TableColumn<SearchApplicant, Boolean> tcReview;

    @FXML
    private TableColumn<SearchApplicant, String> tcStatus;

    @FXML
    private Label lbPosition;

    @FXML
    private Text txPosition;

    @FXML
    private GridPane gpInfo;

    @FXML
    private TableColumn<SearchApplicant, String> tcApplicantName;

    @FXML
    private Text txEnd;

    @FXML
    private Label lbStart;

    @FXML
    private Label lbRequirements;

    @FXML
    private Button btProfile;

    @FXML
    private Label lbEnd;

    @FXML
    private Button btReturn;

    @FXML
    private Text txDescription;

    @FXML
    private Text txRequirements;

    @FXML
    private Button btLogOut;

    @FXML
    private Text txStart;

    private FacultySearch search;

    /*
    Function:       Creates a new SearchInfoRegularController
    Parameters:     InformationPackager packager - contains account and search information
    Preconditions:  SearchInfoRegular page has been requested
     */
    public SearchInfoRegularController(InformationPackager packager) {
        this.search = packager.getSearch();
        this.account = packager.getAccount();
    }

     /*
     Function:       Initializes the Search Info Regular Page
     Parameters:     none
     Preconditions:  Search Info Regular Page has been requested
     Postconditions: Search Info Regular Page has been loaded
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
    }

    /*
    Function:       redirects to the faculty searches page
    Parameters:     ActionEvent event: user clicking the return to faculty searches button
    Preconditions:  user has clicked the return to faculty searches button
    Postconditions: user returns to the faculty searches page
     */
    public void returnToFacultySearches(ActionEvent event) {

        InformationPackager packager = new InformationPackager();
        packager.setAccount(account);
        packager.setSearch(search);

        try {
            // clear search from userdata
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setUserData(packager);

            // return to faculty searches page
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/FacultySearchesRegularPage.fxml")));
            FacultySearchesRegularController controller = new FacultySearchesRegularController(packager);
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

    /*
    Function:       opens the applicant info page for the selected applicant
    Parameters:     MouseEvent event: user clicks on an applicant
    Preconditions:  user has double clicked on an applicant
    Postconditions: user is redirected to the applicant info page
     */
    public void openApplicantInfoRegularPage(MouseEvent event) {

        // ensures a row in the user accounts table has been double clicked
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && event.getTarget().getClass() != TableColumnHeader.class) {
            SearchApplicant applicant = tvApplicants.getSelectionModel().getSelectedItem();
            if (applicant != null) {

                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();

                InformationPackager packager = new InformationPackager();
                packager.setApplicant(applicant);
                packager.setSearch(search);
                packager.setAccount(account);

                // opens the edit account form for the selected account
                try {
                    FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/ApplicantInfoRegularPage.fxml")));
                    ApplicantInfoRegularController controller = new ApplicantInfoRegularController(packager);
                    loader.setController(controller);
                    Parent parent = loader.load();

                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.setTitle("Applicant Information Page - " + applicant.getFirstName() + " " + applicant.getLastName());
                    stage.show();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}
