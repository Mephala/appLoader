package appLauncher;

public class AppLoaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2670579035625313L;
	private Exception e;

	public AppLoaderException(String msg) {
		super(msg);
	}

	public AppLoaderException(Exception e, String msg) {
		this(msg);
		this.e = e;
	}

	public Exception getE() {
		return e;
	}

}
