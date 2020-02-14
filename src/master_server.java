// echo server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;


public class master_server
{
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4445;
    private ServerSocket serverSocket;
    private Socket s;
    public ServerThread[] players;
    int player_number=1;

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
        	players = new ServerThread[2];
            serverSocket = new ServerSocket(port);
            System.out.println("Opened up a server socket on " + port);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            System.err.println("Cannot open a server socket on port " + port);
        }
        while (true)
        {
            try {
				s= serverSocket.accept();
				ServerThread st = new ServerThread(s,player_number);
				st.start();
				players[player_number-1] = st;
				player_number++;
				System.out.println("A connection was established with a client on the address of " + s.getRemoteSocketAddress()+ "With Player Number:" + st.player_number);



				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Connection Error");
			}
            
        }
    }


}