package tests;

import Database.MongoDBController;
import Exceptions.UserExistsException;

public class MongoDBControllerTest {

    public static void main(String[] args) {

        MongoDBController registerLoginFunctions=new MongoDBController();
        // insertion
        try{
            registerLoginFunctions.insertionOfUser("ayoub","123");
        }catch( UserExistsException exc ){
            System.err.println("\n User 'ayoub' already exists ");
        }
        try{
            registerLoginFunctions.insertionOfUser("amine","123");
        }catch( UserExistsException exc ){
            System.err.println("\n User 'amine' already exists ");
        }
        try{
            registerLoginFunctions.insertionOfUser("hassan","123");
        }catch( UserExistsException exc ){
            System.err.println("\n User 'hassan' already exists ");
        }

        //validation of password
        //System.out.println("\n Password validation=> trying (hassan) as password for 'ayoub' => "+registerLoginFunctions.passwordIsValid("ayoub","hassan") );
        //System.out.println("\n Password validation=> trying (123) as password for 'amine' => "+registerLoginFunctions.passwordIsValid("amine","123") );
        //System.out.println("\n Password validation=> trying (123) as password for 'hassan' => "+registerLoginFunctions.passwordIsValid("hassan","123") );


    }
}
