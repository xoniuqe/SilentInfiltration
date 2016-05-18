package de.mih.core.engine.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.mih.core.engine.network.mediation.MediationNetwork;
import de.mih.core.engine.network.mediation.MediationNetwork.RequestLobbyJoin;
import de.mih.core.engine.network.server.datagrams.AckDatagram;
import de.mih.core.engine.network.server.datagrams.BaseDatagram;
import de.mih.core.engine.network.server.datagrams.ChatDatagram;
import de.mih.core.engine.network.server.datagrams.ConnectApprove;
import de.mih.core.engine.network.server.datagrams.ConnectRequest;
import de.mih.core.engine.network.server.datagrams.DisconnectDatagram;

public class UDPClient extends UDPBase
{
	// InetAddress server;
	// int port;
	// DatagramSocket clientSocket;
	// DatagramReceiveHandler receiveHandler;
	// ExecutorService threadPool;
	// private boolean isRunning;
	Thread receiverThread;
	// int sequenceNumber;
	Connection serverConnection;
	Thread packetLoss;

	public UDPClient(String ip, int port) throws UnknownHostException, SocketException
	{
		super();
		this.maxConnections = 1;
		serverConnection = new Connection(InetAddress.getByName(ip), port);
	}

	// long lastPacket = 0;
	public void start() throws IOException
	{
		isRunning = true;
		receiverThread = new Thread("receiver") {

			public void run()
			{
				while (!Thread.interrupted() && isRunning)
				{
					try
					{
						DatagramPacket receivePacket = receivePacket();
						BaseDatagram datagram = Serialization.deserializeDatagram(receivePacket.getData());
						InetSocketAddress socketAddress = (InetSocketAddress) receivePacket.getSocketAddress();

						if (!socketAddress.getAddress().equals(serverConnection.getIP()))
						{
							// Drop packets that do not come from the known
							// server
							continue;
						}
						if (datagram.reliable)
						{
							// this.receivedReliablePackets.push(datagram.sequenceNumber);
							AckDatagram ack = new AckDatagram();
							ack.responseID = datagram.sequenceNumber;
							System.out.println("Sending ack for " + ack.responseID);
							sendTo(serverConnection, ack, false);
						}
						if (datagram instanceof AckDatagram)
						{
							AckDatagram ackDatagram = (AckDatagram) datagram;
							System.out.println("Received ack for " + ackDatagram.responseID);
							serverConnection.removeAcknowledged(ackDatagram.responseID);
							continue;
						}
						else if (datagram instanceof DisconnectDatagram)
						{
							executeDisconnectHandler(serverConnection);
						}
						else if (datagram instanceof ConnectApprove)
						{
							executeConnectHandler(serverConnection);
						}
						// Ignores too old packets
						if (datagram.sequenceNumber > serverConnection.getRemoteSequence())
						{
							executeReceiveHandler(serverConnection, datagram);
						}
						serverConnection.updateRemoteSequence(datagram.sequenceNumber);
						checkConnectionTimeouts();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		receiverThread.start();
		sendData(new ConnectRequest(), true);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String sentence = inFromUser.readLine();
		while (!sentence.equals("exit"))
		{
			ChatDatagram chatMessage = new ChatDatagram();
			chatMessage.message = sentence;
			sendData(chatMessage, true);
			sentence = inFromUser.readLine();
		}
		// packetLoss = new Thread("lostpackets"){
		// public void run()
		// {
		// while (!Thread.interrupted() && isRunning)
		// {
		// try
		// {
		// System.out.println("Checking packets: ");
		// serverConnection.checkLostPackets();
		// Thread.sleep(500);
		// }
		// catch (InterruptedException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		// };
		// packetLoss.start();
	}

	public void close()
	{
		isRunning = false;
		receiverThread.stop();
		socket.close();
	}

	public void stop()
	{
		this.close();
	}

	public static void main(String args[]) throws Exception
	{

		UDPClient client = new UDPClient("localhost", 19876);
		client.setDatagramReceiveHandler(new DatagramReceiveHandler() {

			@Override
			public void receive(Connection connection, BaseDatagram datagram)
			{
				if (datagram instanceof ChatDatagram)
				{
					System.out.println("FROM SERVER: " + ((ChatDatagram) datagram).message);
				}
			}

			@Override
			public void connected(Connection connection)
			{
				// TODO Auto-generated method stub
				System.out.println("connected");
				System.out.println(connection.toString());

			}

			@Override
			public void disconnected(Connection connection)
			{
				// TODO Auto-generated method stub
				System.out.println("disconnected");
				System.out.println(connection.toString());
			}
		});
		client.start();

	}

	public Connection getServerConnection()
	{
		return serverConnection;
	}

	public void sendData(BaseDatagram data, boolean reliable)
	{
		this.sendTo(serverConnection, data, reliable);
	}

}
