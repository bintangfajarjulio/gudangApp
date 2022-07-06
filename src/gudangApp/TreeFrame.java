package gudangApp;

import javax.swing.tree.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

public class TreeFrame extends JFrame {

	private java.util.List<String> baju = new ArrayList<>();
	private java.util.List<String> celana = new ArrayList<>();
	private java.util.List<String> sepatu = new ArrayList<>();

	String loadNama;
	String loadJenis;

	public TreeFrame() throws Exception {
		setTitle("Kluster Barang");
		setSize(300, 200);

		loadXML();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Jenis Barang");
		DefaultMutableTreeNode jenis = new DefaultMutableTreeNode("Baju");
		root.add(jenis);

		for (int i = 0; i < baju.size(); i++) {
			DefaultMutableTreeNode barang = new DefaultMutableTreeNode(baju.get(i));
			jenis.add(barang);
		}

		jenis = new DefaultMutableTreeNode("Celana");
		root.add(jenis);

		for (int i = 0; i < celana.size(); i++) {
			DefaultMutableTreeNode barang = new DefaultMutableTreeNode(celana.get(i));
			jenis.add(barang);
		}

		jenis = new DefaultMutableTreeNode("Sepatu");
		root.add(jenis);

		for (int i = 0; i < sepatu.size(); i++) {
			DefaultMutableTreeNode barang = new DefaultMutableTreeNode(sepatu.get(i));
			jenis.add(barang);
		}

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenuItem logOut = new JMenuItem("Close");
		menuBar.add(logOut);
		logOut.addActionListener(event -> navKatalog());

		JTree tree = new JTree(root);
		add(new JScrollPane(tree));
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

	public void loadXML() throws Exception {
		final File barang = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		try {
			Document doc = builder.parse(barang);
			NodeList nodeList = doc.getElementsByTagName("barang");
			for (int x = 0, size = nodeList.getLength(); x < size; x++) {
				loadNama = nodeList.item(x).getAttributes().getNamedItem("nama").getNodeValue().toString();
				loadJenis = nodeList.item(x).getAttributes().getNamedItem("jenis").getNodeValue().toString();

				if (loadJenis.equals("Baju")) {
					baju.add(loadNama);
				}

				if (loadJenis.equals("Celana")) {
					celana.add(loadNama);
				}

				if (loadJenis.equals("Sepatu")) {
					sepatu.add(loadNama);
				}
			}
		}

		catch (Exception e) {
			System.out.print(e);
		}
	}
}
