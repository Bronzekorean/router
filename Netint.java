import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

public class Netint {
	public NetworkInterface net;
	public int port;
	public InetSocketAddress normal_ip;
	public InetSocketAddress broad_ip;
	public DatagramChannel normal;
	public DatagramChannel broad;

	public Netint(NetworkInterface networkInterface, int port) throws IOException {
		net=networkInterface;
		this.port=port;
		for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
			if (interfaceAddress.getAddress() instanceof Inet4Address
					&& !interfaceAddress.getAddress().isLoopbackAddress()) {
				InetSocketAddress socket = new InetSocketAddress(interfaceAddress.getAddress(), port);
				this.normal_ip=socket;
				int prefix = interfaceAddress.getNetworkPrefixLength();
				String CIDR = interfaceAddress.getAddress().toString().substring(1) + "/" + prefix;
				SubnetUtils utils = new SubnetUtils(CIDR);
				InetSocketAddress broad = new InetSocketAddress(utils.getInfo().getBroadcastAddress(), port);
				broad_ip=broad;
				DatagramChannel channel;
				channel = DatagramChannel.open();
				channel.bind(socket);
				channel.socket().setBroadcast(true);
				channel.socket().setSoTimeout(100);
				normal=channel;
				DatagramChannel channel1;
				channel1 = DatagramChannel.open();
				channel1.bind(broad);
				channel1.socket().setBroadcast(true);
				channel1.socket().setSoTimeout(100);
				break;
			}
		}
		// TODO Auto-generated constructor stub
	}

	static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		System.out.println("Display name: " + netint.getDisplayName());
		System.out.println("Name: " + netint.getName());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			System.out.println("InetAddress: " + inetAddress);
		}
	}
	static HashSet<Netint> my_interfaces(int port) throws IOException {
		HashSet<Netint> result= new HashSet<Netint>();
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

		for (NetworkInterface networkInterface : Collections.list(nets)) {
		result.add(new Netint(networkInterface, port));
		}
		return result;
	}
	static HashMap<DatagramChannel, InetSocketAddress> broadcasts() throws IOException {
		HashMap<DatagramChannel, InetSocketAddress> result = new HashMap<DatagramChannel, InetSocketAddress>();
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

		for (NetworkInterface networkInterface : Collections.list(nets)) {
			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				if (interfaceAddress.getAddress() instanceof Inet4Address
						&& !interfaceAddress.getAddress().isLoopbackAddress()) {
					InetSocketAddress socket = new InetSocketAddress(interfaceAddress.getAddress(), 30009);
					DatagramChannel channel;
					channel = DatagramChannel.open();
					channel.bind(socket);
					channel.socket().setBroadcast(true);
					channel.socket().setSoTimeout(100);
					;
					int prefix = interfaceAddress.getNetworkPrefixLength();
					String CIDR = interfaceAddress.getAddress().toString().substring(1) + "/" + prefix;
					SubnetUtils utils = new SubnetUtils(CIDR);
					InetSocketAddress broad = new InetSocketAddress(utils.getInfo().getBroadcastAddress(), 30009);
					result.put(channel, broad);

				}
			}
		}
		return result;
	}

	static HashSet<DatagramChannel> adresses() throws IOException {
		HashSet<DatagramChannel> result = new HashSet<DatagramChannel>();
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

		for (NetworkInterface networkInterface : Collections.list(nets)) {
			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				if (interfaceAddress.getAddress() instanceof Inet4Address
						&& !interfaceAddress.getAddress().isLoopbackAddress()) {
					InetSocketAddress socket = new InetSocketAddress(interfaceAddress.getAddress(), 30009);
					// System.out.println(socket.getAddress());
					DatagramChannel channel;
					channel = DatagramChannel.open();
					channel.bind(socket);
					// channel.socket().setSoTimeout(10);
					result.add(channel);

				}
			}
		}
		return result;
	}

}
