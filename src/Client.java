import Authentication.RegistrationRequest;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {


        public static String[] explodeServerResponse( String response){
            response = response.replace("#n#", "\n");
            String []input = response.split("\\|");
            List<String> exploded = new ArrayList<String>();
            exploded.add( input[0] );
            input[0] = "";
            exploded.add( String.join( "", input) );
            return exploded.toArray(input);
        }

        public static void main(String[] args) {
            try{
                Socket s=new Socket("localhost",65500);

                InputStream is=s.getInputStream();
                OutputStream os=s.getOutputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ObjectOutputStream oos = new ObjectOutputStream(os);
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                PrintWriter pr=new PrintWriter(os,true);
                Scanner scanner=new Scanner(System.in);

                String userStrInput="";
                String [] serverCmds;
                String outputMsg;
                String serverInput;
                int userIntInput=1 ;
                String userInput="1";
                String username;
                RegistrationRequest rr;

                while (true){
                    serverInput = br.readLine();
                    if( s.isClosed() )
                        break;
                    serverCmds = explodeServerResponse( serverInput );
                    switch( serverCmds[0] ){
                        case "auth":
                            do{
                                System.out.print("\n\t ###  Authentication  ###  ");
                                System.out.print("\n [0]> Exit  ");
                                System.out.print("\n [1]> login  ");
                                System.out.print("\n [2]> register  \n\t > ");
                                try {
                                    userIntInput = scanner.nextInt();
                                    scanner.nextLine(); // nextInt() buffer issue
                                }catch(Exception exc){}
                            }while( userIntInput<0 || userIntInput>2 );
                            switch ( userIntInput ){
                                case 1:
                                    // Login Request Code
                                    continue;

                                case 2:
                                    System.out.print("\n [Server]> Enter the username : ");
                                    userInput = scanner.nextLine();
                                    username = userInput.trim();
                                    System.out.print("\n [Server]> Enter the password : ");
                                    userInput = scanner.nextLine();
                                    rr = new RegistrationRequest( username, userInput);
                                    oos.writeObject( rr );
                                    break;
                                case 0:
                                    System.out.print("\n [Server]> Quiting ....");
                                    s.close();
                                    return;
                            }
                            break;
                        case "writeRead":
                            outputMsg = serverCmds[1];
                            System.out.print(outputMsg);
                            userInput = scanner.nextLine();
                            pr.println(userInput);
                            break;
                        case "write":
                            outputMsg = serverCmds[1];
                            System.out.print(outputMsg);
                            break;
                        case "read":
                            userInput = scanner.nextLine();
                            pr.println(userInput);
                            break;
                    }
                    try {
                        userIntInput = Integer.parseInt( userStrInput );
                    }catch( NumberFormatException nfexc){
                    }
                    if( userIntInput==0 ) break;
                }
                s.close();
            }catch(Exception e){
                System.err.println("\n [Exception]> "+e.getMessage() );
            }
        }
}
