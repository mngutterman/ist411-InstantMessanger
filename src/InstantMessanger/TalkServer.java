import java.net.*;
import java.io.*;
import java.util.*;

public class TalkServer extends Thread
{
	private ServerSocket serverSocket;
	private boolean keepGoing;
	private ArrayList<Socket> clientList;

	public TalkServer(int port) throws IOException
	{
		this.keepGoing = true;
		this.serverSocket = new ServerSocket(port);
		this.clientList = new ArrayList<Socket>();
	}
	
	public static void main(String [] args)
	{
		int port = 5001;

		try
		{
			Thread talkServer = new TalkServer(port);
			talkServer.start();
		}
		catch(IOException e)
		{
			System.out.println("IOException in TalkServer main()");
		}
	}
	
	public void makeConnections()
	{
		while(true)
		{
			try
			{
			
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket client = serverSocket.accept();
				System.out.println("clientCount in TalkServer: " + clientList.size());
				this.clientList.add(client);
				System.out.println("Just connected to " + client.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(client.getInputStream());					
				String message = in.readUTF();
				System.out.println("Message from " + client.getRemoteSocketAddress() + ": " + message);
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				out.writeUTF("You're connected to " + client.getLocalSocketAddress());
				Thread serverMessageHandler = new ServerMessageHandler(clientList.size()-1, this.clientList);
				serverMessageHandler.start();
			}
			catch(IOException e)
			{
				System.out.println("IOException in TalkServer makeConnections()");
				return;
			}
		}
	}

	public void run()
	{		
		this.makeConnections();
		return;
		
	}

}