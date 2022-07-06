package gudangApp;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class DaftarFrame extends JFrame {

	private String loadUsername;
	private String loadPassword;

	public DaftarFrame() {
		setVisible(true);
		setTitle("Laman Registration");
		// setPreferredSize(new Dimension());

		JPanel panelLogin = new JPanel();
		panelLogin.setLayout(new GridLayout(2, 1));

		JPanel panelJudul = new JPanel();

		JPanel panelUsername = new JPanel();
		JPanel panelPassword = new JPanel();
		JPanel panelButton = new JPanel();

		JLabel labelJudul = new JLabel();
		labelJudul.setText("Buat akun");
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

		JButton buttonBatal = new JButton("Batal");
		buttonBatal.addActionListener(event -> navLogin());
		panelButton.add(buttonBatal);

		JButton buttonDaftar = new JButton("Daftar");
		buttonDaftar.addActionListener(event -> {
			if (usernameField.getText().trim().equals("") || passwordField.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Form tidak boleh kosong");
			} else {
				try {
					createAccount(usernameField.getText(), passwordField.getText());
				} catch (TransformerFactoryConfigurationError | TransformerException | IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		});
		panelButton.add(buttonDaftar);

		panelLogin.add(panelUsername);
		panelLogin.add(panelPassword);
		panelLogin.add(panelButton);

		add(panelJudul, BorderLayout.NORTH);
		add(panelLogin, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
		pack();
	}

	public void navLogin() {
		EventQueue.invokeLater(() -> {
			JFrame loginFrame = new LoginFrame();
			loginFrame.setVisible(true);
			loginFrame.setResizable(false);
			dispose();
		});
	}

	public void createAccount(String username, String password) throws TransformerFactoryConfigurationError,
			TransformerException, IOException, ParserConfigurationException {

		final File file = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\akun.xml");
		Document doc = buildDocument(username, password);
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
		JOptionPane.showMessageDialog(this, "Akun berhasil dibuat");
		navLogin();
	}

	public Document buildDocument(String username, String password) throws ParserConfigurationException {
		final File akun = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\akun.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setNamespaceAware(true);

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document newDoc = builder.newDocument();
		Element regElement = newDoc.createElement("register");
		newDoc.appendChild(regElement);

		try {
			Document doc = builder.parse(akun);
			NodeList nodeList = doc.getElementsByTagName("akun");
			for (int x = 0, size = nodeList.getLength(); x < size; x++) {
				loadUsername = nodeList.item(x).getAttributes().getNamedItem("username").getNodeValue().toString();
				loadPassword = nodeList.item(x).getAttributes().getNamedItem("password").getNodeValue().toString();
				Element element = newDoc.createElement("akun");
				element.setAttribute("password", "" + loadPassword);
				element.setAttribute("username", "" + loadUsername);
				regElement.appendChild(element);
			}
			validate();
		} catch (Exception e) {
			System.out.print(e);
		}

		Element element = newDoc.createElement("akun");
		element.setAttribute("password", "" + password);
		element.setAttribute("username", "" + username);
		regElement.appendChild(element);

		return newDoc;
	}
}
