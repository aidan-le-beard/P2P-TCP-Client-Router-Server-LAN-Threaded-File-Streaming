import java.io.*;
import java.net.*;

public class ServerTCPServerRouter {
	public static int ind;

	public static void main(String[] args) throws IOException {
		Socket clientSocket = null; // socket for the thread
		Object[][] RoutingTable = new Object[100][4]; // routing table
		int SockNum = 5556; // port number
		Boolean Running = true;
		int ind = 0; // indext in the routing table

		// Accepting connections
		ServerSocket serverSocket = null; // server socket for accepting connections
		try {
			serverSocket = new ServerSocket(SockNum);
			System.out.println("ServerRouter is Listening on port: 5556.");
		} catch (IOException e) {
			System.err.println("Could not listen on port: 5556.");
			System.exit(1);
		}

		// Creating threads with accepted connections
		while (Running == true) {
			try {
				clientSocket = serverSocket.accept();
				RoutingTable[ind][3] = 
					(new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))).readLine(); // who created the thread
				ServerSRouterSThread t = new ServerSRouterSThread(RoutingTable, clientSocket, ind); // creates a thread with a random port
				t.start(); // starts the thread
				if (RoutingTable[ind][3].equals("Server")) { // only increment if server thread
					ind++; // increments the index
				}
				System.out.println(
						"ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
			} catch (IOException e) {
				System.err.println("Client/Server failed to connect.");
				System.exit(1);
			}	

		} // end while

		// closing connections
		clientSocket.close();
		serverSocket.close();
	}
}