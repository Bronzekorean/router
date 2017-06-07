import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;


public class Message {

	InetSocketAddress from;
	String msg;
	DatagramChannel ch;
	
	Message(InetSocketAddress f, String m, DatagramChannel c){
		
		from = f; 
		msg = m;
		ch = c;
	}

}