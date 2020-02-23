import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

class FTPClient {

	public static void main(String[] args) throws Exception {

		int follower_number=1;
		File theDir = new File("Follower" + Integer.toString(follower_number));
		// if the directory does not exist, create it
		while (theDir.exists()) {
			follower_number++;
			theDir = new File("Follower" + Integer.toString(follower_number));
		}
		System.out.println("creating directory: " + theDir.getName());
		boolean result = false;

		try{
			theDir.mkdir();
			result = true;
		}
		catch(SecurityException se){
			//handle it
		}
		if(result) {
			System.out.println("DIR created");
			String option;
			Config cfg = new Config();
			String address   = cfg.getProperty("address");
			InetAddress addr = InetAddress.getByName(address);
			DataInputStream in = new DataInputStream(System.in);
			Socket s = new Socket(addr, 4445);
			System.out.println("MENU");
			System.out.println("1.SEND");
			System.out.println("2.RECEIVE");
			FTPClient ftp = new FTPClient();
			while (true) {
				option = in.readLine();
				if (option.equals("1")) {
					System.out.println("SEND Command Received..");
					ftp.sendfile(s);
				}

				else if (option.equals("2")) {
					System.out.println("RECEIVE Command Received..");
					ftp.receivefile(s, follower_number);
				}

			}
		}
	}

	public void sendfile(Socket s) throws Exception {
		Socket ssock = s;

		DataInputStream in = new DataInputStream(System.in);

		DataInputStream cin = new DataInputStream(ssock.getInputStream());
		DataOutputStream cout = new DataOutputStream(ssock.getOutputStream());

		cout.writeUTF("RECEIVE");

		String filename = in.readLine();
		System.out.println("Reading File " + filename);
		cout.writeUTF(filename);
		File f = new File(filename);
		FileInputStream fin = new FileInputStream(f);
		int ch;
		do {
			ch = fin.read();
			cout.writeUTF(String.valueOf(ch));
		} while (ch != -1);
		fin.close();
		System.out.println("File Sent");
	}

	public void receivefile(Socket s, int follower_number) throws Exception {
		Socket ssock = s;
		DataInputStream in = new DataInputStream(System.in);
		DataInputStream cin = new DataInputStream(ssock.getInputStream());
		DataOutputStream cout = new DataOutputStream(ssock.getOutputStream());

		cout.writeUTF("SEND");

		String filename = in.readLine();
		cout.writeUTF(filename);
		System.out.println("Receiving File " + filename);
		File theDir = new File("Follower" + Integer.toString(follower_number));
		// if the directory does not exist, create it
		File f = new File("Follower"+Integer.toString(follower_number)+ "/" + filename);
		String path = ("Follower"+Integer.toString(follower_number)+ "/" + filename);
		FileOutputStream fout = new FileOutputStream(path);
		int ch;
		do {
			ch = Integer.parseInt(cin.readUTF());
			if (ch != -1)
				fout.write(ch);
		} while (ch != -1);
		System.out.println("Received File...");
		fout.close();
	}
}