package tests;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

public class testMongoDBConnection {

    public static void main( String args[] ) {

        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017);
        System.out.println("*** Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("RTCATestDatabase");

        //Creating a collection
        try {
            database.createCollection("testCollection");
            System.out.println("*** Collection created successfully");
        } catch (MongoCommandException mce){
            System.out.println("*** Probably this error appears because the collection already exists.");
            System.out.println(mce.getErrorMessage());
        }


    }
}
