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
    }

    @Override
    public void run() {

        ipUser=socket.getRemoteSocketAddress().toString();
        pw.println("Enter Your Name : ");
        try {
            nameUser=br.readLine();
        } catch (IOException e) {
            System.err.print("\n[IOException]> Cannot readLine()");
        }

        System.out.println("\n-> New user has connected : "+nameUser + "  ==> IP : "+ipUser);
        String userStrInput="";
        int userIntInput=1 ;

        while( true ){
            pw.println("\r\n[i]> You are connected now, press 0 to close the connection : ");
            try {
                try {
                   userStrInput = br.readLine();
                    if( userStrInput!=null ) userStrInput = userStrInput.trim();
                }catch( IOException ioexc){
                    System.err.print("\n[IOException]> Cannot readLine()");
                }
                userIntInput = Integer.parseInt( userStrInput );
            }catch( NumberFormatException nfexc){
                pw.println("\r\n[i]> You can stay here as long as you want. ");
                System.err.print("\n[NumberFormatException]> bad number input");
            }
            if( userIntInput==0 ) break;
        }
        closeConnection();
    }

    public void closeConnection(){
        try {
            pw.println("\r\n[i]> Closing connection ... ");
            socket.close();
            System.out.print("\n-> Connection closed : "+nameUser+" ==> IP : "+ipUser);
        }catch (IOException ioexc){
            System.err.print("\n[IOException]> Cannot close connection!");
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
