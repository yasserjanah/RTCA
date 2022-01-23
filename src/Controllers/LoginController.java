package Controllers;

import Authentication.LoginRequest;
import Authentication.RegistrationRequest;
import Communication.Response;
import Communication.Update;
import Database.MongoDBController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Stage primaryStage;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    @FXML
    private TextField usernameId;

    @FXML
    private PasswordField passwordId;

    @FXML
    private Button loginButton;

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

    public void login(){
        loginButton.setDisable(true);
        usernameId.setDisable(true);
        passwordId.setDisable(true);
        String username = usernameId.getText().trim();
        String password = passwordId.getText();
        Alert alert = new Alert( Alert.AlertType.WARNING);
        alert.setTitle("Loggin-in");
        if( username.length()==0 || password.length()==0){
            alert.setHeaderText(" Please enter a username & a password ! ");
            alert.showAndWait();
            return;
        }
        LoginRequest lr = new LoginRequest( username, password);
        try {
            oos.writeObject(lr);
            oos.flush();
            Response res = (Response) ois.readObject();
            switch(res.getType()){
                case "logginErr":
                    alert.setAlertType( Alert.AlertType.ERROR);
                    alert.setHeaderText("Bad credentiels");
                    alert.setContentText( res.getContent() );
                    alert.show();
                    break;
                case "logged":
                    Update update = (Update) ois.readObject();
                    this.openAppView(update.getActiveusers());
                    break;
            }
        }catch (Exception iox) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Connection with the server is lost");
            alert.showAndWait();
        }
        loginButton.setDisable(false);
        usernameId.setDisable(false);
        passwordId.setDisable(false);
    }


    public void openAppView(List<String> activeusers){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Views/UI.fxml"));
        try {
            AnchorPane root = fxmlLoader.load();
            AppController controller = (AppController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setOis(ois);
            controller.setOos(oos);
            controller.setSocket(socket);
            controller.setActiveUsers(activeusers);
            Scene scene = new Scene(root ,1080, 720);
            scene.setRoot(root);
            primaryStage.setScene(scene);
        }catch (Exception ioexc){
            System.err.println("[LoginController::openAppView]Exception> "+ioexc.getMessage());
        }

    }


}
