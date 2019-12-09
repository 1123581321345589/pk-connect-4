package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;

/* Thread Class for each incoming client */
public class ClientThread implements Runnable {

	/* The socket of the client */
	private Socket clientSocket;
	/* Server class from which thread was called */
	private Server baseServer;
	private BufferedReader incomingMessageReader;
	private PrintWriter outgoingMessageWriter;
	/* The name of the client */
	private String clientName;
        
        private String[] opcode = new String[3];

	public ClientThread(Socket clientSocket, Server baseServer) {
		this.setClientSocket(clientSocket);
		this.baseServer = baseServer;
		try {
			/*
			 * Reader to get all incoming messages that the client passes to the
			 * server
			 */
			incomingMessageReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			/* Writer to write outgoing messages from the server to the client */
			outgoingMessageWriter = new PrintWriter(
					clientSocket.getOutputStream(), true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			this.clientName = getClientNameFromNetwork();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					baseServer.clientNames.add(clientName + " - "
							+ clientSocket.getRemoteSocketAddress());
				}

			});
			String inputToServer;
			while (true) {
				inputToServer = incomingMessageReader.readLine();
                                messageDecoder(inputToServer);
                                System.out.println("Now we're in the client thread with:\n" + inputToServer);
				baseServer.writeToAllSockets(inputToServer);
			}
		} catch (SocketException e) {
			baseServer.clientDisconnected(this);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
          //Break down messages based on game protocol
        public void messageDecoder(String msg){
            opcode = new String[]{"", "", ""};
            
            char[] code = msg.toCharArray();
            int arrInd = 0;
            boolean[] checks = new boolean[]{true, true};
            
            int ocInd = 0;
            while(checks[0]){
                if(code[arrInd] != ';'){
                    opcode[ocInd] += code[arrInd];
                }
                else{
                    checks[0] = false;
                }
                arrInd++;
            }
            
            ocInd++;
            while(checks[1]){
                if(code[arrInd] != ';'){
                    opcode[ocInd] += code[arrInd];
                }
                else{
                    checks[1] = false;
                }
                arrInd++;
            }
            
            ocInd++;
            for(; arrInd < msg.length(); arrInd++){
                opcode[ocInd] += code[arrInd];
            }
            
            System.out.println("opcode[0] = "+opcode[0]);
            System.out.println("opcode[1] = "+opcode[1]);
            System.out.println("opcode[2] = "+opcode[2]);
        }

        
	public void writeToServer(String input) {
		outgoingMessageWriter.println(input);
	}

	public String getClientNameFromNetwork() throws IOException {
		/*
		 * Get the name of the client, which is the first data transaction the
		 * server-client make
		 */
		return incomingMessageReader.readLine();
	}

	public String getClientName() {
		return this.clientName;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}
