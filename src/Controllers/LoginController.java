package Controllers;

import Database.MongoDBController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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

    public void openRegister(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Views/register_RTCA.fxml"));
        try {
            AnchorPane root = fxmlLoader.load();
            RegisterController registerController = (RegisterController) fxmlLoader.getController();
            registerController.setMongo(mongo);
            registerController.setPrimaryStage(primaryStage);
            registerController.setOis(ois);
            registerController.setOos(oos);
            registerController.setSocket(socket);
            Scene scene = new Scene(root ,1080, 720);
            scene.setRoot(root);
            primaryStage.setScene(scene);
        }catch (Exception ioexc){
            System.err.println("[LoginController::openRegister]Exception> "+ioexc.getMessage());
        }
    }

}
