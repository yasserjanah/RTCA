import java.io.*;
import java.net.Socket;

public class ConnectionThread extends Thread {

    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;

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

        System.out.println("\n-> New user has connected !");
        String userStrInput="";
        int userIntInput=1 ;

        while( true ){
            pw.println("\r\n[i]> You are connected now, press 0 to close the connection : ");
            try {
                try {
                   userStrInput = br.readLine();
                    if( userStrInput!=null ) userStrInput.trim();
                }catch( IOException ioexc){
                    System.err.print("\n[IOException]> Cannot readLine()");
                }
                userIntInput = Integer.parseInt( userStrInput );
            }catch( NumberFormatException nfexc){
                pw.print("\r\n[i]> You can stay here as long as you want. ");
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
            System.out.print("\n-> Connection closed !");
        }catch (IOException ioexc){
            System.err.print("\n[IOException]> Cannot close connection!");
        }

    }
}
