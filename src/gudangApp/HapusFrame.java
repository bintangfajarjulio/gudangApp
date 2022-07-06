package gudangApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HapusFrame extends JFrame {

	String namaBarang;
	String idBarang;

	String loadNama;
	String loadJenis;
	String loadStok;

	public HapusFrame(String value) throws Exception {
		setVisible(true);
		setTitle("Hapus Barang");
		setPreferredSize(new Dimension(300, 120));

		loadXML(value);

		JPanel nama = new JPanel();
		JPanel button = new JPanel();

		JLabel labelNama = new JLabel();
		labelNama.setText(namaBarang);
		nama.add(labelNama);

		JButton buttonTutup = new JButton("Batal");
		buttonTutup.addActionListener(event -> navKatalog());
		button.add(buttonTutup);

		JButton buttonHapus = new JButton("Hapus");
		buttonHapus.addActionListener(event -> {
			try {
				hapusBarang(idBarang);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		button.add(buttonHapus);

		add(nama, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		pack();
	}

	public void navKatalog() {
		EventQueue.invokeLater(() -> {
			JFrame katalogFrame;
			try {
				katalogFrame = new KatalogFrame();
				katalogFrame.setVisible(true);
				katalogFrame.setResizable(false);
				dispose();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		});
	}

	public void hapusBarang(String idBarang) throws Exception, IOException {
		final File file = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");
		Document doc = buildDocument(idBarang);
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
		JOptionPane.showMessageDialog(this, "Barang dihapus");
		navKatalog();
	}

	public Document buildDocument(String idBarang) throws ParserConfigurationException {
		final File akun = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");

		int id = 1;
		int idCheck = Integer.parseInt(idBarang);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setNamespaceAware(true);

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document newDoc = builder.newDocument();
		Element katalogElement = newDoc.createElement("katalog");
		newDoc.appendChild(katalogElement);
		try {
			Document doc = builder.parse(akun);
			NodeList nodeList = doc.getElementsByTagName("barang");
			for (int x = 0, size = nodeList.getLength(); x < size; x++) {
				loadNama = nodeList.item(x).getAttributes().getNamedItem("nama").getNodeValue().toString();
				loadJenis = nodeList.item(x).getAttributes().getNamedItem("jenis").getNodeValue().toString();
				loadStok = nodeList.item(x).getAttributes().getNamedItem("stok").getNodeValue().toString();

				if (idCheck != id) {
					Element element = newDoc.createElement("barang");
					element.setAttribute("id", "" + id);
					element.setAttribute("nama", "" + loadNama);
					element.setAttribute("jenis", "" + loadJenis);
					element.setAttribute("stok", "" + loadStok);
					katalogElement.appendChild(element);
				}
				id++;
			}
			validate();
		} catch (Exception e) {
			System.out.print(e);
		}

		return newDoc;
	}

	public void loadXML(String value) throws Exception {
		final File barang = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		String idCheck;

		try {
			Document doc = builder.parse(barang);
			NodeList nodeList = doc.getElementsByTagName("barang");
			for (int x = 0, size = nodeList.getLength(); x < size; x++) {
				idCheck = nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue().toString();
				if (idCheck.equals(value)) {
					idBarang = nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue().toString();
					namaBarang = nodeList.item(x).getAttributes().getNamedItem("nama").getNodeValue().toString();
				}
			}
		}

		catch (Exception e) {
			System.out.print(e);
		}
	}
}
