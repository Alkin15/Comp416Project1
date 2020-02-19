
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

	String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;
    int player_number;
    int selected_card ; 
    int rounds_won=0;
    int rounds_played=0;
    int game_start=0;
    int[] deck = new int[26];
    public volatile boolean  card_ready;

    public ServerThread(Socket s, int player_number){
        this.s=s;
        this.player_number = player_number;
    }

    public void run() {
    try{
        is= new BufferedReader(new InputStreamReader(s.getInputStream()));
        os=new PrintWriter(s.getOutputStream());

    }catch(IOException e){
        System.out.println("IO error in server thread");
    }

    try {
    	try {
            line=is.readLine();
            selected_card = Integer.parseInt(line);
		} catch (Exception e) {
			// TODO: handle exception
            selected_card = 1;

			
		}

        while(line.compareTo("QUIT")!=0){
        	if (game_start==1) {
        		try {
        			
                    line=is.readLine();
                    selected_card = Integer.parseInt(line);
                    
                    os.println(selected_card);
                    os.flush();
                    
                    card_ready = true;

                    System.out.println("Card Selected By Player "+ player_number + " is :" + getSelected_card());
					
				} catch (Exception e) {

					
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

            
        }
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client "+line+" Closed");
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

        }
    catch(IOException ie){
        System.out.println("Socket Close Error");
    }
    }//end finally
    }
    public void round_won(){
    	rounds_won++;
    	rounds_played++;
    }
    public void round_lost(){
    	rounds_played++;
    }

	public int getPlayer_number() {
		return player_number;
	}

	public void setPlayer_number(int player_number) {
		this.player_number = player_number;
	}

	public int getSelected_card() {
		return selected_card;
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

}
