package appLauncher.internet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import service.provider.common.util.CommonUtils;
import appLauncher.conf.ConfigurationManager;
import appLauncher.conf.PersistedConfiguration;

public class ApplicationUpdateChecker extends Thread {

	private static Logger logger = Logger.getLogger(ApplicationUpdateChecker.class);
	private static Map<String, String> appNameToMd5 = new HashMap<String, String>();

	public ApplicationUpdateChecker() {
		super(new Runnable() {

			@Override
			public void run() {
				try {
					PersistedConfiguration pc = ConfigurationManager.getInstance().getPc();
					if (pc == null)
						return;
					List<String> appPaths = pc.getDownloadedApplications();
					if (appPaths == null || appPaths.size() == 0)
						return;
					for (String path : appPaths) {
						File app = new File(path);
						byte[] appData = FileUtils.readFileToByteArray(app);
						String md5 = CommonUtils.md5(appData);
						appNameToMd5.put(path, md5);
					}
				} catch (Exception e) {
					logger.error("Failed to run application update.", e);
				}

			}
		});
	}

	public Map<String, String> getMD5Map() {
		Map<String, String> tmp = new HashMap<String, String>();
		if (appNameToMd5.size() == 0)
			return tmp;
		Set<String> appkey = appNameToMd5.keySet();
		for (String string : appkey) {
			tmp.put(string, appNameToMd5.get(string));
		}
		return tmp;
	}

}
