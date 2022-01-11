import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    // list of connected users
    private static List<ConnectionThread> connectionThreadList=new ArrayList<>();

    // incoming connections
    ServerSocket serverSocket;
    // create socket client to communicate

    // Setup
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try {
            // run until the server get closed
            // while is open <==> isn't closed
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();

                ConnectionThread connectionThread=new ConnectionThread(socket);
                connectionThreadList.add(connectionThread); // new thread for every connection, added to arrayList
                connectionThread.start();

                // each object of this class will be responsible for communicating with a user
                // each socket created means that a new user has connected
            }
        }catch (IOException e){
            closeServer();
        }
    }

    //remove connectionThread
    public static void romoveConnectionThread(ConnectionThread connectionThread){
        connectionThreadList.remove(connectionThread);

        System.out.print("\n ................................ \n connected users :\n");
        for (ConnectionThread c:connectionThreadList) {
            System.out.println("\t - "+c.getNameUser() + " ==> IP : "+c.getIpUser());
        }
        System.out.println(" ................................ ");
    }


    public void closeServer(){
        try {
            // server still running
            if(serverSocket != null)
                serverSocket.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(65500);
        Server server = new Server(serverSocket);
        System.out.print("\n-> Starting server on port : 65500 ");
        server.startServer();
    }
}
