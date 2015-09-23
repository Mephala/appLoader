package appLauncher.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import service.provider.common.request.SPApplicationDto;
import service.provider.common.util.CommonUtils;
import appLauncher.AppLoaderException;
import appLauncher.client.AppLoaderClient;
import appLauncher.conf.ConfigurationManager;
import appLauncher.conf.PersistedConfiguration;
import appLauncher.conf.UserSession;
import appLauncher.downloader.Loader;
import appLauncher.internet.ApplicationUpdateChecker;
import appLauncher.internet.InternetConnectionChecker;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5961991494945610L;
	private JPanel contentPane;
	private final JTextField username;
	private final JPasswordField password;
	private final JCheckBox rememberCheckbox;
	private final static int CONNECTION_TIMEOUT = 3000;
	private JFrame loginFrame = this;
	private final JButton loginButton;
	private final static long APP_CHECK_TIMEOUT = 20000l;
	private static Logger logger = Logger.getLogger(LoginFrame.class);
	private static ProcessingFrame pf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			// Set look and feel first.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (!InternetConnectionChecker.canConnectNecessaryUrlsOverInternet(CONNECTION_TIMEOUT)) {
				JOptionPane.showMessageDialog(null, "Failed to connect Internet to retrieve necessary data", "INTERNET CONNECTION FAILURE", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			// Construct configurations...
			ConfigurationManager.construct();
			// Login if user wants his credentials to be remembered.
			PersistedConfiguration pc = ConfigurationManager.getInstance().getPc();
			if (pc != null) {
				boolean userLoginSuccess = AppLoaderClient.login(pc);
				if (!userLoginSuccess) {
					JOptionPane.showMessageDialog(null, "Failed to login server with previous credentials. Please re-enter your credentials..", "Login Failure", JOptionPane.INFORMATION_MESSAGE);
					constructLoginFrame();
				} else {

					EventQueue.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							pf = new ProcessingFrame();
							ViewUtils.centralizeJFrame(pf);
							pf.setVisible(true);
						}
					});

					List<String> toBeDownloadedAppList = calculateDownloadRequirement();
					if (pf != null) {
						pf.setVisible(false);
						pf.dispose();
					}

					if (!CommonUtils.isEmpty(toBeDownloadedAppList)) {
						for (String app : toBeDownloadedAppList) {
							final String loaderArgs[] = new String[2];
							loaderArgs[0] = app;
							// args[1] = ServiceClient.getServerUrl();
							loaderArgs[1] = "http://localhost:8082/";
							Loader.main(loaderArgs);
						}

					}
					AppSelectorFrame.main(null);
				}
			} else {
				constructLoginFrame();
			}
		} catch (Exception e) {
			logger.error("Class construction exception", e);
		}

	}

	private static List<String> calculateDownloadRequirement() throws InterruptedException, AppLoaderException {
		logger.info("Calculating md5 of the pre-downloaded list.");
		// Check for md5 List.
		ApplicationUpdateChecker applicationUpdateChecker = new ApplicationUpdateChecker();
		applicationUpdateChecker.start();

		applicationUpdateChecker.join(APP_CHECK_TIMEOUT);
		Map<String, String> currentMd5Map = applicationUpdateChecker.getMD5Map();
		UserSession us = UserSession.getInstance();
		List<String> userAppNames = ConfigurationManager.getInstance().getPc().getDownloadedApplications();
		userAppNames.clear();
		List<String> toBeDownlaoded = new ArrayList<String>();
		List<SPApplicationDto> userApps = us.getUserEligibleApps();
		if (userApps == null)
			return toBeDownlaoded;
		for (SPApplicationDto appDto : userApps) {
			String name = appDto.getName();
			userAppNames.add(name);
			if (currentMd5Map.containsKey(name)) {
				String currentMd5 = currentMd5Map.get(name);
				String servermd5 = appDto.getMd5();
				if (!servermd5.equals(currentMd5))
					toBeDownlaoded.add(name);
			} else
				toBeDownlaoded.add(name);
		}
		ConfigurationManager.getInstance().saveConfiguration(ConfigurationManager.getInstance().getPc());
		return toBeDownlaoded;
	}

	private static void constructLoginFrame() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				LoginFrame frame = new LoginFrame();
				frame.setResizable(false);
				ViewUtils.centralizeJFrame(frame);
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(56, 82, 122, 15);
		contentPane.add(lblNewLabel);

		username = new JTextField();
		username.setBounds(196, 80, 173, 19);
		contentPane.add(username);
		username.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(56, 151, 122, 15);
		contentPane.add(lblNewLabel_1);

		password = new JPasswordField();
		password.setBounds(196, 149, 173, 19);
		contentPane.add(password);
		password.setColumns(10);

		loginButton = new JButton("Login");
		loginButton.setBounds(170, 235, 117, 25);
		contentPane.add(loginButton);

		rememberCheckbox = new JCheckBox("Rememember Credentials");
		rememberCheckbox.setSelected(true);
		rememberCheckbox.setBounds(158, 204, 211, 23);
		contentPane.add(rememberCheckbox);

		handleLoginButtonClickEvent();
	}

	private void handleLoginButtonClickEvent() {
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginButton.setEnabled(false);
				try {
					String uname = username.getText();
					String pw = new String(password.getPassword());
					Boolean saveCredentials = rememberCheckbox.isSelected();
					if (Boolean.TRUE.equals(saveCredentials))
						ConfigurationManager.getInstance().saveCredentials(uname, pw);
					AppLoaderClient.login(uname, pw, loginFrame);
					JOptionPane.showMessageDialog(null, "Login Success", "Success", JOptionPane.INFORMATION_MESSAGE);
					// TODO Dispose and open AppLoaderFrame
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(null, "Failed to login.Reason:" + exc.getMessage(), "Login Failure", JOptionPane.ERROR_MESSAGE);
				} finally {
					loginButton.setEnabled(true);
				}
			}
		});
	}
}
