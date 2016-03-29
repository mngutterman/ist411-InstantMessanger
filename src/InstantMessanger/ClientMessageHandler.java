import java.io.*;
import java.net.*;
import java.util.*;
public class ClientMessageHandler extends Thread
{
	private Socket serverConnection;
	public ClientMessageHandler(Socket server)
	{
		this.serverConnection = server;
	}
	
	public static boolean getMessageFromServer(Socket serverConnection)
	{
		String message = "";
		try		
		{
			InputStream inFromServer = serverConnection.getInputStream();
			DataInputStream dataIn = new DataInputStream(inFromServer);
			message = dataIn.readUTF();
			if(!message.equals(""))
			{
				System.out.println(message);
				System.out.println("Type a message to the chat room");
			}
		}
		catch(IOException e)
		{
			System.out.println("IOException in ClientMessageHandler getMessageFromServer()");
			return true;
		}
		return false;
	}
	
	
	public void run()
	{
		boolean done = false;
		while(!done)
		{
			done = getMessageFromServer(this.serverConnection);
		}
	}
}