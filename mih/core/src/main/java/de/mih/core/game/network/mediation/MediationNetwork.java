package de.mih.core.game.network.mediation;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;


import de.mih.core.engine.network.server.datagrams.BaseDatagram;

// This class is a convenient place to keep things common to both the client and server.
public class MediationNetwork {
	static public final int tcpPort = 54555;
	static public final int udpPort = 55555;


	// This registers objects that are going to be sent over the network.
	
	static public class Lobby  extends BaseDatagram {
		public String name;
		public int players;
		public InetAddress address;
		public int tcpPort;
		public int udpPort;
		public int id;
		
		public String toString() {
			return name + " [" + address + "]";
		}
	}
	
	static public class RegisterLobby extends BaseDatagram  {
		public Lobby lobby;
		public int id;
	}
	
	
	static public class UpdateLobbies extends BaseDatagram  {
		public HashMap<Integer, Lobby> lobbies;
	}
	
	static public class RequestLobbyUpdate extends BaseDatagram  {
		
	}
	
	static public class RequestLobbyJoin extends BaseDatagram  {
		public Lobby targetLobby;
		public String sourceAddress;
		public String targetAddress;
	}

	static public class RegisterName extends BaseDatagram  {
		public String name;
	}
	
	static public class ExternalInformation  extends BaseDatagram {
		public InetAddress address;
		public int tcpPort;
		public int udpPort;
	}

	static public class UpdateNames extends BaseDatagram {
		public String[] names;
	}

	static public class ChatMessage {
		public String text;
	}
}