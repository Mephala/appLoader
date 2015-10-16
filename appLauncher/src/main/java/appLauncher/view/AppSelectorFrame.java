package appLauncher.view;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import appLauncher.conf.ConfigurationManager;

public class AppSelectorFrame extends JFrame {

	private JPanel contentPane;
	AppSelectorFrame frame = this;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppSelectorFrame frame = new AppSelectorFrame();
					ViewUtils.centralizeJFrame(frame);
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppSelectorFrame() {
		setTitle("App Selection Frame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		List<String> applications = ConfigurationManager.getInstance().getPc().getDownloadedApplications();
		final JComboBox<String> comboBox = new JComboBox<String>();
		for (String app : applications) {
			comboBox.addItem(app);
		}
		comboBox.setBounds(12, 51, 418, 24);
		contentPane.add(comboBox);

		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String app = (String) comboBox.getSelectedItem();
				ProcessBuilder pb = new ProcessBuilder("java", "-jar", app);
				try {
					frame.setVisible(false);
					final Process p = pb.start();
					Thread loggerThread = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								final File logFile = new File(app + ".log");
								if (logFile.exists())
									logFile.delete();
								InputStream is = p.getInputStream();
								if (is != null) {
									byte[] buffer = new byte[1024];
									FileOutputStream fos = new FileOutputStream(logFile);
									while (-1 != is.read(buffer)) {
										fos.write(buffer);
									}
									is.close();
									fos.close();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
					loggerThread.start();
					int exitValue = p.waitFor();
					System.out.println(exitValue);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					frame.setVisible(true);
				}
			}
		});
		btnNewButton.setBounds(77, 117, 117, 25);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Read Logs");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					final String app = (String) comboBox.getSelectedItem();
					File logFile = new File(app + ".log");
					if (!logFile.exists()) {
						JOptionPane.showMessageDialog(null, "No log file generated. Application either does not log, or has not run yet.", "No log file found.",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Desktop.getDesktop().open(logFile);
				} catch (Exception excp) {
					excp.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(265, 117, 117, 25);
		contentPane.add(btnNewButton_1);
	}
}
