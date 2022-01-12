package tests;

import Database.MongoDBController;

public class MongoDBControllerTest {

    public static void main(String[] args) {

        MongoDBController registerLoginFunctions=new MongoDBController();
        // insertion
        registerLoginFunctions.insertionOfUser("sinje","123");
        registerLoginFunctions.insertionOfUser("pppedp ","hassan");
        registerLoginFunctions.insertionOfUser("pppedp ","hassan");

        //validation of password
        registerLoginFunctions.passwordIsValid("pppedp ","hassan");
        registerLoginFunctions.passwordIsValid("pppedp ","hassanaeae");
        registerLoginFunctions.passwordIsValid("aaaaaa ","hassan");

    }
}
