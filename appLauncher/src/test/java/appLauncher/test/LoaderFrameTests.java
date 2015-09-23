package appLauncher.test;

import java.lang.reflect.InvocationTargetException;

import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import service.provider.client.executor.ServiceClient;
import appLauncher.downloader.Loader;

@RunWith(JMockit.class)
public class LoaderFrameTests {

	@Test
	public void testLoadingLoaderFrame() throws InvocationTargetException, InterruptedException {
		ServiceClient.initialize("http://localhost:8082/");
		final String loaderArgs[] = new String[2];
		loaderArgs[0] = "rememberer.jar";
		// args[1] = ServiceClient.getServerUrl();
		loaderArgs[1] = "http://localhost:8082/";
		try {
			Loader.main(loaderArgs);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
