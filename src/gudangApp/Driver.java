package gudangApp;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Driver {
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(() -> {
			JFrame loginFrame = new LoginFrame();
			loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			loginFrame.setResizable(false);
		});
	}
}
