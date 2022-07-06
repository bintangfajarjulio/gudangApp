package gudangApp;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;

public class KatalogFrame extends JFrame {

	private String[] columnNames = { "ID Barang", "Nama Barang", "Jenis Barang", "Stok" };
	private Object[][] cells = {};
	DefaultTableModel modelTable;
	JTable tabelBarang;

	int loadId;
	String loadNama;
	String loadJenis;
	int loadStok;

	public KatalogFrame() throws ParserConfigurationException {
		setVisible(true);
		setTitle("Laman Katalog");
		// setPreferredSize(new Dimension());

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenuItem logOut = new JMenuItem("Log Out");
		menuBar.add(logOut);
		logOut.addActionListener(event -> logOut());

		JPanel judulPanel = new JPanel();
		JLabel judulTabel = new JLabel();
		judulTabel.setText("Data Barang Gudang");
		judulPanel.add(judulTabel);

		JPanel contentPanel = new JPanel();

		modelTable = new DefaultTableModel(cells, columnNames);
		tabelBarang = new JTable(modelTable);
		loadTableValue();
		JScrollPane scrollPaneTable = new JScrollPane(tabelBarang);
		contentPanel.add(scrollPaneTable);

		JPanel buttonPanel = new JPanel();
		JButton buttonTambah = new JButton("Tambah Barang");
		buttonTambah.addActionListener(event -> tambahBarang());
		buttonPanel.add(buttonTambah);

		JButton buttonInfo = new JButton("Lihat Info");
		buttonInfo.addActionListener(event -> infoBarang());
		buttonPanel.add(buttonInfo);

		JButton buttonEdit = new JButton("Edit Info");
		buttonEdit.addActionListener(event -> editBarang());
		buttonPanel.add(buttonEdit);

		JButton buttonHapus = new JButton("Hapus Barang");
		buttonHapus.addActionListener(event -> hapusBarang());
		buttonPanel.add(buttonHapus);

		JButton buttonTree = new JButton("Tree Kluster");
		buttonTree.addActionListener(event -> treeKategori());
		buttonPanel.add(buttonTree);

		add(judulPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}

	public void logOut() {
		JOptionPane.showMessageDialog(this, "Anda sudah logout");
		EventQueue.invokeLater(() -> {
			JFrame loginFrame = new LoginFrame();
			loginFrame.setVisible(true);
			loginFrame.setResizable(false);
			dispose();
		});
	}

	public void tambahBarang() {
		EventQueue.invokeLater(() -> {
			JFrame tambahBarangFrame = new TambahBarangFrame();
			tambahBarangFrame.setVisible(true);
			tambahBarangFrame.setResizable(false);
			dispose();
		});
	}

	public void loadTableValue() throws ParserConfigurationException {
		final File barang = new File("D:\\Project\\javaEclipse\\gudangApp\\src\\gudangApp\\barang.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		try {
			Document doc = builder.parse(barang);
			NodeList nodeList = doc.getElementsByTagName("barang");
			for (int x = 0, size = nodeList.getLength(); x < size; x++) {
				loadId = Integer.parseInt(nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue());
				loadNama = nodeList.item(x).getAttributes().getNamedItem("nama").getNodeValue().toString();
				loadJenis = nodeList.item(x).getAttributes().getNamedItem("jenis").getNodeValue().toString();
				loadStok = Integer.parseInt(nodeList.item(x).getAttributes().getNamedItem("stok").getNodeValue());

				modelTable.addRow(new Object[] { loadId, loadNama, loadJenis, loadStok });
			}
		}

		catch (Exception e) {
			System.out.print(e);
		}
	}

	public void infoBarang() {
		try {
			int column = 0;
			int row = tabelBarang.getSelectedRow();
			String value = tabelBarang.getModel().getValueAt(row, column).toString();

			EventQueue.invokeLater(() -> {
				JFrame infoFrame;
				try {
					infoFrame = new InfoFrame(value);
					infoFrame.setVisible(true);
					infoFrame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			});
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Pilih kolom barang pada tabel");
		}
	}

	public void editBarang() {
		try {
			int column = 0;
			int row = tabelBarang.getSelectedRow();
			String value = tabelBarang.getModel().getValueAt(row, column).toString();

			EventQueue.invokeLater(() -> {
				JFrame editFrame;
				try {
					editFrame = new EditFrame(value);
					editFrame.setVisible(true);
					editFrame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			});
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Pilih kolom barang pada tabel");
		}
	}

	public void hapusBarang() {
		try {
			int column = 0;
			int row = tabelBarang.getSelectedRow();
			String value = tabelBarang.getModel().getValueAt(row, column).toString();

			EventQueue.invokeLater(() -> {
				JFrame hapusFrame;
				try {
					hapusFrame = new HapusFrame(value);
					hapusFrame.setVisible(true);
					hapusFrame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispose();
			});
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Pilih kolom barang pada tabel");
		}
	}

	public void treeKategori() {
		EventQueue.invokeLater(() -> {
			JFrame treeFrame;
			try {
				treeFrame = new TreeFrame();
				treeFrame.setVisible(true);
				treeFrame.setResizable(false);
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
