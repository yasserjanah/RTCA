import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ConnectionThread extends Thread {

    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private String nameUser;
    private String ipUser;

    ConnectionThread(Socket socket) throws IOException {
        this.socket = socket;
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        pw = new PrintWriter( os, true);
        nameUser = "{UNSET}";
    }

    @Override
    public void run() {
        int i;
        ipUser=socket.getRemoteSocketAddress().toString();

        pw.println("writeRead|#n# [Server]> Enter Your Name to start : ");
        pw.flush();
        try {
            nameUser=br.readLine();
        } catch (IOException e) {
            System.err.print("#n#[IOException2]> Cannot readLine()");
        }

        System.out.print("#n#-> New user has connected : "+nameUser + "  ==> IP : "+ipUser);
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
                pw.println("write|#n#\t\t " + i + " -> " + th.getNameUser() + "  (@)  " + th.getIpUser());
            }
            pw.flush();
            pw.println("writeRead|#n# [Server]> You may need to press enter each time to show received messages, To send a message type=> IndexOfUser:YourMessageHere #n# [Server] your input > ");


            try {
                try {
                   userStrInput = br.readLine();
                    if( userStrInput!=null ) userStrInput = userStrInput.trim();
                }catch( IOException ioexc){
                    System.err.print("#n#[IOException3]> Cannot readLine()");
                }
                userIntInput = Integer.parseInt( userStrInput );
                if( userIntInput==0 ) break;
            }catch( NumberFormatException nfexc){
                System.err.print("#n#[NumberFormatException]> bad number input");
                explode = userStrInput.split(":");
                if( explode.length<2 || explode[1].trim().length()==0 ){
                    pw.println("write|#n# [Server]> Invalid input, try again. ");
                }else{
                    str_targetUser = explode[0];
                    explode[0] ="";
                    message = String.join( "", explode);
                    try {
                        int_targetUser = Integer.parseInt( str_targetUser );
                        if( int_targetUser<1 || int_targetUser>Server.getConnectionThreadList().size() )
                            throw new NumberFormatException();
                        Server.sendOneToOneMsg( this.nameUser, this.ipUser.split(":")[0], message, int_targetUser-1);
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

        pw.println("write|#n#*** #n#*****[Server: New message] from("+srcName+"@"+srcIp+")> "+Message+"#n#***");
    }

    public void closeConnection(){
        try {
            pw.println("write|#n# [Server]> Closing connection ... ");
            socket.close();
            System.out.print("#n#-> Connection closed : "+nameUser+" ==> IP : "+ipUser);
        }catch (IOException ioexc){
            System.err.print("#n#[IOException4]> Cannot close connection!");
        }
        Server.romoveConnectionThread(this); // to remove ConnectionThread from the list
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionThread)) return false;
        ConnectionThread that = (ConnectionThread) o;
        return Objects.equals(nameUser, that.nameUser) && Objects.equals(ipUser, that.ipUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameUser, ipUser);
    }


    public String getNameUser() {
        return nameUser;
    }

    public String getIpUser() {
        return ipUser;
    }
}
