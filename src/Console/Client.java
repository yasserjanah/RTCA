package Console;

import Authentication.LoginRequest;
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
                LoginRequest lr;

                // Header
                System.out.println("\n\t\t"+Colors.RED_BACKGROUND+"                                    "+Colors.RESET);
                System.out.println("\t\t"+Colors.RED_BACKGROUND+Colors.WHITE_BOLD+"          RTCA Client App           "+Colors.RESET);
                System.out.println("\t\t"+Colors.RED_BACKGROUND+"                                    "+Colors.RESET);

                while (true){
                    serverInput = br.readLine();
                    if( s.isClosed() || serverInput==null ) // throws an exception
                        break;
                    serverCmds = explodeServerResponse(serverInput);
                    switch (serverCmds[0]) {
                        case "auth" -> {
                            do {
                                System.out.print("\n\n\n\t"+Colors.RED+" #"+Colors.CYAN+"------------"+Colors.RED+"[ "+Colors.YELLOW_UNDERLINED+"Authentication"+Colors.RED+" ]"+Colors.CYAN+"------------"+Colors.RED+"#  \n");
                                System.out.print("\n\t"+Colors.GREEN_BOLD+"0"+Colors.CYAN+") "+Colors.WHITE_BOLD+"Exit.");
                                System.out.print("\n\t"+Colors.GREEN_BOLD+"1"+Colors.CYAN+") "+Colors.WHITE_BOLD+"Login.");
                                System.out.print("\n\t"+Colors.GREEN_BOLD+"2"+Colors.CYAN+") "+Colors.WHITE_BOLD+"Register.");
                                System.out.print("\n\n\t"+Colors.CYAN_BOLD_BRIGHT+">> "+Colors.GREEN_BOLD);
                                try {
                                    userIntInput = scanner.nextInt();
                                    scanner.nextLine(); // nextInt() buffer issue
                                } catch (Exception ignored) {
                                }
                            } while (userIntInput < 0 || userIntInput > 2);
                            switch (userIntInput) {
                                case 1 -> {
                                    // Login Request Code
                                    System.out.print("\n"+Colors.GREEN_BOLD+" ["+Colors.PURPLE_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Login"+Colors.RESET+Colors.GREEN_BOLD+"]"+Colors.WHITE+"> Enter the "+Colors.GREEN_BOLD+"username"+Colors.RESET+" : ");
                                    userInput = scanner.nextLine();
                                    username = userInput.trim();
                                    System.out.print("\n"+Colors.GREEN_BOLD+" ["+Colors.PURPLE_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Login"+Colors.RESET+Colors.GREEN_BOLD+"]"+Colors.WHITE+"> Enter the "+Colors.GREEN_BOLD+"password"+Colors.RESET+" : ");
                                    userInput = scanner.nextLine();
                                    lr = new LoginRequest(username, userInput);
                                    oos.writeObject(lr);
                                }
                                case 2 -> {
                                    System.out.print("\n"+Colors.GREEN_BOLD+" ["+Colors.PURPLE_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Register"+Colors.RESET+Colors.GREEN_BOLD+"]"+Colors.WHITE+"> Enter the "+Colors.GREEN_BOLD+"username"+Colors.RESET+" : ");
                                    userInput = scanner.nextLine();
                                    username = userInput.trim();
                                    System.out.print("\n"+Colors.GREEN_BOLD+" ["+Colors.PURPLE_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Register"+Colors.RESET+Colors.GREEN_BOLD+"]"+Colors.WHITE+"> Enter the "+Colors.GREEN_BOLD+"password"+Colors.RESET+" : ");
                                    userInput = scanner.nextLine();
                                    rr = new RegistrationRequest(username, userInput);
                                    oos.writeObject(rr);
                                }
                                case 0 -> {
                                    System.out.print("\n"+Colors.GREEN_BOLD+" ["+Colors.RED_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Exit"+Colors.RESET+Colors.GREEN_BOLD+"]"+Colors.RED+"> Shut Down...\n");
                                    s.close();
                                    return;
                                }
                            }
                        }
                        case "writeRead" -> {
                            outputMsg = serverCmds[1];
                            System.out.print(outputMsg);
                            userInput = scanner.nextLine();
                            pr.println(userInput);
                        }
                        case "write" -> {
                            outputMsg = serverCmds[1];
                            System.out.print(outputMsg);
                        }
                        case "read" -> {
                            userInput = scanner.nextLine();
                            pr.println(userInput);
                        }
                    }
                    try {
                        userIntInput = Integer.parseInt( userStrInput );
                    }catch( NumberFormatException ignored){
                    }
                    if( userIntInput==0 ) break;
                }
                s.close();
            }catch(Exception e){
                System.out.print("\n"+Colors.GREEN_BOLD+" ["+Colors.RED_BACKGROUND+Colors.WHITE_BOLD+"Console.Server::Exception"+Colors.RESET+Colors.GREEN_BOLD+"]"+Colors.RED+"> "+e.getMessage()+"\n");
            }
        }
}
