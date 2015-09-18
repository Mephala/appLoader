package appLauncher.internet;

import appLauncher.util.InternetUtils;

public class InternetConnectionChecker {

	/**
	 * Can user has sufficient connection to connect and retrieve data in order to run applications in the launcher.
	 * 
	 * @return
	 */
	public static boolean canConnectNecessaryUrlsOverInternet(int timeout) {
		// check ip receiving first.
		boolean ipCheck = InternetUtils.canConnectToUrlOverHttp("checkip.amazonaws.com",timeout);
		boolean mobileGourmet1 = InternetUtils.canConnectToUrlOverHttp("mobile-gourmet.com",timeout);
		boolean mobileGourmet2 = InternetUtils.canConnectToUrlOverHttp("hepsikelepir.net",timeout);
		boolean mobileGourmet = mobileGourmet1 || mobileGourmet2;
		return ipCheck && mobileGourmet;
	}

}
