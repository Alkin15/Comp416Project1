import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class follower_FTP {
	public static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
	public static final int port = 4444;
	private ServerSocket Sock;
	private Socket s;
	follower_Thread[] followers;
	int follower_number = 1;
	follower_FTP ftp;

	/**
	 * Initiates a server socket on the input port and keeps listening on the line
	 */
	public follower_FTP()
	{

		try
		{
			/*
            Opens up a server socket on the specified port and listens
			 */
			Config cfg = new Config();

			String address = (cfg.getProperty("address"));
			InetAddress addr = InetAddress.getByName(address);
			Sock = new ServerSocket(port,50,addr);
			DataInputStream cin = new DataInputStream(s.getInputStream());
			DataOutputStream cout = new DataOutputStream(s.getOutputStream());
			ftp = new follower_FTP();
			followers = new follower_Thread[50];

			System.out.println("Opened up a server socket on " + port);
		}
		catch (IOException e)
		{
			//e.printStackTrace();
			System.err.println("Cannot open a server socket on port " + port);
		}
		while (true)
		{



		}
	}

//	public void sendfile(Socket s) throws Exception {
//		Socket sock = s;
//
//		DataInputStream cin = new DataInputStream(sock.getInputStream());
//		DataOutputStream cout = new DataOutputStream(sock.getOutputStream());
//		String filename = cin.readUTF();
//		System.out.println("Reading File " + filename);
//		File f = new File(filename);
//		FileInputStream fin = new FileInputStream(f);
//		int ch;
//		do {
//			ch = fin.read();
//			cout.writeUTF(Integer.toString(ch));
//		} while (ch != -1);
//		fin.close();
//		System.out.println("File Sent");
//	}
//
//	public void receivefile(Socket s) throws Exception {
//		Socket ssock = s;
//
//		DataInputStream cin = new DataInputStream(ssock.getInputStream());
//		DataOutputStream cout = new DataOutputStream(ssock.getOutputStream());
//
//		String filename = cin.readUTF();
//		System.out.println("Receiving File " + filename);
//		File f = new File(filename);
//		FileOutputStream fout = new FileOutputStream(f);
//		int ch;
//		while ((ch = Integer.parseInt(cin.readUTF())) != -1) {
//			fout.write(ch);
//		}
//		System.out.println("Received File...");
//		fout.close();
//	}

}
