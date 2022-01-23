package Controllers;

import Database.MongoDBController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {


    private MongoDBController mongo;
    private Stage primaryStage;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public void setMongo(MongoDBController mongo) {
        this.mongo = mongo;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }
    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void openLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Views/login_RTCA.fxml"));
        try {
            AnchorPane root = fxmlLoader.load();
            LoginController loginController = (LoginController) fxmlLoader.getController();
            loginController.setMongo(mongo);
            loginController.setPrimaryStage(primaryStage);
            loginController.setOis(ois);
            loginController.setOos(oos);
            loginController.setSocket(socket);
            Scene scene = new Scene(root ,1280, 720);
            scene.setRoot(root);
            primaryStage.setScene(scene);
        }catch (Exception ioexc){
            System.err.println("[RegisterController::openLogin]Exception> "+ioexc.getMessage());
        }

    }
}
