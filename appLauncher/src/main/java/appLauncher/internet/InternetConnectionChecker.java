package appLauncher.internet;

import appLauncher.util.InternetUtils;

public class InternetConnectionChecker {

	/**
	 * Can user has sufficient connection to connect and retrieve data in order to run applications in the launcher.
	 * 
	 * @return
	 */
	public static boolean canConnectNecessaryUrlsOverInternet() {
		// check ip receiving first.
		boolean ipCheck = InternetUtils.canConnectToUrlOverHttp("checkip.amazonaws.com");
		boolean mobileGourmet1 = InternetUtils.canConnectToUrlOverHttp("mobile-gourmet.com");
		boolean mobileGourmet2 = InternetUtils.canConnectToUrlOverHttp("hepsikelepir.net");
		boolean mobileGourmet = mobileGourmet1 || mobileGourmet2;
		return ipCheck && mobileGourmet;
	}

}
