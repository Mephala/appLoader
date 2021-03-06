package appLauncher.util;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

public class InternetUtils {

	private static final Logger logger = Logger.getLogger(InternetUtils.class);

	/**
	 * boolean connectStatus = canConnectToUrlOverHttp("google.com") || canConnectToUrlOverHttp("46.196.100.145");
	 * 
	 * @param httoAddressOrIp
	 * @param timeout 
	 * @return
	 */
	public static boolean canConnectToUrlOverHttp(String httoAddressOrIp, int timeout) {
		boolean canConnect = false;
		Integer port = 80;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(httoAddressOrIp, port), timeout);
			socket.close();
			canConnect = true;
		} catch (Exception e) {
			logger.error("Failed to connect url:" + httoAddressOrIp + " + port:" + port, e);
		}
		return canConnect;
	}

}
