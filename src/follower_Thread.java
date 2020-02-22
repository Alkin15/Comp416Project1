import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class follower_Thread extends Thread{
	int follower_number;
	DataInputStream cin ;
	DataOutputStream cout ;
	Socket s;
	public follower_Thread (Socket s,int follower_number) throws IOException{
		this.follower_number = follower_number;
		this.s = s;
		cin = new DataInputStream(s.getInputStream());
		cout = new DataOutputStream(s.getOutputStream());

	}
	
	public void run(){
		try {
			String option = cin.readUTF();
			if (option.equals("SEND")) {
				System.out.println("SEND Command Received..");
				sendfile(s);
			}

			else if (option.equals("RECEIVE")) {
				System.out.println("RECEIVE Command Received..");
				receivefile(s);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	 public void sendfile(Socket s) throws Exception {
			Socket ssock = s;

			cin = new DataInputStream(ssock.getInputStream());
			cout = new DataOutputStream(ssock.getOutputStream());
			String filename = cin.readUTF();
			System.out.println("Reading File " + filename);
			File f = new File(filename);
			FileInputStream fin = new FileInputStream(f);
			int ch;
			do {
				ch = fin.read();
				cout.writeUTF(Integer.toString(ch));
			} while (ch != -1);
			fin.close();
			System.out.println("File Sent");
		}

		public void receivefile(Socket s) throws Exception {
			Socket ssock = s;

			cin = new DataInputStream(ssock.getInputStream());
			cout = new DataOutputStream(ssock.getOutputStream());

			String filename = cin.readUTF();
			System.out.println("Receiving File " + filename);
			File f = new File(filename + Integer.toString(follower_number));
			FileOutputStream fout = new FileOutputStream(f);
			int ch;
			while ((ch = Integer.parseInt(cin.readUTF())) != -1) {
				fout.write(ch);
			}
			System.out.println("Received File...");
			fout.close();
		}
}
