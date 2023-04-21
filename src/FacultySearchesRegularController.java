import javafx.collections.ObservableList;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/*
Purpose:          Controller class for the FacultySearchesRegularPage
Collaboration:    Created by LoginController
Author:           Jacob Wolfe
 */

public class FacultySearchesRegularController extends UIPageController{

    @FXML
    private TableView<FacultySearch> tvSearches2;

    @FXML
    private TableColumn<FacultySearch, String> tcPosition2;

    @FXML
    private TableColumn<FacultySearch, LocalDate> tcEnd2;

    @FXML
    private TableView<FacultySearch> tvSearches1;

    @FXML
    private TableColumn<FacultySearch, String> tcPosition1;

    @FXML
    private TableColumn<FacultySearch, LocalDate> tcEnd1;

    @FXML
    private Button btProfile;

    @FXML
    private Label lbAvailSearches;

    @FXML
    private TableColumn<FacultySearch, LocalDate> tcStart2;

    @FXML
    private TableColumn<FacultySearch, LocalDate> tcStart1;

    @FXML
    private Label lbYourSearches;

    @FXML
    private Button btLogOut;


    /*
    Function:       Creates a new FacultySearchesRegularController
    Parameters:     InformationPackager packager - contains user account information
    Preconditions:  FacultySearchesRegularPage has been requested
     */
    public FacultySearchesRegularController(InformationPackager packager) {
        this.account = packager.getAccount();
    }

    /*
    Function:       Initializes the Faculty Searches Regular Page
    Parameters:     none
    Preconditions:  Faculty Searches Regular Page has been requested
    Postconditions: Faculty Searches Regular Page has been loaded
     */
    @FXML
    public void initialize() {
        // load users searches
        ObservableList<FacultySearch> userSearches = FacultySearchDBController.getUsersSearches(account);
        tcPosition1.setCellValueFactory(search -> search.getValue().getPositionProperty());
        tcStart1.setCellValueFactory(search -> search.getValue().getStartDateProperty());
        tcEnd1.setCellValueFactory(search -> search.getValue().getEndDateProperty());
        tvSearches1.setItems(userSearches);

        // load searches not associated with user
        ObservableList<FacultySearch> otherSearches = FacultySearchDBController.getNotUsersSearches(account);
        tcPosition2.setCellValueFactory(search -> search.getValue().getPositionProperty());
        tcStart2.setCellValueFactory(search -> search.getValue().getStartDateProperty());
        tcEnd2.setCellValueFactory(search -> search.getValue().getEndDateProperty());
        tvSearches2.setItems(otherSearches);
    }

    /*
    Function:       opens a faculty search page
    Parameters:     MouseEvent event: user clicking on the specific faculty search
    Preconditions:  faculty search has been clicked
    Postconditions: faculty search page has been opened
     */
    public void openFacultySearchPage(MouseEvent event) {

        // ensures a row in the your searches table has been double clicked
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && event.getTarget().getClass() != TableColumnHeader.class) {
            FacultySearch search = tvSearches1.getSelectionModel().getSelectedItem();
            if (search != null) {
                // get current stage
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();

                // package information
                FXMLLoader loader;
                UIPageController controller;
                InformationPackager packager = new InformationPackager();
                packager.setAccount(account);
                packager.setSearch(search);

                // opens the search info page for the selected search
                try {
                    if (CommitteeAssignmentsDBController.isChair(account, search)) {
                        loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/SearchInfoChairPage.fxml")));
                        controller = new SearchInfoChairController(packager);
                    } else {
                        loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/SearchInfoRegularPage.fxml")));
                        controller = new SearchInfoRegularController(packager);
                    }

                    loader.setController(controller);
                    Parent parent = loader.load();

                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.setTitle("Search Information Page - " + search.getPosition());
                    stage.show();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}
