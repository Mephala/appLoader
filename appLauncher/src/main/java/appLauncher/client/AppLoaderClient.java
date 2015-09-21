package appLauncher.client;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import service.provider.client.executor.ServiceClient;
import service.provider.common.core.RequestApplication;
import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.SPApplicationIntent;
import service.provider.common.dto.SPApplicationUserDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SPApplicationRequestDto;
import service.provider.common.response.SPApplicationResponseDto;
import appLauncher.AppLoaderException;
import appLauncher.conf.PersistedConfiguration;

public class AppLoaderClient {

	static {
		ServiceClient.initialize("http://localhost:8082"); // For local testing.
	}

	private static final Logger logger = Logger.getLogger(AppLoaderClient.class);

	public static boolean login(PersistedConfiguration pc) throws AppLoaderException {
		if (pc == null)
			return false;
		try {
			long start = System.currentTimeMillis();
			String username = pc.getUsername();
			String pw = pc.getPassword();
			SPApplicationRequestDto spAppRequest = RequestDtoFactory.createSPApplicationRequest(RequestApplication.SPA);
			spAppRequest.setIntent(SPApplicationIntent.LOGIN);
			SPApplicationUserDto user = new SPApplicationUserDto();
			user.setUsername(username);
			user.setPassword(pw);
			spAppRequest.setUser(user);
			SPApplicationResponseDto response = ServiceClient.processSPARequest(spAppRequest);
			if (response == null)
				throw new AppLoaderException("Failed to login server. Server did not respond.");
			if (ResponseStatus.OK.equals(response.getResponseStatus())) {
				logger.info("User login success. Login process took " + (System.currentTimeMillis() - start) + " ms.");
				return true;
			}
			return false;
		} catch (AppLoaderException ae) {
			return false;
		} catch (Exception e) {
			logger.error("Failed to login client.", e);
			throw new AppLoaderException("Failed to login server. Reason:" + e.getMessage());
		}
	}

	public static boolean login(String uname, String pw, JFrame frame) throws AppLoaderException {
		PersistedConfiguration pc = new PersistedConfiguration();
		pc.setPassword(pw);
		pc.setUsername(uname);
		return login(pc);
	}

}
