package GUI;

import Controllers.LoginController;
import Database.MongoDBController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GUIApplication extends Application {

    private static Stage primaryStageObj;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public GUIApplication(){
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        try{
            socket = new Socket("localhost",65500);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ois = new ObjectInputStream(is);
            oos = new ObjectOutputStream(os);

        }catch (Exception exc){
            Alert socketAlert = new Alert(Alert.AlertType.ERROR);
            socketAlert.setContentText("Cannot connect so server !");
            socketAlert.setHeaderText("Connection to server failed !");
            socketAlert.showAndWait();
            primaryStage.close();
            return;
        }

        primaryStageObj = primaryStage;
        //creating the root object
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Views/login_RTCA.fxml"));
        AnchorPane root = fxmlLoader.load();


        LoginController loginController = (LoginController) fxmlLoader.getController();
        loginController.setPrimaryStage(primaryStage);
        loginController.setSocket(socket);
        loginController.setOis(ois);
        loginController.setOos(oos);

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
