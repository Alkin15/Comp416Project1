// echo server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;


public class master_server
{
	public static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_SERVER_PORT = 4444;
	private ServerSocket serverSocket;
	private Socket s;
	public ServerThread[] players;
	public GameThread[] gameThread;
	int player_number = 1;
	int game_number = 0 ;
	int waiting_player_number = 0 ;

	/**
	 * Initiates a server socket on the input port and keeps listening on the line
	 * @param port
	 */
	public master_server(int port)
	{
		try
		{
			/*
            Opens up a server socket on the specified port and listens
			 */
			InetAddress addr = InetAddress.getByName("172.20.132.209");
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
				s = serverSocket.accept();
				ServerThread st = new ServerThread(s,player_number);
				st.start();
				players[player_number-1] = st;
				player_number++;
				waiting_player_number++;
				System.out.println("A connection was established with a client on the address of " + s.getRemoteSocketAddress()+ "With Player Number:" + st.player_number);
				if (waiting_player_number == 2) {
					System.out.println("Game has started for " + (game_number+1) + " !");
					GameThread gameThread = new GameThread(game_number,players[game_number*2],players[game_number*2+1]);
					gameThread.start();
					game_number++;
					waiting_player_number = 0 ;

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Connection Error");
			}


		}
	}





}