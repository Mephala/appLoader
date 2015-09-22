package appLauncher.conf;

import java.util.List;

public class PersistedConfiguration {

	private String username;
	private String password;
	private Boolean saveConfiguration;
	private List<String> downloadedApplications;

	public PersistedConfiguration() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((saveConfiguration == null) ? 0 : saveConfiguration.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistedConfiguration other = (PersistedConfiguration) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (saveConfiguration == null) {
			if (other.saveConfiguration != null)
				return false;
		} else if (!saveConfiguration.equals(other.saveConfiguration))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getSaveConfiguration() {
		return saveConfiguration;
	}

	public void setSaveConfiguration(Boolean saveConfiguration) {
		this.saveConfiguration = saveConfiguration;
	}

	public List<String> getDownloadedApplications() {
		return downloadedApplications;
	}

	public void setDownloadedApplications(List<String> downloadedApplications) {
		this.downloadedApplications = downloadedApplications;
	}

}
