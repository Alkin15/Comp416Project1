import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoUpdate extends Thread{
	
	/*collection name, a.k.a the game's name. */
	private String collectionName;
	
	
	MongoConnection connection = new MongoConnection(collectionName);
	boolean isChanged;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now;
	String player1, player2;
	
	public MongoUpdate(String collectionName){
		isChanged = false;
		connection = new MongoConnection(collectionName);
		this.collectionName = collectionName;
	}
	
	public MongoConnection getConnection() {
		return connection;
	}
	
	public void run() {
		
		try
		{
		    while(true) {
		    	Thread.sleep(30000); //wait for 30 secs for update
	    		now = LocalDateTime.now();
	    		System.out.println(dtf.format(now)); //2016/11/16 12:08:43
		    	if (isChanged) {
		    		System.out.println("Current time: " + dtf.format(now) + ", " + connection.getCollectionName() + " will be synchronized");
		    		//updatePlayerInfo(playerName, changedValue, newValue);
		    	} else {
		    		connection.updatePlayerInfo("Thread-1", "NumRounds", "1");
		    		System.out.println("Current time: " + dtf.format(now) + ", no update is needed. Already synced!");
		    	}
		    }
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		
	}
	
	/** Updates game state on collection */
//	public void updatePlayerInfo (String playerName, String changedValue, String newValue) {
//		try {
//			MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
//			Document found = (Document) collection.find(new Document("Player", playerName)).first();
//			if(found != null){
//				Bson updatedvalue = new Document(changedValue, newValue);
//				Bson updateoperation = new Document("$set", updatedvalue);
//				collection.findOneAndUpdate(found,updateoperation);
//			}
//		} catch (Exception e){
//			System.out.println("Error updating player info in " + collectionName + "/" + playerName);
//		}
//		
//	}
	
	/**
	 * Drops collection
	 * */
	public void dropCollection() {
		connection.dropCollection();
	}
	
	/**
	 * Checks if there is a change in the game state
	 * */
	public boolean isChanged() {
		return isChanged;
	}

	/**
	 * Sets the game state to changed or not
	 * */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
	
	/**
	 * @deprecated
	 * */
//	public void incrementRound(String player1, String name2) {
//		// TODO Auto-generated method stub
//		connection.incrementRound(player1, name2);
//	}

	/**
	 * @deprecated
	 * */
//	public void addPointToPlayer(String name) {
//		// TODO Auto-generated method stub
//		connection.addPointToPlayer(name);
//	}

	/**
	 * Checks if the database has the collection with the name collectionName
	 * @param collectionName
	 * */
	public boolean hasCollection(String collectionName) {
		// TODO Auto-generated method stub
		return connection.hasCollection(collectionName);
	}

	public void openCollection() {
		// TODO Auto-generated method stub
		connection.openCollection(player1, player2);
	}
	
	public void addUser(String name) {
		if (player1 == null) player1 = name;
		else if (player2 == null) player2 = name;
	}
	
	public void addUsers(String name1, String name2) {
		player1 = name1;
		player2 = name2;
	}
	
	public String getCollectionName() {
		return this.collectionName;
	}
	
	public String getCollection() {
		return player1 + "-" + player2;
	}

}
