package appLauncher.conf;

import java.util.List;

import service.provider.common.request.SPApplicationDto;

public class UserSession {

	private List<SPApplicationDto> userEligibleApps;

	private static UserSession instance;

	private UserSession() {

	}

	public static synchronized UserSession getInstance() {
		if (instance == null)
			instance = new UserSession();
		return instance;
	}

	public List<SPApplicationDto> getUserEligibleApps() {
		return userEligibleApps;
	}

	public void setUserEligibleApps(List<SPApplicationDto> userEligibleApps) {
		this.userEligibleApps = userEligibleApps;
	}

}
