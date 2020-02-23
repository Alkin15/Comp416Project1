import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;


public class GameThread extends Thread{

	String collectionName;
	MongoConnection connection;
	
	int game_number;
	ServerThread player1;
	ServerThread player2;
	int[] deck = IntStream.range(0, 52).toArray();
	JSONObject states = new JSONObject();
	JSONObject hash = new JSONObject();
	static int counter = 0;
	String filename;

	public GameThread(int game_number, ServerThread player1, ServerThread player2, MongoConnection connection){
		this.game_number=game_number;
		this.player1 = player1;
		this.player2 = player2;
		this.collectionName = player1.getName() + "-" + player2.getName();
		this.connection = connection;

		//Create states json file
		filename = collectionName + ".json";
		try (FileWriter file = new FileWriter(filename)) {

			file.write(states.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}


		//Create hash json file
		try (FileWriter file = new FileWriter("hash.json")) {
			hash.put("HashCode", states.hashCode());
			file.write(hash.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println(Arrays.toString(deck));
		shuffleDeck();
		deal_deck();


		
		if (!connection.hasCollection(collectionName)) {
			connection.openCollection(player1.getName(), player2.getName());
		} else {
			System.out.println("Collection exists");
		}

		while (true) {
			try {
				if (player1.card_ready==true && player2.card_ready==true) {
					compare__cards(game_number);
				}
				if (player1.getRounds_played() == 25 && player2.getRounds_played() == 25) {
					connection.dropCollection();
				}
			} catch (Exception e) {

			}
		}


	}
	void shuffleDeck() {
		Random rand = new Random();

		for (int i = 0; i < 52; i++) {
			int j = rand.nextInt(52);
			swapCards(i, j);
		}
	}

	void swapCards(int i, int j) {
		int temp = deck[i];
		deck[i] = deck[j];
		deck[j] = temp;
	}
	public void deal_deck(){
		for (int i = 0; i < 26; i++) {
			player1.deck[i]=deck[i];
			player2.deck[i]=deck[i+26];
		}
	}
	public int get_card_value(int card){
		return card%13;
	}
	public void compare__cards(int game_number){
		if (get_card_value(player1.getSelected_card())>get_card_value(player2.getSelected_card())) {
			connection.addPointToPlayer(player1.getName());
			player1.round_won();
			player2.round_lost();
			System.out.println("Round won by Player " + player1.getPlayer_number() + " .");
			addState("Round won by Player " + player1.getPlayer_number() + " .");
			player1.card_ready=false;
			player2.card_ready=false;
		}else if (get_card_value(player1.getSelected_card())<get_card_value(player2.getSelected_card())) {
			connection.addPointToPlayer(player2.getName());
			player2.round_won();
			player1.round_lost();
			System.out.println("Round won by Player " + player2.getPlayer_number() + " .");
			addState("Round won by Player " + player2.getPlayer_number() + " .");
			player1.card_ready=false;
			player2.card_ready=false;
		} else {
			player1.round_lost();
			player2.round_lost();
			System.out.println("Round Tied.");
			addState("Round Tied.");
			player2.card_ready=false;
			player1.card_ready=false;
		}

		connection.incrementRound(player1.getName(), player2.getName());
	}



	public void addState(String state){
		String key = Integer.toString(counter);
		states.put(key, state);
		try (FileWriter file = new FileWriter(filename)) {

			file.write(states.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		counter++;

		//HashCode Updated
		hash.put("HashCode", hash.hashCode());
		try (FileWriter file = new FileWriter("hash.json")) {
			hash.put("HashCode", states.hashCode());
			file.write(hash.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
