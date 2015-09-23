package appLauncher.downloader;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import appLauncher.view.LauncherView;
import appLauncher.view.LoadingData;
import appLauncher.view.ViewUtils;

class LauncherViewLoader {
	LaunchViewStartupResult result;

	protected LauncherViewLoader() {

	}

	LaunchViewStartupResult launchView(final LoadingData loadingData) {
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					try {
						result = new LaunchViewStartupResult();
						result.lw = new LauncherView(loadingData);
						ViewUtils.centralizeJFrame(result.lw);
						result.isSuccess = true;
					} catch (Exception e) {
						result.isSuccess = false;
						result.error = e.getMessage();
					}

				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
