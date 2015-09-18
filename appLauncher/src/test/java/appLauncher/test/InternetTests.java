package appLauncher.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class InternetTests {

	@Test
	public void testConnection() throws IOException {

		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("checkip.amazonaws.com", 80), 3000);
		socket.close();
		socket = new Socket();
		socket.connect(new InetSocketAddress("46.196.100.145", 80), 3000);
		socket.close();
		socket = new Socket();
		socket.connect(new InetSocketAddress("hepsikelepir.net", 80), 3000);
		socket.close();
		socket = new Socket();
		socket.connect(new InetSocketAddress("mobile-gourmet.com", 80), 3000);
		socket.close();
	}

}
