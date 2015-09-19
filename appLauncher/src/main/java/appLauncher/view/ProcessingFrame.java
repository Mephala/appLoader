package appLauncher.view;

import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ProcessingFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6376313331420630844L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProcessingFrame frame = new ProcessingFrame();
					ViewUtils.centralizeJFrame(frame);
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
	public ProcessingFrame() {
		setTitle("Processing..."); // Generic frame for processing jobs.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 226, 294);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Icon loadingIcon = ViewUtils.createImageIcon("/loading_spinner.gif", "Masrik");
		JLabel lblNewLabel = new JLabel(loadingIcon);
		lblNewLabel.setBounds(10, 11, 200, 211);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 242, 200, 14);
		contentPane.add(lblNewLabel_1);
	}
}
