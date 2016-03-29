import java.io.*;
import java.net.*;
import java.util.*;
public class ServerMessageHandler extends Thread
{
	ArrayList<Socket> clientList;
	Socket clientConnection;
	int clientNumber;
	String message = "";
	public ServerMessageHandler(int clientNum, ArrayList<Socket> cList)
	{
		this.clientList = cList;
		this.clientConnection = this.clientList.get(clientNum);
		this.clientNumber = this.clientList.indexOf(clientConnection);
		System.out.println("Index of this ServerMessageHandler in clientList: " + clientNumber);
	}
	
	public void listenForMessage()
	{
		boolean keepGoing = true;
		System.out.println("clientCount in listenForMessage: " + this.clientList.size());
		while(keepGoing)
		{
			try
			{
				DataInputStream dataInputStream = new DataInputStream(this.clientConnection.getInputStream());
				message = dataInputStream.readUTF();
				System.out.println("Message from " + this.clientConnection.getRemoteSocketAddress() + ": " + message);
				if(message.equals("quit"))
				{
					killConnection();
					return;
				}
				if(!message.equals(""))
				{
					System.out.println("About to send message: " + message);
					this.sendMessage(message);
					message = "";
				}
			}
			catch(IOException e)
			{
				System.out.println("IOException in ServerMessageHandler listenForMessage()");
			}
		}
	}
	
	public void sendMessage(String message)
	{
		System.out.println("clientCount in sendMessage: " + this.clientList.size());
		try
		{
			for(int k = 0; k < clientList.size(); k++)
			{
				System.out.println("In for loop in SendMessage()");
				DataOutputStream dataOutToClient = new DataOutputStream(this.clientList.get(k).getOutputStream());
				System.out.println("Message from " + this.clientConnection.getRemoteSocketAddress() + ": " + message);
				dataOutToClient.writeUTF("Message from " + this.clientConnection.getRemoteSocketAddress() + ": " + message);
			}
		}
		catch(IOException e)
		{
			System.out.println("IOException in ServerMessageHandler sendMessage()");
		}
		return;
	}
	
	public void killConnection()
	{
		try
		{
			this.clientConnection.close();
			this.clientList.remove(this.clientList.indexOf(clientConnection));
		}
		catch(IOException e)
		{
			System.out.println("IOException in ServerMessageHandler killConnection()");
		}
		return;
		
	}
	
	public void run()
	{
		this.listenForMessage();
		return;
	}
}