package Controllers;

import Database.MongoDBController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private MongoDBController mongo;
    private Stage primaryStage;

    public void setMongo(MongoDBController mongo) {
        this.mongo = mongo;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void openRegister(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Views/register_RTCA.fxml"));
        try {
            AnchorPane root = fxmlLoader.load();
            RegisterController loginController = (RegisterController) fxmlLoader.getController();
            loginController.setMongo(mongo);
            loginController.setPrimaryStage(primaryStage);
            Scene scene = new Scene(root ,1080, 720);
            scene.setRoot(root);
            primaryStage.setScene(scene);
        }catch (Exception ioexc){
            System.err.println("[LoginController::openRegister]Exception> "+ioexc.getMessage());
        }
    }

}
