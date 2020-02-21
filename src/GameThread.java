import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;
import java.util.stream.IntStream;


public class GameThread extends Thread{

	MongoConnection connection = new MongoConnection();
	int game_number;
	ServerThread player1;
	ServerThread player2;
	int[] deck = IntStream.range(0, 52).toArray();
	public GameThread(int game_number, ServerThread player1, ServerThread player2){
		this.game_number=game_number;
		this.player1 = player1;
		this.player2 = player2;
	}

	public void run() {
		System.out.println(Arrays.toString(deck));
		shuffleDeck();
		deal_deck();

		if (!connection.hasCollection(player1.getName() + "-" + player2.getName())) {
			connection.openCollection(player1.getName(), player2.getName());
		} else {
			System.out.println("Collection exists");
		}

		while (true) {
			try {
				if (player1.card_ready==true && player2.card_ready==true) {
					compare__cards(game_number);
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
			player1.card_ready=false;
			player2.card_ready=false;
		}else if (get_card_value(player1.getSelected_card())<get_card_value(player2.getSelected_card())) {
			connection.addPointToPlayer(player2.getName());
			player2.round_won();
			player1.round_lost();
			System.out.println("Round won by Player " + player2.getPlayer_number() + " .");
			player1.card_ready=false;
			player2.card_ready=false;
		} else {
			player1.round_lost();
			player2.round_lost();
			System.out.println("Round Tied.");
			player2.card_ready=false;
			player1.card_ready=false;
		}

		connection.incrementRound(player1.getName(), player2.getName());
	}

}
