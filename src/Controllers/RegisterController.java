package Controllers;

import Authentication.LoginRequest;
import Authentication.RegistrationRequest;
import Communication.Response;
import Database.MongoDBController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {


    private Stage primaryStage;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    @FXML
    private TextField usernameId;

    @FXML
    private PasswordField passwordId;

    @FXML
    private Button registerButton;


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
            loginController.setPrimaryStage(primaryStage);
            loginController.setOis(ois);
            loginController.setOos(oos);
            loginController.setSocket(socket);
            Scene scene = new Scene(root ,1080, 720);
            scene.setRoot(root);
            primaryStage.setScene(scene);
        }catch (Exception ioexc){
            System.err.println("[RegisterController::openLogin]Exception> "+ioexc.getMessage());
        }
    }

    public void register(){
        registerButton.setDisable(true);
        usernameId.setDisable(true);
        passwordId.setDisable(true);
        String username = usernameId.getText().trim();
        String password = passwordId.getText();
        Alert alert = new Alert( Alert.AlertType.WARNING);
        alert.setTitle("Registration");
        if( username.length()==0 || password.length()==0){
            alert.setHeaderText(" Please enter a username & a password ! ");
            alert.showAndWait();
            return;
        }
        RegistrationRequest rr = new RegistrationRequest( username, password);
        try {
            oos.writeObject(rr);
            oos.flush();
            Response res = (Response) ois.readObject();
            switch(res.getType()){
                case "userExists":
                    alert.setAlertType( Alert.AlertType.ERROR);
                    alert.setHeaderText("This username exists already");
                    alert.setContentText( res.getContent() );
                    alert.show();
                    break;
                case "userRegistred":
                    alert.setAlertType( Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Registration is successful !");
                    alert.setContentText( res.getContent() );
                    alert.show();
                    break;
            }
        }catch (Exception iox) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Connection with the server is lost");
            alert.showAndWait();
        }
        registerButton.setDisable(false);
        usernameId.setDisable(false);
        passwordId.setDisable(false);
    }

}
