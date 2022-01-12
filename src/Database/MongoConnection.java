package Database;

import com.mongodb.MongoClient;

public class MongoConnection {
    private static MongoClient mongoClient;
    static {
        // Creating a Mongo client
        mongoClient=new MongoClient("localhost",27017);
    }


    public static MongoClient getMongoClient() {
        return mongoClient;
    }
}
