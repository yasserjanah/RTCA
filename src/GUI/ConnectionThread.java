package GUI;

import Authentication.LoginRequest;
import Authentication.RegistrationRequest;
import Communication.Message;
import Communication.Response;
import Database.MongoDBController;
import Exceptions.LoginErrorException;
import Exceptions.UserExistsException;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConnectionThread extends Thread {

    private Socket socket;
    private final MongoDBController mongoDBController;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String username=null;
    private String ipUser;

    ConnectionThread( Socket socket, MongoDBController mongoDBController) throws IOException {
        this.socket = socket;
        this.mongoDBController = mongoDBController;
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        ois = new ObjectInputStream(is);
    }

    @Override
    public void run() {
        ipUser = socket.getRemoteSocketAddress().toString();
        RegistrationRequest rr ;
        LoginRequest loginRequestr;
        Object responseObject;
        Response res;
        Message message;

        while( socket.isConnected() ){

                try {
                    responseObject = ois.readObject();

                    if( username==null ) {

                        if (responseObject instanceof RegistrationRequest) {

                            rr = (RegistrationRequest) responseObject;
                            try {
                                synchronized (this.mongoDBController) {
                                    this.mongoDBController.insertionOfUser(rr.getUsername(), rr.getPassword());
                                }
                            } catch (UserExistsException uexc) {
                                res = new Response("userExists", "user with username " + rr.getUsername() + " exists already ");
                                oos.writeObject(res);
                                oos.flush();
                                continue;
                            }
                            res = new Response("userRegistred", "user with username " + rr.getUsername() + " is successfully registered ! ");
                            oos.writeObject(res);
                            oos.flush();
                            continue;
                        } else {
                            loginRequestr = (LoginRequest) responseObject;
                            try {
                                boolean check = this.mongoDBController.passwordIsValid(loginRequestr.getUsername(), loginRequestr.getPassword());
                                if (check) {
                                    username = loginRequestr.getUsername();
                                    System.out.println("[userLogged] -> New user has connected : "+ username + "  ==> IP : "+ipUser);
                                    res = new Response("logged", username + " successfully logged in");
                                    oos.writeObject(res);
                                    oos.flush();
                                    continue;
                                } else {
                                    System.out.println("[i] invalid logging attempt >  " + loginRequestr.getUsername());
                                    res = new Response("logginErr", "username OR password Incorrect !");
                                    oos.writeObject(res);
                                    oos.flush();
                                    continue;
                            }
                        } catch (LoginErrorException uexc) {
                            res = new Response("logginErr", "username OR password Incorrect !");
                            oos.writeObject(res);
                            oos.flush();
                        }
                    }
                }else{
                        if(responseObject instanceof Message){
                            message = (Message) responseObject;
                            int destIndex =-1;
                            List<ConnectionThread> connectionThreadList = Server.getConnectionThreadList();
                            if( message.isBroadCast() ){
                                Server.sendBroadcastMsg( message );
                                continue;
                            }
                            for (int i = 0; i < connectionThreadList.size(); i++) {
                                if (connectionThreadList.get(i).getUsername() == message.getDestination()) {
                                    destIndex = i;
                                    break;
                                }
                            }
                            if( destIndex!=-1) {
                                Server.sendOneToOneMsg(message, destIndex);

                            }else {
                                res = new Response("userDisconnected", "user "+message.getDestination()+" is not active currently");
                                oos.writeObject(res);
                            }
                        }

                }
            } catch (Exception e) {
                System.err.print("\n[Exception]> exception reading Object : " + e.getMessage());
                break;// if it cannot read an object, this means that the connection is closed !
            }
        }
        closeConnection();
    }

    public void receiveMsg( Message message ){
        try {
            oos.writeObject(message);
            oos.flush();
            System.out.printf("[ReceivedMsg]> "+message);
        }catch (Exception exc){
            System.err.println("[Thread::receiveMsg] Exception > "+exc.getMessage());
        }
    }

    public void closeConnection(){
        try {
            Response res = new Response("closeConnection", "Closing connection ...");
            oos.writeObject(res);
            oos.flush();
            socket.close();
            System.out.println("[i] Connection closed : "+ username +" ==> IP : "+ipUser);
        }catch (IOException ioexc){
            System.err.print("\n[IOException4]> Cannot close connection!");
        }
        Server.romoveConnectionThread(this); // to remove Console.ConnectionThread from the list
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionThread)) return false;
        ConnectionThread that = (ConnectionThread) o;
        return Objects.equals(username, that.username) && Objects.equals(ipUser, that.ipUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, ipUser);
    }


    public String getUsername() {
        return username;
    }

    public String getIpUser() {
        return ipUser;
    }
}
