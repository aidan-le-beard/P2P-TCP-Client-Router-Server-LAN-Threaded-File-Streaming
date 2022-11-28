import java.io.*;
import java.net.*;

public class ServerSRouterSThread extends Thread {
	private Object[][] RTable; // routing table
	private PrintWriter out; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String  addr; // communication strings
	private int ind; // indext in the routing table

	// Constructor
	ServerSRouterSThread(Object[][] Table, Socket toClient, int index) throws IOException {
		ind = index; // this thread's spot in the RTable
		out = new PrintWriter(toClient.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
		RTable = Table;
		addr = toClient.getInetAddress().getHostAddress();
		if (("Server").equals((String) RTable[ind][3])) { // only store in RTable if server thread
			RTable[ind][0] = addr; // IP addresses
			RTable[ind][1] = toClient; // sockets for communication
			RTable[ind][2] = ("M" + (ind + 1)); // the "Name" of the node
		} 
	}

	// Run method (will run for each machine that connects to the ServerRouter)
	public void run() {

		try {

			// Server has created the thread: just store info in routing table
			if (("Server").equals((String) RTable[ind][3])) {

				// Initial sends/receives
				System.out.println("Waiting for Client to connect.");
				out.println("Connected to the router."); // confirmation of connection

			// Client has created the thread: send their S-Router back the IP address
			} else {

				System.out.println("Client is connected.");

				String search = in.readLine(); // read the node to search for from Client S-Router

				// continuously loop through server nodes until the wanted server node exists
				long RTableLookupTime = System.nanoTime(); // time RTable lookup starts
				for (int i = 0; i < 100; i++) {
					if (search.equals((String) RTable[i][2])) {
						out.println(Long.toString(System.nanoTime() - RTableLookupTime)); // send RTable lookup time to client SRouter
						out.println(RTable[i][0]); // send wanted server IP to client SRouter
						break;
					}
					if (i == 99) {
						i = -1;
					}
				}
			}

			// close connections
			out.close();
			in.close();
			
		} // end try
		catch (IOException e) {
			System.err.println("Could not listen to socket.");
			System.exit(1);
		}
	}
}