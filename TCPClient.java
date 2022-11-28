import java.io.*;
import java.net.*;

public class TCPClient {
	public static void main(String[] args) throws IOException {

		// Variables for setting up connection and communication
		String routerName = "XXX.XXX.X.XX"; //### PUT CLIENT SERVERROUTER IP HERE
		String nodeWeWant = "M1"; //### PUT THE NODE YOU WISH TO CONNECT TO HERE: INCREMENTS M1, M2, M3, etc...
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		String serverAddress; // the P2P server IP we will directly connect to once provided by SRouter
		int SockNum = 5555; // port number

		// variables for calculations
		long MessagesSent = 0; // count the total # of messages sent
		double TotalMessageSize = 0; // count how many bytes have been sent
		int ConnectionSetupTime = 0;
		long t0, t1 = 0, t;
		int messageSize = 0; // count message size being sent

		// Variables for message passing
		File file = new File("C:/Users/XXX/XXX"); //### PUT FILE PATH OF FILE TO BE TRANSMITTED HERE
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = null;
		byte[] byteBuffer = new byte[2048]; // holds bytes being sent
		String fromServer; // messages received from ServerRouter
		String RTableLookupTime;

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		// Communication process (initial sends/receives)
		fromServer = in.readLine();// initial receive from router (verification of connection)
		t0 = System.currentTimeMillis(); // start counting time from S-Router connection setup
		System.out.println("ServerRouter: " + fromServer);
		out.println(nodeWeWant); // initial send: the server node we want to connect with

		// Receive IP of the node we wish to connect with from S-router
		RTableLookupTime = in.readLine(); // read how long RTable lookup took
		serverAddress = in.readLine();

		// close S-Router communications: we have received the IP we want
		Socket.close();
		out.close();
		in.close();

		// Tries to connect to the peer with the new IP address
		try {
			Socket = new Socket(serverAddress, SockNum);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			outputStream = Socket.getOutputStream();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about router: " + serverAddress);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + serverAddress);
			System.exit(1);
		}

		// receive that connection is successful
		System.out.println(in.readLine());

		// connection with peer successful: calculate total time to connect
		t1 = System.currentTimeMillis();
		t = t1 - t0;
		ConnectionSetupTime = (int) t;

		// Communication while loop
		while ((messageSize = inputStream.read(byteBuffer)) > 0) {
				MessagesSent++;
				TotalMessageSize += messageSize;
				System.out.println("Total Message Size Sent: " + TotalMessageSize);
				outputStream.write(byteBuffer, 0, messageSize);
		}

		// send at end to signify file is finished sending
		byte DoneSending[] = {'D', 'O', 'N', 'E'}; 
        outputStream.write(DoneSending, 0, 4);

		// wait for the file to be fully received
		System.out.println("Server: " + in.readLine());

		t = System.currentTimeMillis() - t1; // calculate total send/receive time

		// print calculations
		System.out.println("Connection setup time (from connecting to S-Router to connecting with peer): " 
				+ ConnectionSetupTime + " ms.\nTotal message size sent: " + TotalMessageSize + 
				" bytes.\nTotal Messages Sent: " + MessagesSent + ".\nAverage message size sent: " + 
				(TotalMessageSize / MessagesSent) + " bytes.\nTotal time from sending to fully received: "
				 + t + "ms.\nBytes Sent Per ms: " + (TotalMessageSize / t) + ".\nRouting Table lookup time: "
				 + RTableLookupTime + " ns.");

		// closing connections
		inputStream.close();
		outputStream.close();
		out.close();
		in.close();
		Socket.close();

		//plays the audio/video file that was sent
		if (! (file.getName().charAt(file.getName().length() - 1) == 't' && 
			file.getName().charAt(file.getName().length() - 2) == 'x' && 
			file.getName().charAt(file.getName().length() - 3) == 't')) {
			ClientApp.run(args);
		}
	}
}