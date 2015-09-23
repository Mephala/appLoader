package appLauncher.downloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JOptionPane;

import service.provider.client.executor.ServiceClient;
import appLauncher.view.LauncherView;
import appLauncher.view.LoadingData;

public class Loader {

	public static void main(String[] args) throws InterruptedException {
		if (args != null && args.length == 2) {
			final LauncherView launcherView;
			String appName = args[0];
			String serverBaseUrl = args[1];
			try {
				ExecutorService executor = Executors.newCachedThreadPool();
				final LoadingData loadingData = LoadingData.constructLoadingData();
				loadingData.setInfo("Checking internet connection ...");
				Future<LaunchViewStartupResult> launchResultFuture = executor.submit(new Callable<LaunchViewStartupResult>() {
					public LaunchViewStartupResult call() throws Exception {
						LauncherViewLoader viewLoader = new LauncherViewLoader();
						return viewLoader.launchView(loadingData);
					}
				});
				Long totalData = ServiceClient.getFileSizeForDownload(appName);
				boolean connectionSuccess = !totalData.equals(Long.valueOf(-1));
				if (connectionSuccess) {
					while (!launchResultFuture.isDone())
						;
					LaunchViewStartupResult startupResult = launchResultFuture.get();
					if (startupResult.isSuccess) {
						launcherView = startupResult.lw;
						loadingData.setTotalData(totalData);
						loadingData.getDownloadStarted().set(true);
						loadingData.setInfo("Connected. Downloading updates...");
						final String fileName = appName;
						URL link = new URL(serverBaseUrl + appName + "/downloadFile.do");
						InputStream in = new BufferedInputStream(link.openStream());
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						byte[] buf = new byte[1024];
						int n = 0;
						long processData = 0l;
						while (-1 != (n = in.read(buf))) {
							loadingData.setInfo("Downloading app data...");
							loadingData.setProcessedData(processData);
							out.write(buf, 0, n);
							processData += 1024;
						}
						loadingData.setInfo("App is downloaded successfully.");
						loadingData.setProcessedData(totalData);
						out.close();
						in.close();
						byte[] response = out.toByteArray();
						FileOutputStream fos = new FileOutputStream(fileName);
						fos.write(response);
						fos.close();
						// Executors.newCachedThreadPool().submit(new Runnable()
						// {
						// public void run() {
						// try {
						// Runtime.getRuntime().exec("java -jar " + fileName + "
						// " + fileName);
						// } catch (IOException e) {
						// JOptionPane.showMessageDialog(null, "Reeskont");
						// }
						// }
						// });
						launcherView.setVisible(false);
						launcherView.dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Error occured during loader window startup. Error message:" + startupResult.error
								+ ". Contact to Gökhan Özgözen: gokhan.ozgozen@gmail.com", "HATA!!!!", JOptionPane.ERROR_MESSAGE);
						System.exit(-1);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Connection failure. Either you are offline or the servers are down. Contact to Gökhan Özgözen: gokhan.ozgozen@gmail.com", "HATA!!!!",
							JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "HATA!!!!", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		} else {
			throw new IllegalArgumentException("Wrong checkpoint.");
		}
	}
}
