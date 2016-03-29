import java.net.*;
import java.io.*;
import java.util.*;

public class TalkClient
{
	public static void main(String [] args)
	{
		
/*		System.out.println("Please enter a server name");
		String serverName = scanner.next();
		System.out.println("Please enter a port");
		int port = scanner.nextInt();
*/
		String serverName = "localhost";
		int port = 5001;
		Socket serverConnection = makeConnection(serverName, port);
		Thread clientMessageHandler = new ClientMessageHandler(serverConnection);
		clientMessageHandler.start();
		boolean keepGoing = true;
		
		while(keepGoing == true)
		{
			try
			{
				String userMessage = getMessageFromUser();
				if(userMessage.equals("quit"))
				{
					System.out.println("Bye");
					keepGoing = false;
				}
				if(!userMessage.equals(""))
				{
					sendMessage(serverConnection, userMessage);
				}
			}
			catch(Exception e)
			{
				System.out.println("Exception in TalkClient main()");
			}				
		}		
	}
	
	public static Socket makeConnection(String serverName, int port)
	{
		Socket serverConnection = null;
		try
		{

			System.out.println("Connecting to " + serverName + " on port " + port);
			serverConnection = new Socket(serverName, port);
			System.out.println("Just connected to " + serverConnection.getRemoteSocketAddress());
			OutputStream outToServer = serverConnection.getOutputStream();
			DataOutputStream dataOutToServer = new DataOutputStream(outToServer);
			dataOutToServer.writeUTF("Hello from " + serverConnection.getLocalSocketAddress());
			InputStream inFromServer = serverConnection.getInputStream();
			DataInputStream dataIn = new DataInputStream(inFromServer);
			System.out.println("Server says: " + dataIn.readUTF());
		}
		catch(IOException e)
		{
			System.out.println("IOException in TalkClient makeConnection()");
		}
		return serverConnection;
	}
		
	public static String getMessageFromUser()
	{
		Scanner scanner = new Scanner(System.in);
		String message = "";
		try
		{
			System.out.println("Type a message to the chat room");
			if(scanner.hasNextLine())
			{
				message = scanner.nextLine();
			}		
		}
		catch(Exception e)
		{
			System.out.println("Exception in TalkClient getMessageFromUser()");
		}
		return message;	
	}
	
	public static void sendMessage(Socket serverConnection, String message)
	{
		try
		{
			OutputStream outToServer = serverConnection.getOutputStream();
			DataOutputStream dataOutToServer = new DataOutputStream(outToServer);
			dataOutToServer.writeUTF(message);
		}
		catch(IOException e)
		{
			System.out.println("IOException in TalkClient sendMessage()");
		}
	}
	
}