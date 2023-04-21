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
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/*
Purpose:          Controller for the Applicant Info Regular Page
Collaboration:    Created by SearchInfoRegularController
                  Calls methods in CommentsDBController
Author:           Jacob Wolfe
 */

public class ApplicantInfoRegularController extends UIPageController {

    @FXML
    private TextArea taComment;

    @FXML
    private TableColumn<Comment, String> tcComment;

    @FXML
    private GridPane gpInfo;

    @FXML
    private Label lbComments;

    @FXML
    private Text txStatus;

    @FXML
    private TableView<Comment> tvComments;

    @FXML
    private Label lbName;

    @FXML
    private Button btComment;

    @FXML
    private Button btProfile;

    @FXML
    private Label lbResume;

    @FXML
    private Text txResume;

    @FXML
    private TableColumn<Comment, LocalDate> tcDate;

    @FXML
    private Button btReturn;

    @FXML
    private Label lbStatus;

    @FXML
    private Text txName;

    @FXML
    private Label lbNewComment;

    @FXML
    private Button btLogOut;

    private FacultySearch search;
    private SearchApplicant applicant;

    /*
    Function:       Creates a new ApplicantInfoRegularController
    Parameters:     InformationPackager packager - contains user's account information, search information, applicant information
    Preconditions:  ApplicantInfoRegularPage has been requested
     */
    public ApplicantInfoRegularController(InformationPackager packager) {
        // gets information from packager
        this.account = packager.getAccount();
        this.applicant = packager.getApplicant();
        this.search = packager.getSearch();
    }

    /*
    Function:       Initializes the Applicant Info Regular Page
    Parameters:     none
    Preconditions:  Applicant Info Chair Regular page has been requested
    Postconditions: Applicant Info Chair Regular page has been loaded
     */
    @FXML
    public void initialize() {
        ObservableList<Comment> comments = CommentsDBController.getMemberComments(account, applicant);
        tcComment.setCellValueFactory(comment -> comment.getValue().getCommentProperty());
        tcDate.setCellValueFactory(comment -> comment.getValue().getDateProperty());
        tvComments.setItems(comments);

        txName.setText(applicant.getFirstName() + " " + applicant.getLastName());
        txResume.setText(applicant.getLinkToResume());
        txStatus.setText(applicant.getStatus());
    }

    /*
    Function:       Adds a comment to the applicant
    Parameters:     none
    Preconditions:  comment has been typed and submit button has been clicked
    Postconditions: Comment has been added to the database
     */
    public void addNewComment() {
        if (!taComment.getText().equals("")) {
            // wrap comment details into a Comment object and submit to database
            Comment comment = new Comment(account.getUserID(), applicant.getApplicantID(), taComment.getText(), LocalDate.now());
            tvComments.getItems().add(comment);
            CommentsDBController.createNewComment(account, applicant, comment);
            taComment.setText("");
        }
    }

    /*
    Function:       Opens the Edit Comment form
    Parameters:     MouseEvent event - the user clicking on the comment
    Preconditions:  comment has been double clicked
    Postconditions: edit comment form has been opened
     */
    public void openEditCommentForm(MouseEvent event) {

        // ensures a row in the comment table has been double clicked
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && event.getTarget().getClass() != TableColumnHeader.class) {
            Comment comment = tvComments.getSelectionModel().getSelectedItem();
            if (comment != null) {

                // packages the account, search, applicant, and comment information
                InformationPackager packager = new InformationPackager();
                packager.setAccount(account);
                packager.setSearch(search);
                packager.setApplicant(applicant);
                packager.setComment(comment);

                try {
                    // loads the edit comment form
                    FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/EditCommentForm.fxml")));
                    EditCommentFormController controller = new EditCommentFormController(packager);
                    loader.setController(controller);
                    Parent parent = loader.load();

                    // opens the edit comment form
                    Stage stage = new Stage();
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.setTitle("Edit Comment Form");
                    stage.initModality(Modality.APPLICATION_MODAL);

                    // refreshed the page on submission
                    stage.setOnCloseRequest(e -> {
                        initialize();
                    });

                    stage.show();
                } catch (IOException e) {
                    createErrorMessage("Cannot find UIPagesAndForms/EditCommentForm.fxml");
                }
            }
        }
    }

    /*
    Function:       Returns the user to the search info page
    Parameters:     ActionEvent event - the user clicking on the Return to Faculty Search Button
    Preconditions:  user has clicked on the Return to Faculty Search button
    Postconditions: Page has been redirected to the Faculty Searches page
     */
    public void returnToFacultySearch(ActionEvent event) {

        // get current stage
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        // load search info page
        FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/SearchInfoRegularPage.fxml")));
        InformationPackager packager = new InformationPackager();
        packager.setAccount(account);
        packager.setSearch(search);
        SearchInfoRegularController controller = new SearchInfoRegularController(packager);

        // opens the search info page for the selected search
        try {
            loader.setController(controller);
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Search Information Page - " + search.getPosition());
            stage.show();
        } catch (IOException e) {
            createErrorMessage("Cannot find UIPagesAndForms/SearchInfoRegularPage.fxml");
        }
    }
}
