package gudangApp;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class LoginFrame extends JFrame {

	private String loadUsername;
	private String loadPassword;

	public LoginFrame() {
		setVisible(true);
		setTitle("Laman Login");
		// setPreferredSize(new Dimension());

		JPanel panelLogin = new JPanel();
		panelLogin.setLayout(new GridLayout(2, 1));

		JPanel panelJudul = new JPanel();

		JPanel panelUsername = new JPanel();
		JPanel panelPassword = new JPanel();
		JPanel panelButton = new JPanel();

		JLabel labelJudul = new JLabel();
		labelJudul.setText("Akses Gudang");
		panelJudul.add(labelJudul);

		JLabel labelUsername = new JLabel();
		labelUsername.setText("Username");
		panelUsername.add(labelUsername);

		JTextField usernameField = new JTextField(25);
		panelUsername.add(usernameField);

		JLabel labelPassword = new JLabel();
		labelPassword.setText("Password");
		panelPassword.add(labelPassword);

		JTextField passwordField = new JTextField(25);
		panelPassword.add(passwordField);

		JButton buttonLogin = new JButton("Login");
		buttonLogin.addActionListener(event -> {
			try {
				authLogin(usernameField.getText().toString(), passwordField.getText().toString());
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		});
		panelButton.add(buttonLogin);

		JButton buttonDaftar = new JButton("Daftar");
		buttonDaftar.addActionListener(event -> navRegistrasi());
		panelButton.add(buttonDaftar);

		panelLogin.add(panelUsername);
		panelLogin.add(panelPassword);

		add(panelJudul, BorderLayout.NORTH);
		add(panelLogin, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
		pack();
	}

	public void authLogin(String username, String password) throws ParserConfigurationException {
		final File akun = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\akun.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		boolean loginStatus = false;

		try {
			Document doc = builder.parse(akun);
			NodeList nodeList = doc.getElementsByTagName("akun");
			for (int x = 0, size = nodeList.getLength(); x < size; x++) {
				loadUsername = nodeList.item(x).getAttributes().getNamedItem("username").getNodeValue().toString();
				loadPassword = nodeList.item(x).getAttributes().getNamedItem("password").getNodeValue().toString();

				if (username.equals(loadUsername) && password.equals(loadPassword)) {
					JOptionPane.showMessageDialog(this, "Login berhasil");
					loginStatus = true;
					navKatalog();
				}
			}

			if (loginStatus == false)
				JOptionPane.showMessageDialog(this, "Login tidak berhasil");

			validate();
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	private void navRegistrasi() {
		EventQueue.invokeLater(() -> {
			JFrame daftarFrame = new DaftarFrame();
			daftarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			daftarFrame.setResizable(false);
			setVisible(false);
		});
	}

	private void navKatalog() {
		EventQueue.invokeLater(() -> {
			JFrame katalogFrame;
			try {
				katalogFrame = new KatalogFrame();
				katalogFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				katalogFrame.setResizable(false);
				setVisible(false);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		});
	}
}