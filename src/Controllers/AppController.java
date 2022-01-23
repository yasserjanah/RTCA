package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private Stage primaryStage;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ObservableList<String> activeUsers;


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

    public void setActiveUsers(List<String> users) {
        this.activeUsers = FXCollections.observableArrayList(users);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
