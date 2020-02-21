import java.net.Socket;
import java.util.Random;
import java.util.stream.IntStream;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class MongoConnection{
	
	
	
	/*collection name, a.k.a the game's name. */
	static String collectionName;
	
	
	private static final String uri = "mongodb+srv://Admin:admin@cluster0-9d4rq.mongodb.net/test?retryWrites=true&w=majority";
	private static final MongoClientURI clientURI = new MongoClientURI(uri);
	private static final MongoClient mongoClient = new MongoClient(clientURI);
	protected static final MongoDatabase mongoDatabase = mongoClient.getDatabase("WarGame");
	
	public MongoConnection() {
		// TODO Auto-generated constructor stub
		
	}


	

	

	/** Check if our database has a collection with the same name. 
	 * If it does, it means we cannot create a game with the same name,
	 * since we want the game names to be unique */
	public boolean hasCollection (String collectionName) {
		for (String name : mongoDatabase.listCollectionNames()) {
			if (name.equals(collectionName)) {
				return true;
			}
		}
		return false;
	}

	/** Opens a new collection for a new game */
	public void openCollection (String player1, String player2) {
		collectionName = player1 + "-" + player2;	
		mongoDatabase.createCollection(collectionName);
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);

		Document document1 = new Document("Player", player1);
		document1.append("NumRounds", 0);
		document1.append("Score", 0);
		//document1.append("RemainingCards", deck1);
		collection.insertOne(document1);

		Document document2 = new Document("Player", player2);
		document2.append("NumRounds", 0);
		document2.append("Score", 0);
		//document2.append("RemainingCards", deck2);
		collection.insertOne(document2);
	}

	/** Updates game state on collection */
	public void updatePlayerInfo (String playerName, String changedValue, String newValue) {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
			Document found = (Document) collection.find(new Document("Player", playerName)).first();
			if(found != null){
				Bson updatedvalue = new Document(changedValue, newValue);
				Bson updateoperation = new Document("$set", updatedvalue);
				collection.updateOne(found,updateoperation);
			}
		} catch (Exception e){
			System.out.println("Error updating player info in " + collectionName + "/" + playerName);
		}
		
	}
	
	/** Increases a player's score by 1 */
	public void addPointToPlayer (String playerName) {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
			Document found = (Document) collection.find(new Document("Player", playerName)).first();
			if(found != null){
				int oldscore = found.getInteger("Score");
				Bson updatedvalue = new Document("Score", oldscore++);
				Bson updateoperation = new Document("$set", updatedvalue);
				collection.updateOne(found,updateoperation);
			}
		} catch (Exception e){
			System.out.println("Error adding point to player in " + collectionName + "/" + playerName);
		}
		
	}
	
	/** Removes the selected card from a player's deck */
	public void removeCard (String playerName, int cardNum) {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
			Document found = (Document) collection.find(new Document("Player", playerName)).first();
			if(found != null){
				int[] cards = (int[]) found.get("RemainingCards");
				
				int result[] = new int[cards.length-1];
				int j = 0;
			    for(int i = 0; i < cards.length; i++)
			        if(cardNum != cards[i]) {
			            result[j] = cards[i];
			            j++;
			        }
				
				Bson updatedvalue = new Document("Remaining cards", result);
				Bson updateoperation = new Document("$set", updatedvalue);
				collection.updateOne(found,updateoperation);
			}
		} catch (Exception e){
			System.out.println("Error removing card in " + collectionName + "/" + playerName);
		}
		
	}
	
	/** Drops collection */
	public void dropCollection() {
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		collection.drop();
	}

}
