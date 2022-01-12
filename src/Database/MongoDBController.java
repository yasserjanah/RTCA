package Database;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class MongoDBController {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    public MongoDBController() {
        mongoClient=MongoConnection.getMongoClient();

        // Accessing the database
        mongoDatabase=mongoClient.getDatabase("RTCADatabase");

        // Retrieving a collection
        mongoCollection=mongoDatabase.getCollection("user");
        System.out.println("Collection selected successfully");
    }

    public void insertionOfUser(String username , String password){
        Document document=mongoCollection.find(Filters.eq("username", username)).first();
        if(document!=null){
            System.out.println("[Erro]:username " +username+" exists ");
        }
        else {
            mongoCollection.insertOne(transformToDocument(username,password));
            System.out.println("user inserted successfully ");
        }
    }

    private Document transformToDocument(String username , String password){
        String hashedPassword= BCrypt.hashpw(password,BCrypt.gensalt());
        Document document=new Document("username",username).append("password",hashedPassword);
        return document;
    }

    public boolean passwordIsValid(String username, String password){
        Document document=mongoCollection.find(Filters.eq("username", username)).first();
        if( document==null){
            System.out.println("[Erro]:username "+ username +" not exists ");
            return false;
        }
        String pw_hash =document.getString("password");
        if( BCrypt.checkpw(password, pw_hash) ) {
            System.out.println("password valid ");
            return true;
        }else {
            System.out.println("[Erro]:password not valid");
            return false;
        }
    }
}
