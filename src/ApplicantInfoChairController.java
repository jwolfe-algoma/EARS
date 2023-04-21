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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/*
Purpose:          Controller for the Applicant Info Chair Page
Collaboration:    Created by SearchInfoChairController
                  Calls methods in CommentsDBController, ApplicantAssignmentsDBController
Author:           Jacob Wolfe
 */

public class ApplicantInfoChairController extends UIPageController {

    @FXML
    private TextArea taComment;

    @FXML
    private ToggleGroup tgStatus;

    @FXML
    private RadioButton rbApproved;

    @FXML
    private TableColumn<Comment, String> tcComment;

    @FXML
    private RadioButton rbIncomplete;

    @FXML
    private Text txStatus;

    @FXML
    private TableView<Comment> tvComments;

    @FXML
    private RadioButton rbRejected;

    @FXML
    private RadioButton rbShortlisted;

    @FXML
    private Button btComment;

    @FXML
    private Label lbStatusUpdate;

    @FXML
    private Button btProfile;

    @FXML
    private Text txResume;

    @FXML
    private TableColumn<Comment, LocalDate> tcDate;

    @FXML
    private TableColumn<Comment, String> tcAuthor;

    @FXML
    private Label lbAllComments;

    @FXML
    private Button btReturn;

    @FXML
    private Button btStatus;

    @FXML
    private Text txName;

    @FXML
    private Label lbNewComment;

    @FXML
    private Button btLogOut;

    private FacultySearch search;
    private SearchApplicant applicant;

    /*
    Function:       Creates a new ApplicantInfoChairController
    Parameters:     InformationPackager packager - contains user's account information, search information, applicant information
    Preconditions:  ApplicantInfoChairPage has been requested
     */
    public ApplicantInfoChairController(InformationPackager packager) {
        this.account = packager.getAccount();
        this.search = packager.getSearch();
        this.applicant = packager.getApplicant();
    }

    /*
    Function:       Initializes the Applicant Info Chair Page
    Parameters:     none
    Preconditions:  Applicant Info Chair Page has been requested
    Postconditions: Applicant Info Chair Page has been loaded
     */
    @FXML
    public void initialize() {
        // add comments to comment table
        ObservableList<Comment> comments = CommentsDBController.getApplicantComments(applicant);
        tcAuthor.setCellValueFactory(comment -> new SimpleStringProperty(CommentsDBController.getAuthor(comment.getValue())));
        tcDate.setCellValueFactory(comment -> comment.getValue().getDateProperty());
        tcComment.setCellValueFactory(comment -> comment.getValue().getCommentProperty());
        tvComments.setItems(comments);

        // update applicant information
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
            // wraps data into a comment object and submits to comments DB
            Comment comment = new Comment(account.getUserID(), applicant.getApplicantID(), taComment.getText(), LocalDate.now());
            tvComments.getItems().add(comment);
            CommentsDBController.createNewComment(account, applicant, comment);
            taComment.setText("");
        }
    }

    /*
    Function:       Updates the status of the applicant in the database
    Parameters:     none
    Preconditions:  new status has been selected and update status button has been clicked
    Postconditions: status has been updated
     */
    public void updateStatus() {
        // checks which status is selected
        if (rbIncomplete.isSelected()) {
            applicant.setStatus("Review Incomplete");
        } else if (rbRejected.isSelected()) {
            applicant.setStatus("Rejected");
        } else if (rbShortlisted.isSelected()) {
            applicant.setStatus("Shortlisted");
        } else if (rbApproved.isSelected()) {
            applicant.setStatus("Approved for Hire");
        }
        // submits status to DB and updates on page
        ApplicantAssignmentsDBController.updateApplicantStatus(applicant, search);
        txStatus.setText(applicant.getStatus());
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

                // packs information about the search, applicant, account, and comment
                InformationPackager packager = new InformationPackager();
                packager.setAccount(account);
                packager.setSearch(search);
                packager.setApplicant(applicant);
                packager.setComment(comment);

                // opens the edit account form for the selected account only if the chair is the author
                if (comment.getUserID() == account.getUserID()) {
                    try {
                        // load the form
                        FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/EditCommentForm.fxml")));
                        EditCommentFormController controller = new EditCommentFormController(packager);
                        loader.setController(controller);
                        Parent parent = loader.load();

                        // open the form
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.setTitle("Edit Comment Form");
                        stage.initModality(Modality.APPLICATION_MODAL);

                        // refresh the page upon closure
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
    }

    /*
    Function:       Returns the user to the search info page
    Parameters:     ActionEvent event - the user clicking on the Return to Faculty Search Button
    Preconditions:  user has clicked on the Return to Faculty Search button
    Postconditions: Page has been redirected to the Faculty Searches page
     */
    public void returnToFacultySearch(ActionEvent event) {
        // gets current stage
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        // loads SearchInfo page
        FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/SearchInfoChairPage.fxml")));
        InformationPackager packager = new InformationPackager();
        packager.setAccount(account);
        packager.setSearch(search);
        SearchInfoChairController controller = new SearchInfoChairController(packager);

        // opens the search info page for the selected search
        try {
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Search Information Page - " + search.getPosition());
            stage.show();
        } catch (IOException e) {
            createErrorMessage("Cannot find UIPagesAndForms/SearchInfoChairPage.fxml");
        }
    }
}
