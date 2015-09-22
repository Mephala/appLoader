package appLauncher.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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
				String app = (String) comboBox.getSelectedItem();
				ProcessBuilder pb = new ProcessBuilder("java", "-jar", app);
				try {
					frame.setVisible(false);
					Process p = pb.start();
					p.waitFor();
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
		btnNewButton.setBounds(159, 116, 117, 25);
		contentPane.add(btnNewButton);
	}
}
