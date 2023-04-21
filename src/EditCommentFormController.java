import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/*
Purpose:          Controller class for the Edit Comment Form
Collaboration:    Created by ApplicantInfoRegularController and ApplicantInfoChairController
Author:           Jacob Wolfe
 */

public class EditCommentFormController extends UIFormController {

    @FXML
    private TextArea taComment;

    @FXML
    private GridPane gpForm;

    @FXML
    private Label lbCurrent;

    @FXML
    private Label lbNew;

    @FXML
    private Pane pnMain;

    @FXML
    private Text txComment;

    @FXML
    private Label lbComment;

    @FXML
    private Button btReturn;

    @FXML
    private Button btDelete;

    @FXML
    private Button btSubmit;

    private Comment comment;

    /*
    Function:       Creates a new EditCommentFormController
    Parameters:     InformationPackager packager - contains comment information
    Preconditions:  EditCommentForm has been requested
     */
    public EditCommentFormController(InformationPackager packager) {
        this.comment = packager.getComment();
    }

    /*
    Function:       Initializes the Edit Comment Form
    Parameters:     none
    Preconditions:  Edit Comment Form has been requested
    Postconditions: Edit Comment Form has been loaded
     */
    @FXML
    public void initialize() {
        // load the comment text
        txComment.setText(comment.getComment());
    }

    /*
    Function:       delete a comment
    Parameters:     ActionEvent event: user clicking the delete button
    Preconditions:  delete comment button has been clicked
    Postconditions: comment has been removed from the database
     */
    public void deleteComment(ActionEvent event) {
        // delete comment and close form
        CommentsDBController.deleteComment(comment);
        cancel(event);
    }

    /*
    Function:       update a comment with new information
    Parameters:     ActionEvent event: user clicking the submit button
    Preconditions:  comment information has been entered and submit button has been clicked
    Postconditions: comment has been updated in the database
     */
    public void updateComment(ActionEvent event) {
        // confirms text has been entered
        if (!taComment.getText().equals("")) {
            comment.setComment(taComment.getText());
        }
        // submit changes and close form
        CommentsDBController.updateComment(comment);
        cancel(event);
    }

}
