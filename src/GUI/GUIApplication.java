package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUIApplication extends Application {

    private static Stage primaryStageObj;


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStageObj = primaryStage;

        //creating the root object
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Views/login_RTCA.fxml"));
        Parent root = fxmlLoader.load();

        //Creating a Scene by passing the root object, height and width
        Scene scene = new Scene(root ,1080, 720);
        scene.setRoot(root);


        //Setting the title to Stage.
        primaryStage.setTitle("RTCA fx-client");

        //Adding the scene to Stage
        primaryStage.setScene(scene);

        //disable resizing
        primaryStage.setResizable(false);

        //Displaying the contents of the stage
        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }

}
