package tests;

import Database.MongoDBFunctions;

public class MongoDBFunctionsTest {

    public static void main(String[] args) {

        MongoDBFunctions registerLoginFunctions=new MongoDBFunctions();
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
