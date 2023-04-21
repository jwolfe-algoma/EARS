import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;

/*
Purpose:          Controller class for the FacultySearchDeptHeadPage
Collaboration:    Created by LoginController
Author:           Jacob Wolfe
 */

public class FacultySearchesDeptHeadController extends UIPageController {

    @FXML
    private TableColumn<FacultySearch, String> tcPosition;

    @FXML
    private Button btProfile;

    @FXML
    private TableColumn<FacultySearch, LocalDate> tcStart;

    @FXML
    private TableView<FacultySearch> tvSearches;

    @FXML
    private TableColumn<FacultySearch, LocalDate> tcEnd;

    @FXML
    private Button btSubmit;

    @FXML
    private Button btLogOut;

    /*
    Function:       Creates a new FacultySearchesDeptHeadController
    Parameters:     InformationPackager packager - not used but accepted for compatability when called by other classes
    Preconditions:  FacultySearchesDeptHeadPage has been requested
     */
    public FacultySearchesDeptHeadController(InformationPackager packager) {
    }

    /*
    Function:       Initializes the Faculty Searches Dept Head Page
    Parameters:     none
    Preconditions:  Faculty Searches Dept Head Page has been requested
    Postconditions: Faculty Searches Dept Head Page has been loaded
     */
    @FXML
    public void initialize() {
        // load a list of searches in the system
        ObservableList<FacultySearch> searches = FacultySearchDBController.getAllFacultySearches();
        tcPosition.setCellValueFactory(search -> search.getValue().getPositionProperty());
        tcStart.setCellValueFactory(search -> search.getValue().getStartDateProperty());
        tcEnd.setCellValueFactory(search -> search.getValue().getEndDateProperty());
        tvSearches.setItems(searches);
    }

    /*
    Function:       opens the add faculty search form
    Parameters:     ActionEvent event: user clicking the add faculty search button
    Preconditions:  add search button has been clicked
    Postconditions: add faculty search form has been opened
     */
    public void openAddFacultySearchForm(ActionEvent event) {
        try {
            // load faculty search form
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/CreateSearchForm.fxml")));
            Parent parent = loader.load();

            // open faculty search form
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Create Search Form");
            stage.initModality(Modality.APPLICATION_MODAL);

            // updates search list upon form closure
            stage.setOnCloseRequest((e) -> {
                initialize();
            });

            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Function:       opens a faculty search page
    Parameters:     MouseEvent event: user clicking on the specific faculty search
    Preconditions:  faculty search has been clicked
    Postconditions: faculty search page has been opened
     */
    public void openFacultySearchPage(MouseEvent event) {

        // ensures a row in the searches table has been double clicked
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && event.getTarget().getClass() != TableColumnHeader.class) {
            FacultySearch search = tvSearches.getSelectionModel().getSelectedItem();
            if (search != null) {

                // opens the search information page for the selected account
                try {
                    // get the current stage
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    InformationPackager packager = (InformationPackager) stage.getUserData();
                    packager.setSearch(search);

                    // load search page
                    FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/SearchInfoDeptHeadPage.fxml")));
                    SearchInfoDeptHeadController controller = new SearchInfoDeptHeadController(packager);
                    loader.setController(controller);
                    Parent parent = loader.load();

                    // open search page
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
