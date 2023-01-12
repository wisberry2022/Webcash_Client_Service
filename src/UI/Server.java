package UI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8000);
			Socket cl = null;
			Socket cslr = null;
			
			while(true) {
				cl = server.accept();
				cslr = server.accept();	
			}
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
