import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
        public static void main(String[] args) {
            try{
                Socket s=new Socket("localhost",65500);

                InputStream is=s.getInputStream();
                OutputStream os=s.getOutputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                PrintWriter pr=new PrintWriter(os,true);
                Scanner scanner=new Scanner(System.in);
                System.out.println(br.readLine());
                String name= scanner.nextLine();
                pr.println(name);

                String userStrInput="";
                int userIntInput=1 ;
                while (true){
                    br.readLine();
                    System.out.println(br.readLine());
                    try {
                        userStrInput =scanner.nextLine();
                        pr.println(userStrInput);
                        if( userStrInput!=null ) userStrInput = userStrInput.trim();

                        userIntInput = Integer.parseInt( userStrInput );
                    }catch( NumberFormatException nfexc){
                        br.readLine();
                        System.out.println(br.readLine());
                    }
                    if( userIntInput==0 ) break;
                }
                br.readLine();
                System.out.println(br.readLine());

                s.close();
            }catch(Exception e){System.out.println(e);}
        }
}
