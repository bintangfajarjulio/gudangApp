package gudangApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class InfoFrame extends JFrame {

	private String id;
	private String nama;
	private String jenis;
	private String stok;

	public InfoFrame(String value) throws Exception {
		setVisible(true);
		setTitle("Info Barang");
		setPreferredSize(new Dimension(300, 200));

		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new GridLayout(4, 1));

		loadXML(value);

		JPanel panelJudul = new JPanel();

		JPanel panelId = new JPanel();
		JPanel panelNama = new JPanel();
		JPanel panelJenis = new JPanel();
		JPanel panelStok = new JPanel();
		JPanel panelButton = new JPanel();

		JLabel labelJudul = new JLabel();
		labelJudul.setText("Info Barang");
		panelJudul.add(labelJudul);

		JLabel labelId = new JLabel();
		labelId.setText("ID:");
		panelId.add(labelId);

		JLabel infoId = new JLabel();
		infoId.setText(id);
		panelId.add(infoId);

		JLabel labelNama = new JLabel();
		labelNama.setText("Nama:");
		panelNama.add(labelNama);

		JLabel infoNama = new JLabel();
		infoNama.setText(nama);
		panelNama.add(infoNama);

		JLabel labelJenis = new JLabel();
		labelJenis.setText("Jenis:");
		panelJenis.add(labelJenis);

		JLabel infoJenis = new JLabel();
		infoJenis.setText(jenis);
		panelJenis.add(infoJenis);

		JLabel labelStok = new JLabel();
		labelStok.setText("Stok:");
		panelStok.add(labelStok);

		JLabel infoStok = new JLabel();
		infoStok.setText(stok);
		panelStok.add(infoStok);

		JButton buttonTutup = new JButton("Tutup");
		buttonTutup.addActionListener(event -> navKatalog());
		panelButton.add(buttonTutup);

		panelInfo.add(panelId);
		panelInfo.add(panelNama);
		panelInfo.add(panelJenis);
		panelInfo.add(panelStok);

		add(panelJudul, BorderLayout.NORTH);
		add(panelInfo, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
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
					id = nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue().toString();
					nama = nodeList.item(x).getAttributes().getNamedItem("nama").getNodeValue().toString();
					jenis = nodeList.item(x).getAttributes().getNamedItem("jenis").getNodeValue().toString();
					stok = nodeList.item(x).getAttributes().getNamedItem("stok").getNodeValue().toString();
				}
			}
		}

		catch (Exception e) {
			System.out.print(e);
		}
	}
}
