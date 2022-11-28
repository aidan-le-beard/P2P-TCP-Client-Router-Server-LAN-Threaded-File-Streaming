# P2P TCP Client-Router-Server LAN Threaded File-Streaming

## You do NOT have permission to use this code for any schoolwork purposes under any circumstances. 

## You do NOT have permission to use this code for any commercial purposes without speaking to me to work out a deal.

This program creates a P2P (peer-to-peer) client-server TCP LAN connection, where the client and server do not know each other's IP. Their connection is facilitated by 2 respective routers ("server-router," "S-Router"), requiring a total of 4 different machines. The 2 S-Routers serve to connect the Client and Server, immediately threading any connections, and after the client and server are connected, immediately end the connection to the S-Router, while continuing to wait for further clients and servers to connect. The client then streams a file to the server directly: the peer-to-peer connection. The client outputs how many bytes it has sent, while the server outputs how many bytes it has received, to verify that the total file has been sent. Finally, if the file is not a .txt file, the program attempts to open and play the file in JavaFX on both the client and server machines, to verify transmission. Connection setup time, routing table lookup time, and bytes sent per
millisecond are also calulated and printed.

## Network Diagram:
![image](https://user-images.githubusercontent.com/33675444/204374820-56a14a65-f2d2-45c4-8a4e-d7a04fd5932c.png)
![image](https://user-images.githubusercontent.com/33675444/204375135-4c87a129-15ad-40ad-aa1b-384b153df5c3.png)


## THIS PROGRAM REQUIRES JAVAFX TO RUN
### To run WITHOUT JavaFX, or if playing video/audio is not wanted/required:
1) Delete ServerApp.java and ClientApp.java
2) Comment out or delete lines 114-118 in TCPClient.java
3) Comment out or delete lines 84-88 in TCPServer.java

### To run WITH JavaFX, if wanted: Guide to Install JavaFX in VSCode, if JavaFX is not already installed:
1) Download JavaFX from https://gluonhq.com/products/javafx/ for the proper operating system on both the Client and the Server machines. 
2) Unzip the file. 
3) In VSCode, under “Java Projects” in the bottom left corner, click “Referenced Libraries,” click the “+” symbol, navigate to the lib folder in the unzipped file, highlight all of the files, and hit enter. 
4) Go to “Run,” press “Add Configurations,” and in the “launch.json” file, at the bottom after the final “projectName,” add a “,” to the end of the line, press enter, and copy-paste or type the following: "vmArgs": "--module-path \"C:/Users/XXX/XXX/javafx-sdk-17.0.2/lib\" --add-modules javafx.controls,javafx.fxml,javafx.media"
5) Replace the path above with the path to the location of the downloaded JavaFX lib folder.

### To execute on Windows:

Knowledge of Java IDE usage and Java code execution is assumed.

1) Place ClientApp.java (if using JavaFX) and TCPClient.java on one computer.
2) Place ServerApp.java (if using JavaFX) and TCPServer.java on a second computer that is on the same Wi-Fi network.
3) Place ClientSRouterSThread.java and ClientTCPServerRouter.java on a third computer that is on the same Wi-Fi network.
4) Place ServerTCPServerRouter.java and ServerSRouterSThread.java on a fourth computer that is on the same Wi-Fi network.
5) Edit ClientSRouterSThread.java line 9: put the IP of the machine with ServerTCPServerRouter.java and ServerSRouterSThread.java on it. 
   #### Note: IP can be found on the other machine in command prompt by typing “ipconfig” and retrieving the IPv4 Address from the section titled “Wireless LAN adapter Wi-Fi.”
6) Edit TCPClient.java line 8: put the IP of the machine with ClientSRouterSThread.java and ClientTCPServerRouter.java on it.
7) **Optional (for multiple connections):** If only connecting 1 client-server pair, this step is not necessary. If connecting more: edit TCPClient.java line 9: put the number of the node you wish to connect to: starts at M1, followed by M2, M3, etc. So if trying to connect to the 4th server you have connected, it would be M4, for example.
8) Edit TCPClient.java line 24: put the file path to the file that you wish for the client to transmit here. This file will be sent to the server.
9) Edit TCPServer.java line 11: put the IP of the machine with ServerTCPServerRouter.java and ServerSRouterSThread.java on it.
10) Edit TCPServer.java line 35: put the file path to where the server should save the file received from the client.
12) Run ClientTCPServerRouter.java and ServerTCPServerRouter.java first.
13) Run TCPServer.java second.
14) Run TCPClient.java third.
15) **Optional:** Repeat steps 13-14 for other Server/Client pairs that are prepped to connect using the same Server Routers. If connecting more pairs, make sure to follow step 7, changing the line to "M2" for the 2nd pair, "M3" for the 3rd pair, etc.
