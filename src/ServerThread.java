
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Enumeration;

public class ServerThread extends Thread{

	MongoUpdate mongoupdate;

	String line=null;
	BufferedReader  is = null;
	PrintWriter os=null;
	Socket s=null;
	int player_number;
	int selected_card ; 
	int rounds_won=0;
	String last_round;
	int rounds_played=0;
	int game_start=0;
	String name;
	boolean is_won ;
	int[] deck = new int[26];
	public volatile boolean  card_ready;

	public ServerThread(Socket s, int player_number, MongoUpdate mongoupdate){
		this.s=s;
		this.player_number = player_number;
		this.mongoupdate = mongoupdate;
	}

	public void run() {
		try{
			is= new BufferedReader(new InputStreamReader(s.getInputStream()));
			os=new PrintWriter(s.getOutputStream());

		}catch(IOException e){
			System.out.println("IO error in server thread");
		}

		try {
			name = enter_name();
			mongoupdate.addUser(name);
			System.out.println(name + " joined session.");
			try {
				line=is.readLine();
				selected_card = Integer.parseInt(line);
			} catch (Exception e) {
				selected_card = 1;


			}

			while(line.compareTo("QUIT")!=0){
				if (game_start==1) {
					try {

						line=is.readLine();
						selected_card = Integer.parseInt(line);

						if (selected_card==2) {
							if (card_ready== true) {
								os.println("You have already selected card!");
								os.flush();

							}else{
								os.println(deck[rounds_played]);
								os.flush();
								card_ready = true;

								System.out.println("Card Selected By Player "+ player_number + " is :" + getSelected_card());

							}

						}
						else if (selected_card==3) {
							if (last_round==null) {
								os.println("No rounds have been played.");
								os.flush();

								System.out.println("No rounds have been played by Player " + getPlayer_number());
							}else {
								os.println("Last round was " + rounds_played + "and result is " + last_round);
								os.flush();

								System.out.println("Last round was " + rounds_played + " for player "+ player_number + " was :" + last_round);
							}


						}
						else if (selected_card==4) {
							os.println("Currently You have played " + rounds_played +" and won "+ rounds_won);
							os.flush();

							System.out.println("Currently player " +player_number +" has played" + rounds_played +" and won "+ rounds_won);

						}else {
							os.println("Invalid Input, Try 2 3 or 4");
							os.flush();

							System.out.println("Invalid Input from" + player_number);
						}





					} catch (Exception e) {


					}
					if(rounds_played==25){
						try{
							System.out.println("Connection Closing..");
							if (is!=null){
								is.close(); 
								System.out.println(" Socket Input Stream Closed");
							}

							if(os!=null){
								os.close();
								System.out.println("Socket Out Closed");
							}
							if (s!=null){
								s.close();
								System.out.println("Socket Closed");
							}
							mongoupdate.dropCollection();

						}
						catch(IOException ie){
							System.out.println("Socket Close Error");
						}
					}


				}
				if (game_start == 0) {

					try {
						if (selected_card!=0) {
							System.out.println("Game is not initialized for player "+ getPlayer_number()  +" input 0 to begin the game!");
							os.println("Game is not initialized for player "+ getPlayer_number()  +" input 0 to begin the game!");
							os.flush();

							line=is.readLine();
							selected_card = Integer.parseInt(line);

						}else if(selected_card==0){
							System.out.println("Game For Player" + player_number + "is initialized!");
							setGame_start(1);
							os.println("Game is initialized! Your deck is = " + Arrays.toString(deck));
							os.flush();

						}
					} catch (Exception e) {


					}




				}
				if (line.compareTo("QUIT")==0) {
					mongoupdate.dropCollection();
					break;
				}
				
			}
			mongoupdate.dropCollection();
		}
		catch(NullPointerException e){
			line=this.getName(); //reused String line for getting thread name
			System.out.println("Client "+line+" Closed");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		finally{    
			try{
				System.out.println("Connection Closing..");
				if (is!=null){
					is.close(); 
					System.out.println(" Socket Input Stream Closed");
				}

				if(os!=null){
					os.close();
					System.out.println("Socket Out Closed");
				}
				if (s!=null){
					s.close();
					System.out.println("Socket Closed");
				}
				mongoupdate.dropCollection();

			}
			catch(IOException ie){
				System.out.println("Socket Close Error");
			}
		}//end finally
	}
	public void round_won(){
		rounds_won++;
		rounds_played++;
		last_round = "You have won the last round!";
	}
	public void round_lost(){
		rounds_played++;
		last_round= "You have lost the last round";

	}

	public int getPlayer_number() {
		return player_number;
	}

	public void setPlayer_number(int player_number) {
		this.player_number = player_number;
	}

	public int getSelected_card() {
		return deck[rounds_played];
	}

	public void setSelected_card(int selected_card) {
		this.selected_card = selected_card;
	}

	public int getRounds_won() {
		return rounds_won;
	}

	public void setRounds_won(int rounds_won) {
		this.rounds_won = rounds_won;
	}

	public int getRounds_played() {
		return rounds_played;
	}

	public void setRounds_played(int rounds_played) {
		this.rounds_played = rounds_played;
	}

	public int getGame_start() {
		return game_start;
	}

	public void setGame_start(int game_start) {
		this.game_start = game_start;
	}

	public String enter_name() throws IOException{
		line=is.readLine();
		os.println(line);
		os.flush();

		return line;
	}

	public String getPlayerName() {
		return name;
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

}
