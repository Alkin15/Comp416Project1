// echo server

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class master_server
{
	/*collection name, a.k.a the game's name. */
	String collectionName;
	
	
	
	public static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_SERVER_PORT = 4444;
	private ServerSocket serverSocket;
	private Socket s;
	public ServerThread[] players;
	public GameThread[] gameThread;
	int player_number = 1;
	int game_number = 0 ;
	int waiting_player_number = 0 ;
	int follower_number = 1;
    follower_Thread[] follower_all;
	ServerSocket Sock ;
	DataInputStream cin;
	DataOutputStream cout;

	/**
	 * Initiates a server socket on the input port and keeps listening on the line
	 * @param port
	 */
	public master_server(int port,boolean follower) throws Exception
	{
		
		if (follower==false) {
    		try
            {
                /*
                Opens up a server socket on the specified port and listens
                 */
            	InetAddress addr = InetAddress.getByName("192.168.43.42");
            	players = new ServerThread[50];
            	gameThread = new GameThread[25];
                serverSocket = new ServerSocket(port,50,addr);
                System.out.println("Opened up a server socket on " + port + addr);
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                System.err.println("Cannot open a server socket on port " + port);
            }
            while (true)
            {
                try {
                	MongoUpdate mongoupdate = new MongoUpdate();
    				s = serverSocket.accept();
    				ServerThread st = new ServerThread(s,player_number, mongoupdate);
    				st.start();
    				
    				players[player_number-1] = st;
    				player_number++;
    				waiting_player_number++;
    				System.out.println("A connection was established with a client on the address of " + s.getRemoteSocketAddress()+ "With Player Number:" + st.player_number);
    				if (waiting_player_number == 2) {
    					collectionName = players[game_number*2].getPlayerName() + "-" + players[game_number*2+1].getPlayerName();
    					System.out.println("Game has started for " + (game_number+1) + " !");
    					GameThread gameThread = new GameThread(game_number,players[game_number*2],players[game_number*2+1], mongoupdate);
    					gameThread.start();
    					game_number++;
    					waiting_player_number = 0 ;
    					mongoupdate.start();
    				}
    				
    				
    			





    				
    			} catch (Exception e) {
    				// TODO: handle exception
    				e.printStackTrace();
    				System.out.println("Connection Error");
    			}

                
            }
           
			
		}else if (follower == true) {
			try {
				InetAddress addr = InetAddress.getByName("192.168.43.42");
				System.out.println("Follower init");
				Sock = new ServerSocket(4445,50,addr);
				follower_Thread[] follower_all = new follower_Thread[25];

				
				while (true) {
					try {
						
						Socket s = Sock.accept();
						follower_Thread ft = new follower_Thread(s,follower_number);
						ft.start();
						System.out.println("A connection was established with a client on the address of " + s.getRemoteSocketAddress()+ "With Player Follower:" + follower_number);
						follower_all[follower_number-1]=ft;
						follower_number++;
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}



}