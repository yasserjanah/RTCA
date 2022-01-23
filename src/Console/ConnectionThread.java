package Console;

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
        ipUser = socket.getRemoteSocketAddress().toString();

        while(true){ // while not logged
            pw.println("auth|#n# [Console.Server]> authentication ");
            pw.flush();
            try {
                Object responseObject = ois.readObject();
                if (responseObject instanceof RegistrationRequest) {

                    RegistrationRequest rr = (RegistrationRequest) responseObject;
                    try {
                        synchronized (this.mongoDBController) {
                            this.mongoDBController.insertionOfUser(rr.getUsername(), rr.getPassword());
                        }
                    } catch (UserExistsException uexc) {
                        pw.println("write|#n##n#"+Colors.WHITE_BOLD+" ["+Colors.YELLOW_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Register"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"> "+Colors.GREEN_BOLD+rr.getUsername()+Colors.YELLOW_BOLD+" already taken, choose another one "+Colors.RED+"! \n\n"+Colors.RESET);
                        pw.println("auth|#n# [Console.Server]> authentication ");
                        pw.flush();
                    }
                    pw.println("write|#n##n#"+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Register"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"> user "+ Colors.GREEN_BOLD + rr.getUsername() + Colors.RESET + " is " + Colors.GREEN_UNDERLINED + "successfully"+Colors.RESET+" registred, " + Colors.WHITE_BOLD + " please sign-in " + Colors.YELLOW_BOLD+ "! #n#");
                    pw.flush();
                    pw.println("auth|#n# [Console.Server]> authentication ");
                    pw.flush();
                } else {
                    // pw.println("auth|#n# [Console.Server]> authentication ");
                    // pw.flush();
                    // Login request handling
                    // don't forget to set username
                    LoginRequest loginRequestr = (LoginRequest) responseObject;
                    try {
                        boolean check = this.mongoDBController.passwordIsValid(loginRequestr.getUsername(), loginRequestr.getPassword());
                        if (check) {
                            username = loginRequestr.getUsername();
                            pw.println("write|#n##n#"+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Login"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"> user "+ Colors.GREEN_BOLD + loginRequestr.getUsername() + Colors.RESET + " is " + Colors.GREEN_UNDERLINED + "successfully"+Colors.RESET+" logged in. #n#"); // writeRead = write+read , so client will send smthing not expected by server
                            pw.flush();
                            break; // if loged break
                        } else {
                            pw.println("write|#n##n#"+Colors.WHITE_BOLD+" ["+Colors.RED_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Login"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.YELLOW_BOLD+"> username OR password is Incorrect! #n#");
                            pw.flush();
                            pw.println("auth|#n# [Console.Server]> authentication ");
                            pw.flush();
                        }

                    } catch (LoginErrorException uexc) {
                        pw.println("write|#n##n#"+Colors.WHITE_BOLD+" ["+Colors.RED_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Login"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.YELLOW_BOLD+"> username OR password is Incorrect! #n#");
                        pw.flush();
                        pw.println("auth|#n# [Console.Server]> authentication ");
                        pw.flush();
                    }

                }
            } catch (Exception e) {
                System.err.print("\n[Exception]> exception reading Object : " + e.getMessage());
                // check if connection was closed here :
                // I've closed the connection because when the client doesn't send any object
                // the server enter an inifite loop
                closeConnection();
                break;
            }
        }

        System.out.print("\n"+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::NewUserConnected"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> New user has connected : "+ Colors.GREEN_BOLD + username + Colors.CYAN_BOLD + "  ==>"+Colors.WHITE_BOLD+" IP : "+Colors.GREEN_BOLD+ipUser+Colors.RESET);
        String userStrInput="", str_targetUser="", message="";;
        String [] explode;
        int userIntInput=1, int_targetUser=0 ;
        pw.println("write|#n# "+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> You are "+Colors.GREEN_BOLD+"connected"+Colors.RESET+" now, to close this connection enter "+Colors.YELLOW_BOLD+"0"+Colors.RESET+"#n##n#");

        while( true ){
            pw.println("write|#n##n# "+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+" Currently connected users : ");
            i=0;
            pw.flush();
            for( ConnectionThread th: Server.getConnectionThreadList() ) {
                i++;
                pw.println("write|#n#\t\t " + Colors.GREEN_BOLD + i + Colors.YELLOW_BOLD + " -> " + Colors.GREEN_BOLD + th.getUsername() + Colors.RESET + "  (@)  " + Colors.GREEN_BOLD + th.getIpUser() + Colors.RESET );
            }
            pw.flush();
            pw.println("writeRead|#n# "+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> You may need to press enter each time to show received messages, #n#" +
                    Colors.WHITE_BOLD+"  ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> To send a message "+Colors.YELLOW_BOLD+"type=> "+Colors.GREEN_BOLD+"IndexOfUser"+Colors.YELLOW_BOLD+":"+Colors.CYAN_BOLD+"YourMessageHere #n#" +
                    Colors.WHITE_BOLD+"  ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> To send a broadcast "+Colors.YELLOW_BOLD+"type=> "+Colors.GREEN_BOLD+"*"+Colors.YELLOW_BOLD+":"+Colors.CYAN_BOLD+"YourMessageHere #n# " + Colors.RESET + Colors.CYAN_BACKGROUND+"#"+Colors.RESET+Colors.GREEN_BOLD+"> "+Colors.WHITE_BOLD);

            try {
                try {
                   userStrInput = br.readLine();
                    if( userStrInput!=null ) userStrInput = userStrInput.trim();
                }catch( IOException ioexc){
                    System.err.print("\n[IOException3]> Cannot readLine()");
                    closeConnection();
                    break;
                }
                assert userStrInput != null;
                userIntInput = Integer.parseInt( userStrInput );
                if( userIntInput==0 ) break;
            }catch( NumberFormatException nfexc){
                System.err.print("\n[NumberFormatException]> bad number input");
                if (userStrInput == null ) {
                    closeConnection();
                    break;
                }
                explode = userStrInput.split(":");
                if( explode.length<2 || explode[1].trim().length()==0 ){
                    pw.println("write|#n# "+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> Invalid Input, try again.");
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
                        pw.println("write|#n# "+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::CONNECTED"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> Invalid Input, try again.");
                    }
                }
            }
            if( userIntInput==0 ) break;
        }
        closeConnection();
    }

    public void receiveMsg(String srcName, String srcIp, String Message){
        pw.flush();
        pw.println("write|#n# "+Colors.WHITE_BOLD+" ["+Colors.GREEN_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::MESSAGE"+Colors.RESET+Colors.WHITE_BOLD+"]"+Colors.WHITE_BOLD+"-> New message from "+Colors.YELLOW_BOLD+"("+Colors.GREEN_BOLD+srcName+Colors.WHITE_BOLD+"@"+Colors.GREEN_BOLD+srcIp+Colors.YELLOW_BOLD+")> "+Colors.WHITE_BOLD+Message+"#n#");
    }

    public void closeConnection(){
        try {
            pw.println("write|#n# [Console.Server]> Closing connection ... ");
            socket.close();
            System.out.print("\n-> Connection closed : "+ username +" ==> IP : "+ipUser);
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
