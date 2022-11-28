import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String[] args) throws IOException {

		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading from ServerRouter
		String routerName = "XXX.XXX.X.XX"; //### PUT SERVER SERVERROUTER IP HERE
		int SockNum = 5556; // port number for connection

		// Variables to verify transmission
		int messageSize = 0; // calculates how many bytes are received with each message
		long TotalMessageSize = 0; // calculates the total message size received

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			out.println("Server"); // let the S-Router know what kind of thread this is
		} catch (UnknownHostException e) {
			System.err.println("Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		// Variables for message passing and file writing
		InputStream inputStream; // reads the output from the ServerRouter
		byte[] byteBuffer = new byte[2048]; // holds the incoming byte stream
		File file = new File("C:/Users/XXX/XXX"); //### PUT PATH TO WHERE RECEIVED FILE SHOULD BE WRITTEN HERE
		OutputStream outputStream = new FileOutputStream(file); 

		// Communication process (initial sends/receives)
		System.out.println("ServerRouter: " + in.readLine()); // verification info received into RTable

		// close finished connections
		in.close();
		out.close();
		Socket.close();

		// begin to listen for the client peer connection
		ServerSocket serverSocket = new ServerSocket(5555);
		Socket = serverSocket.accept();
		out = new PrintWriter(Socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
		inputStream = Socket.getInputStream();
		
		// let other peer know connection is successful
		out.println("Connection to peer successful.");

		// Communication while loop
			while ((messageSize = inputStream.read(byteBuffer)) > 0) {
				if (messageSize >= 4) {
					if (byteBuffer[messageSize - 4] == 'D' && byteBuffer[messageSize - 3] == 'O' && 
						byteBuffer[messageSize - 2] == 'N' && byteBuffer[messageSize - 1] == 'E') {
						outputStream.write(byteBuffer, 0, messageSize - 4);
						TotalMessageSize += messageSize - 4;
						System.out.println("Total Message Size Received: " + TotalMessageSize);
						break;
					}
				}
				TotalMessageSize += messageSize;
				System.out.println("Total Message Size Received: " + TotalMessageSize);
				outputStream.write(byteBuffer, 0, messageSize);
			}

		// acknowledge to the client all data has been received
		out.println("Success.");
			
		// closing connections
		inputStream.close();
		outputStream.close();
		out.close();
		in.close();
		Socket.close();
		serverSocket.close();

		// plays the audio/video file after being received
		if (! (file.getName().charAt(file.getName().length() - 1) == 't' && 
            file.getName().charAt(file.getName().length() - 2) == 'x' && 
            file.getName().charAt(file.getName().length() - 3) == 't')) {
            ServerApp.run(args);
        }		
	}
}