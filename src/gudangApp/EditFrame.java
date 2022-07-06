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

public class EditFrame extends JFrame {

	String kategori[] = { "Baju", "Celana", "Sepatu" };

	private String loadNama;
	private String loadJenis;
	private String loadStok;
	private int loadId;

	public EditFrame(String value) throws Exception {
		setVisible(true);
		setTitle("Edit Info Barang");
		// setPreferredSize(new Dimension());

		loadId = Integer.parseInt(value);
		loadXML(value);

		JPanel panelEdit = new JPanel();
		panelEdit.setLayout(new GridLayout(3, 1));

		JPanel panelJudul = new JPanel();
		JPanel panelNama = new JPanel();
		JPanel panelJenis = new JPanel();
		JPanel panelStok = new JPanel();
		JPanel panelButton = new JPanel();

		JLabel labelJudul = new JLabel();
		labelJudul.setText("Edit Info Barang");
		labelJudul.setHorizontalAlignment(JLabel.CENTER);
		panelJudul.add(labelJudul);

		JLabel labelNama = new JLabel();
		labelNama.setText("Nama");
		panelNama.add(labelNama);

		JTextField namaField = new JTextField(25);
		namaField.setText(loadNama);
		panelNama.add(namaField);

		JLabel labelJenis = new JLabel();
		labelJenis.setText("Jenis");
		panelJenis.add(labelJenis);

		JComboBox<String> jenisBox = new JComboBox<>(kategori);
		jenisBox.setPreferredSize(new Dimension(276, 20));

		for (int i = 0; i < kategori.length; i++) {
			if (kategori[i].equals(loadJenis))
				jenisBox.setSelectedIndex(i);
		}

		panelJenis.add(jenisBox);

		JLabel labelStok = new JLabel();
		labelStok.setText("Stok");
		panelStok.add(labelStok);

		JTextField stokField = new JTextField(25);
		stokField.setText(loadStok);
		panelStok.add(stokField);

		JButton buttonBatal = new JButton("Batal");
		buttonBatal.addActionListener(event -> navKatalog());
		panelButton.add(buttonBatal);

		JButton buttonEdit = new JButton("Edit");
		buttonEdit.addActionListener(event -> {
			try {
				if (namaField.getText().trim().equals("") || stokField.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(this, "Form tidak boleh kosong");
				}

				else {
					int valueStok = Integer.parseInt(stokField.getText());

					if (valueStok >= 0) {
						String boxValue = jenisBox.getSelectedItem().toString();
						editBarang(namaField.getText().toString(), boxValue, valueStok);
					}

					else {
						JOptionPane.showMessageDialog(this, "Stok tidak boleh minus");
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Stok hanya boleh angka");
			}
		});

		panelButton.add(buttonEdit);
		panelEdit.add(panelNama);
		panelEdit.add(panelJenis);
		panelEdit.add(panelStok);

		add(panelJudul, BorderLayout.NORTH);
		add(panelEdit, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
		pack();
	}

	public void navKatalog() {
		EventQueue.invokeLater(() -> {
			JFrame katalogFrame;
			try {
				katalogFrame = new KatalogFrame();
				katalogFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				katalogFrame.setResizable(false);
				dispose();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		});
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
					loadNama = nodeList.item(x).getAttributes().getNamedItem("nama").getNodeValue().toString();
					loadJenis = nodeList.item(x).getAttributes().getNamedItem("jenis").getNodeValue().toString();
					loadStok = nodeList.item(x).getAttributes().getNamedItem("stok").getNodeValue().toString();
				}
			}
		}

		catch (Exception e) {
			System.out.print(e);
		}
	}

	public void editBarang(String nama, String jenis, int stok) throws Exception {
		final File file = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");
		Document doc = buildDocument(nama, jenis, stok);
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
		JOptionPane.showMessageDialog(this, "Info diperbarui");
		navKatalog();
	}

	public Document buildDocument(String nama, String jenis, int stok) throws ParserConfigurationException {
		final File akun = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");
		int id = 1;

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

				if (id == loadId) {
					Element element = newDoc.createElement("barang");
					element.setAttribute("id", "" + id);
					element.setAttribute("nama", "" + nama);
					element.setAttribute("jenis", "" + jenis);
					element.setAttribute("stok", "" + stok);
					katalogElement.appendChild(element);
				}

				else {
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
}
