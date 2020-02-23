import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkClient {

	public static void main(String args[]) throws IOException{

		InetAddress address3=InetAddress.getByName("212.253.113.4"); 
		Config cfg = new Config();
		String address = (cfg.getProperty("address"));
		Socket s1=null;
		String line=null;
		BufferedReader br=null;
		BufferedReader is=null;
		PrintWriter os=null;
		boolean game_init= false;
		try {
			s1=new Socket(address, 4444); // You can use static final constant PORT_NUM
			br= new BufferedReader(new InputStreamReader(System.in));
			is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
			os= new PrintWriter(s1.getOutputStream());
		}
		catch (IOException e){
			e.printStackTrace();
			System.err.print("IO Exception");
		}

		System.out.println("Client Address : "+address);
		System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

		String response=null;
		try{
			line=br.readLine();
			while(line.compareTo("QUIT")!=0){
				os.println(line);
				os.flush();
				response=is.readLine();
				System.out.println("Server Response : "+response);
				line=br.readLine();

			}

		}
		catch(IOException e){
			System.out.println("Invalid Input!");
		}
		finally{

			is.close();os.close();br.close();s1.close();
			System.out.println("Connection Closed");

		}

	}
}