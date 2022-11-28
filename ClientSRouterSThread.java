import java.io.*;
import java.net.*;

public class ClientSRouterSThread extends Thread {
	private Object[][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in, inFromServerRouter; // reader (for reading from the machine connected to)
	private String addr; // communication strings
	private String destination = "XXX.XXX.X.XX"; // ### WRITE IP OF OTHER S-ROUTER HERE

	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table

	// Constructor
	ClientSRouterSThread(Object[][] Table, Socket toClient, int index) throws IOException {
		ind = index;
		out = new PrintWriter(toClient.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
		RTable = Table;
		addr = toClient.getInetAddress().getHostAddress();
		RTable[ind][0] = addr; // IP addresses
		RTable[ind][1] = toClient; // sockets for communication
		RTable[ind][2] = ("N" + (index + 1)); // The Node's name
	}

	// Run method (will run for each machine that connects to the ServerRouter)
	public void run() {
		try {

			// Initial sends/receives
			System.out.println("Connecting to " + destination);
			out.println("Connected to the router."); // confirmation of connection
			RTable[ind][3] = in.readLine(); //read and store the node trying to be reached from the client

			// waits 10 seconds to let the routing table fill with all machines' information
			try {
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException ie) {
				System.out.println("Thread interrupted");
			}

			// connect with the other S-Router and obtain the IP of the desired node: use the already open thread connection
			try {
				outSocket = new Socket(destination, 5556); // creates a new thread for the connection with the other S-Router
				outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
				inFromServerRouter = new BufferedReader(new InputStreamReader(outSocket.getInputStream())); // creates a reader
			} catch (UnknownHostException e) {
				System.err.println("Don't know about router.");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection.");
				System.exit(1);
			}

			// communication with "Server" S-Router
			outTo.println("Client"); // tells thread this is a client requesting information
			outTo.println(RTable[ind][3]); // tell the server S-Router the node we are looking for
			out.println(inFromServerRouter.readLine()); // receive RTable lookup time and forward to client
			out.println(inFromServerRouter.readLine()); // receive the IP of the node we are looking for and send it to the client

			// closing connections
			outTo.close();
			outSocket.close();
			out.close();
			in.close();

		} // end try
		catch (IOException e) {
			System.err.println("Could not listen to socket.");
			System.exit(1);
		}
	}
}