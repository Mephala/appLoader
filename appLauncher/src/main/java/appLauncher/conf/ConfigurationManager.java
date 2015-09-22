package appLauncher.conf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import appLauncher.AppLoaderException;
import service.provider.common.util.CommonUtils;

public class ConfigurationManager {

	private static ConfigurationManager instance;
	private PersistedConfiguration pc;
	private static final Logger logger = Logger.getLogger(ConfigurationManager.class);
	private final static String PK = "Yska8DqQPok154a8";
	private final static String CONF_FILE = "appLauncher.conf";
	private final static ObjectMapper OM = new ObjectMapper();

	private ConfigurationManager() throws AppLoaderException {
		long start = System.currentTimeMillis();
		logger.info("Initializing Configuration Manager...");
		try {
			File confFile = new File(CONF_FILE);
			if (confFile.exists()) {
				String confEncrypted = FileUtils.readFileToString(confFile);
				String confDecrypted = CommonUtils.decryptStringAES(confEncrypted, PK);
				this.pc = OM.readValue(confDecrypted, PersistedConfiguration.class);
			} else {
				this.pc = null;
			}
			logger.info("Finished initializing Configuration Manager in " + (System.currentTimeMillis() - start) + " ms.");
		} catch (Exception e) {
			logger.error("Failed to initialize Configuration Manager", e);
			throw new AppLoaderException(e, "Failed to retrieve configurations...");
		}
	}

	public PersistedConfiguration getPc() {
		return pc;
	}

	public static synchronized ConfigurationManager getInstance() {
		if (instance == null)
			throw new IllegalStateException("Configuration Manager has to be constructed before retrieval");
		return instance;
	}

	public static synchronized void construct() throws AppLoaderException {
		if (instance == null)
			instance = new ConfigurationManager();
	}

	public void saveCredentials(String uname, String pw) throws JsonGenerationException, JsonMappingException, IOException {
		if (this.pc == null)
			this.pc = new PersistedConfiguration();
		persistConfiguration(uname, pw);
	}

	private void persistConfiguration(String uname, String pw) throws IOException, JsonGenerationException, JsonMappingException {
		pc.setPassword(pw);
		pc.setUsername(uname);
		pc.setSaveConfiguration(true);
		String serializedPc = OM.writeValueAsString(pc);
		File confFile = new File(CONF_FILE);
		if (confFile.exists())
			confFile.delete();
		String encryptPc = CommonUtils.encryptStringAES(serializedPc, PK);
		FileUtils.writeStringToFile(confFile, encryptPc, "UTF-8");
	}

	public void saveConfiguration(PersistedConfiguration pc) throws AppLoaderException {
		long start = System.currentTimeMillis();
		try {
			File confFile = new File(CONF_FILE);
			if (confFile.exists()) {
				confFile.delete();

			}
			String confAsString = OM.writeValueAsString(pc);
			String confDecrypted = CommonUtils.encryptStringAES(confAsString, PK);
			FileUtils.writeStringToFile(confFile, confDecrypted);
			logger.info("Finished initializing Configuration Manager in " + (System.currentTimeMillis() - start) + " ms.");
		} catch (Exception e) {
			throw new AppLoaderException(e, "Failed to save  configurations...");
		}

	}

}
