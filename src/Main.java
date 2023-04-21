import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

/*
Purpose:          Main method to start the EARS system
Collaboration:    Creates LoginPage or ErrorMessage
Author:           Jacob Wolfe
 */

public class Main extends Application {

    /*
    Function:       Starts the EARS software
    Parameters:     Stage primaryStage: stage to be opened when the software is loaded
    Preconditions:  user has started the EARS software
    Postconditions: Login page has been opened
     */
    public void start(Stage primaryStage) throws Exception {
        Parent parent;
        Scene scene;

        try {
            // creates the EARSDB on the server specified in properties.txt and creates tables if they do not exist
            DBProperties.createEARSDB();

            // load login screen if database connection successfully established
            parent = FXMLLoader.load(getClass().getResource("UIPagesAndForms/LoginPage.fxml"));
            scene = new Scene(parent);
            primaryStage.setTitle("Employment Application Review System");
        } catch (SQLException ex) {
            // load error message prompting the user to enter correct information to properties.txt
            FXMLLoader loader = new FXMLLoader((getClass().getResource("UIPagesAndForms/ErrorMessage.fxml")));
            ErrorMessageController controller = new ErrorMessageController("Cannot connect to MySQL database - please recheck the parameters in properties.txt");
            loader.setController(controller);
            parent = loader.load();
            scene = new Scene(parent);
            primaryStage.setTitle("Error");
        }

        // open the application
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}