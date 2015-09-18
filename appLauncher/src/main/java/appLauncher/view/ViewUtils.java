package appLauncher.view;

import javax.swing.ImageIcon;

public class ViewUtils {

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path, String description) {
		if (path != null) {
			java.net.URL imgURL = ViewUtils.class.getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL, description);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}
		return null;
	}

}
