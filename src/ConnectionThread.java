import Authentication.LoginRequest;
import Authentication.RegistrationRequest;
import Database.MongoDBController;
import Exceptions.LoginErrorException;
import Exceptions.UserExistsException;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ConnectionThread extends Thread {

    private Socket socket;
    private final MongoDBController mongoDBController;
    private PrintWriter pw;
    private BufferedReader br;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String username;
    private String ipUser;

    ConnectionThread( Socket socket, MongoDBController mongoDBController) throws IOException {
        this.socket = socket;
        this.mongoDBController = mongoDBController;
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        ois = new ObjectInputStream(is);
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        pw = new PrintWriter( os, true);
        username = "{UNSET}";
    }

    @Override
    public void run() {
        int i;
        ipUser=socket.getRemoteSocketAddress().toString();

        pw.println("auth|#n# [Server]> authentication ");
        pw.flush();
        try {
            Object responseObject = ois.readObject();
            if( responseObject instanceof RegistrationRequest ){

                RegistrationRequest rr = (RegistrationRequest) responseObject;
                try{
                    synchronized (this.mongoDBController){
                        this.mongoDBController.insertionOfUser( rr.getUsername(), rr.getPassword() );
                    }
                }catch(UserExistsException uexc){
                    pw.println("write|#n##n# [Server]> username already taken, choose another one ! ");
                    pw.println("auth|#n# [Server]> authentication ");
                    pw.flush();
                }
                pw.println("write|#n##n# [Server]> user ("+rr.getUsername()+") is successfully registred, please sign-in ! #n#");
                pw.flush();
                pw.println("auth|#n# [Server]> authentication ");
                pw.flush();
            }else{
                // pw.println("auth|#n# [Server]> authentication ");
                // pw.flush();
                // Login request handling
                // don't forget to set username
                LoginRequest loginRequestr = (LoginRequest) responseObject;
                try{
                    boolean check = this.mongoDBController.passwordIsValid( loginRequestr.getUsername(), loginRequestr.getPassword() );
                    if (check) {
                        System.out.println("VALID");
                        pw.println("writeRead|#n##n# [Server]> user ("+loginRequestr.getUsername()+") is successfully logged In ! #n#");
                        pw.flush();

                        username = loginRequestr.getUsername();
                    }else {
                        System.out.println("NOT VALID");
                        pw.println("write|#n##n# [Server]> username OR password Incorrect ! ");
                        pw.flush();
                        pw.println("auth|#n# [Server]> authentication ");
                        pw.flush();
                    }

                }catch(LoginErrorException uexc){
                    pw.println("write|#n##n# [Server]> username OR password Incorrect ! ");
                    pw.flush();
                    pw.println("auth|#n# [Server]> authentication ");
                    pw.flush();
                }

            }

            username =br.readLine();
        } catch (Exception e) {
            System.err.print("\n[Exception]> exception reading Object : "+e.getMessage());
            // check if connection was closed here :
        }

        System.out.print("\n-> New user has connected : "+ username + "  ==> IP : "+ipUser);
        String userStrInput="", str_targetUser="", message="";;
        String [] explode;
        int userIntInput=1, int_targetUser=0 ;
        pw.println("write|#n# [Server]> You are connected now, to close this connection enter 0#n##n#");

        while( true ){
            pw.println("write|#n##n# [Server]> Currently connected users : ");
            i=0;
            pw.flush();
            for( ConnectionThread th: Server.getConnectionThreadList() ) {
                i++;
                pw.println("write|#n#\t\t " + i + " -> " + th.getUsername() + "  (@)  " + th.getIpUser());
            }
            pw.flush();
            pw.println("writeRead|#n# [Server]> You may need to press enter each time to show received messages, To send a message type=> IndexOfUser:YourMessageHere #n#" +
                    "To send a broadcast type=> *:YourMessageHere #n# [Server] your input > ");


            try {
                try {
                   userStrInput = br.readLine();
                    if( userStrInput!=null ) userStrInput = userStrInput.trim();
                }catch( IOException ioexc){
                    System.err.print("\n[IOException3]> Cannot readLine()");
                }
                userIntInput = Integer.parseInt( userStrInput );
                if( userIntInput==0 ) break;
            }catch( NumberFormatException nfexc){
                System.err.print("\n[NumberFormatException]> bad number input");
                explode = userStrInput.split(":");
                if( explode.length<2 || explode[1].trim().length()==0 ){
                    pw.println("write|#n# [Server]> Invalid input, try again. ");
                }else{
                    str_targetUser = explode[0];
                    explode[0] ="";
                    message = String.join( "", explode);
                    try {
                        if(str_targetUser.equals("*")){
                            Server.sendBroadcastMsg( this.username, this.ipUser.split(":")[0], message,this);
                        }else{
                            int_targetUser = Integer.parseInt( str_targetUser );
                            if( int_targetUser<1 || int_targetUser>Server.getConnectionThreadList().size() )
                                throw new NumberFormatException();
                            Server.sendOneToOneMsg( this.username, this.ipUser.split(":")[0], message, int_targetUser-1);
                        }

                    }catch( NumberFormatException nfexc2){
                        pw.println("write|#n#[Server]> Invalid input, try again. ");
                    }
                }
            }
            if( userIntInput==0 ) break;
        }
        closeConnection();
    }

    public void receiveMsg(String srcName, String srcIp, String Message){
        pw.flush();
        pw.println("write|#n#*** #n#*****[Server: New message] from("+srcName+"@"+srcIp+")> "+Message+"#n#***");
    }

    public void closeConnection(){
        try {
            pw.println("write|#n# [Server]> Closing connection ... ");
            socket.close();
            System.out.print("\n-> Connection closed : "+ username +" ==> IP : "+ipUser);
        }catch (IOException ioexc){
            System.err.print("\n[IOException4]> Cannot close connection!");
        }
        Server.romoveConnectionThread(this); // to remove ConnectionThread from the list
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
