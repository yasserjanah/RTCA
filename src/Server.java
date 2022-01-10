import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
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
                System.out.println("New user has connected !");

                // each object of this class will be responsible for communicating with a user
                // each socket created means that a new user has connected

            }

        }catch (IOException e){
            closeServer();
        }
    }

    public void closeServer(){
        try {
            // server still running
            if(serverSocket != null)
                serverSocket.close();

        } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(65500);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
