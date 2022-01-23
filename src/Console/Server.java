package Console;

import Database.MongoDBController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    // list of connected users
    private static List<ConnectionThread> connectionThreadList=new ArrayList<>();

    public MongoDBController mongoDBController;

    // incoming connections
    ServerSocket serverSocket;
    // create socket client to communicate

    // Setup
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.mongoDBController = new MongoDBController();
    }

    public void startServer(){
        try {
            // run until the server get closed
            // while is open <==> isn't closed
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();

                ConnectionThread connectionThread=new ConnectionThread( socket, this.mongoDBController);
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
            System.out.println("\t - "+c.getUsername() + " ==> IP : "+c.getIpUser());
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

    public static List<ConnectionThread> getConnectionThreadList() {
        return connectionThreadList;
    }

    public static void sendOneToOneMsg( String srcName, String srcIp, String Message, int targetIndex){
        connectionThreadList.get(targetIndex).receiveMsg( srcName, srcIp, Message);
    }

    public static void sendBroadcastMsg( String srcName, String srcIp, String Message, ConnectionThread th){
        for (ConnectionThread c : connectionThreadList){
            if (c.equals(th)) continue;
            c.receiveMsg( srcName, srcIp, Message);
        }

    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(65500);
        Server server = new Server(serverSocket);
        System.out.println("\n-> Starting server on port : 65500 ");
        server.startServer();
    }
}
