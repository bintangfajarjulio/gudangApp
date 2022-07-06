package gudangApp;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class TambahBarangFrame extends JFrame {

	String kategori[] = { "Baju", "Celana", "Sepatu" };

	private String loadNama;
	private String loadJenis;
	private int loadStok;

	public TambahBarangFrame() {
		setVisible(true);
		setTitle("Tambah Barang");
		// setPreferredSize(new Dimension());

		JPanel panelTambah = new JPanel();
		panelTambah.setLayout(new GridLayout(3, 1));

		JPanel panelJudul = new JPanel();

		JPanel panelNama = new JPanel();
		JPanel panelJenis = new JPanel();
		JPanel panelStok = new JPanel();
		JPanel panelButton = new JPanel();

		JLabel labelJudul = new JLabel();
		labelJudul.setText("Masukkan Info Barang");
		labelJudul.setHorizontalAlignment(JLabel.CENTER);
		panelJudul.add(labelJudul);

		JLabel labelNama = new JLabel();
		labelNama.setText("Nama");
		panelNama.add(labelNama);

		JTextField namaField = new JTextField(25);
		panelNama.add(namaField);

		JLabel labelJenis = new JLabel();
		labelJenis.setText("Jenis");
		panelJenis.add(labelJenis);

		JComboBox<String> jenisBox = new JComboBox<>(kategori);
		jenisBox.setPreferredSize(new Dimension(276, 20));
		panelJenis.add(jenisBox);

		JLabel labelStok = new JLabel();
		labelStok.setText("Stok");
		panelStok.add(labelStok);

		JTextField stokField = new JTextField(25);
		panelStok.add(stokField);

		JButton buttonBatal = new JButton("Batal");
		buttonBatal.addActionListener(event -> navKatalog());
		panelButton.add(buttonBatal);

		JButton buttonTambah = new JButton("Tambah");
		buttonTambah.addActionListener(event -> {
			try {
				if (namaField.getText().trim().equals("") || stokField.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(this, "Form tidak boleh kosong");
				} else {
					int value = Integer.parseInt(stokField.getText());
					if (value > 0) {
						String boxValue = jenisBox.getSelectedItem().toString();
						try {
							tambahBarang(namaField.getText().toString(), boxValue, value);
						} catch (NumberFormatException | TransformerException | IOException
								| ParserConfigurationException e) {
							e.printStackTrace();
						}
					}

					else {
						JOptionPane.showMessageDialog(this, "Stok tidak boleh kosong");
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Stok hanya boleh angka");
			}
		});
		panelButton.add(buttonTambah);

		panelTambah.add(panelNama);
		panelTambah.add(panelJenis);
		panelTambah.add(panelStok);

		add(panelJudul, BorderLayout.NORTH);
		add(panelTambah, BorderLayout.CENTER);
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

	public void tambahBarang(String nama, String jenis, int stock)
			throws TransformerException, IOException, ParserConfigurationException {
		final File file = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");
		Document doc = buildDocument(nama, jenis, stock);
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
		JOptionPane.showMessageDialog(this, "Barang ditambahkan");
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
				loadStok = Integer.parseInt(nodeList.item(x).getAttributes().getNamedItem("stok").getNodeValue());

				Element element = newDoc.createElement("barang");
				element.setAttribute("id", "" + id);
				element.setAttribute("nama", "" + loadNama);
				element.setAttribute("jenis", "" + loadJenis);
				element.setAttribute("stok", "" + loadStok);
				katalogElement.appendChild(element);
				id++;
			}
			validate();
		} catch (Exception e) {
			System.out.print(e);
		}

		Element element = newDoc.createElement("barang");
		element.setAttribute("id", "" + id);
		element.setAttribute("nama", "" + nama);
		element.setAttribute("jenis", "" + jenis);
		element.setAttribute("stok", "" + stok);
		katalogElement.appendChild(element);

		return newDoc;
	}
}
