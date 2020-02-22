import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws Exception{
        System.out.println("1 for master 0 for follower");
        boolean follower = false;
		Scanner in = new Scanner(System.in); 
		  
        String s = in.nextLine(); 

        if (1==Integer.parseInt(s)) {
			follower = false ;
		}else if (0==Integer.parseInt(s)) {
			follower = true;
		}

		master_server server = new master_server(master_server.DEFAULT_SERVER_PORT,follower);


		
	}
}
