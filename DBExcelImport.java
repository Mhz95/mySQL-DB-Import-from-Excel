import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import exception.InvalidExcelFileException;

/**
 * Project to import from excel files to DB
 *
 * @Author Alya Alshammeri, Munerah H. Alzaidan, Nourah Alshahrani
 * @Instructor Dr. Sofien Gannoni
 * @Course KSU CCIS CSC581 DB
 * @Date March 2020
 */
public class DBExcelImport extends JFrame {

	static String file;
	static int sheetNo;
	/*
	 * Database connection
	 */
	Database projectDB;

	/**
	 * GUI Components
	 */
	JTabbedPane pane;
	JTabbedPane paneT;
	JButton importBtn;
	JButton connect;
	JButton viewBtn;
	JButton clear;
	JButton exit;
	JButton readFileBtn;
	JProgressBar progressBar;
	JLabel filepathLbl;
	JLabel sheetNumLbl;
	JLabel excelPreviewLbl;
	JLabel dbPreviewLbl;
	JLabel logsLbl;
	JLabel author;
	JTextField textfilepath;
	JLabel dbNameLbl;
	JTextField dbNameTxt;
	JLabel userNameLbl;
	JTextField userNameTxt;
	JLabel pwdLbl;
	JPasswordField pwdTxt;
	JFileChooser chooser;
	JTable excelTable;
	JComboBox<String> sheetNumCb;
	static JTextArea logs;
	static boolean errorconnected = false;
	int numberOfSheets;

	String dbName, userName, pwd;

	ExcelToObjectMapper mapper;

	String text = "Welcome to Database Excel Importer !\nPlease enter an excel sheet";

	public DBExcelImport() {

		/**
		 * Setup JFrame
		 */
		setTitle("CSC 581 - Excel to DB");
		setSize(960, 580);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		/**
		 * Add Components to JFrame
		 */

		filepathLbl = new JLabel("Excel File Directory :");
		sheetNumLbl = new JLabel("Sheet Number :");
		excelPreviewLbl = new JLabel("Excel Preview :");
		dbPreviewLbl = new JLabel("DB Preview :");
		dbNameLbl = new JLabel("DB Name :");
		userNameLbl = new JLabel("User Name :");
		pwdLbl = new JLabel("Password :");

		logsLbl = new JLabel("Logs :");
		author = new JLabel("© Alya Alshammeri, Munerah H. Alzaidan, Nourah Alshahrani");
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("excel table file", "xlsx", "csv", "xlsm");
		chooser.setFileFilter(filter);
		logs = new JTextArea();

		logs.setText(text);
		logs.setLineWrap(true);
		logs.setWrapStyleWord(true);

		JScrollPane logsPane = new JScrollPane(logs);
		logsPane.setPreferredSize(new Dimension(500, 200));
		logsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		connect = new JButton("Connect");
		importBtn = new JButton("Import");
		viewBtn = new JButton("View Data");
		clear = new JButton("Reset");
		readFileBtn = new JButton("Open file");
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		textfilepath = new JTextField();
		dbNameTxt = new JTextField();
		userNameTxt = new JTextField();
		pwdTxt = new JPasswordField();
		sheetNumCb = new JComboBox<String>();
		sheetNumCb.setEnabled(false);
		excelTable = new JTable();

		add(dbNameLbl);
		add(dbNameTxt);
		add(userNameLbl);
		add(userNameTxt);
		add(pwdLbl);
		add(pwdTxt);
		add(connect);

		add(filepathLbl);
		add(textfilepath);
		add(readFileBtn);
		add(sheetNumLbl);
		add(sheetNumCb);
		add(importBtn);
		add(viewBtn);
		add(clear);
		add(progressBar);
		add(logsLbl);
		add(logsPane);
		add(author);
		add(excelPreviewLbl);
		add(dbPreviewLbl);

		setVisible(true);

		/**
		 * JFrame Components Positions
		 * 
		 * @param (x,y,width,height)
		 */

		dbNameLbl.setBounds(500, 10, 100, 40);
		dbNameTxt.setBounds(500, 40, 100, 30);

		userNameLbl.setBounds(600, 10, 100, 40);
		userNameTxt.setBounds(600, 40, 100, 30);

		pwdLbl.setBounds(700, 10, 100, 40);
		pwdTxt.setBounds(700, 40, 100, 30);

		connect.setBounds(810, 40, 100, 40);

		filepathLbl.setBounds(500, 60, 250, 40);
		textfilepath.setBounds(500, 90, 300, 30);

		readFileBtn.setBounds(810, 85, 100, 40);

		sheetNumLbl.setBounds(500, 115, 250, 40);
		sheetNumCb.setBounds(500, 140, 250, 40);

		viewBtn.setBounds(500, 180, 100, 40);
		importBtn.setBounds(600, 180, 100, 40);
		clear.setBounds(810, 180, 100, 40);

		progressBar.setBounds(500, 220, 250, 30);

		logsLbl.setBounds(500, 240, 160, 40);
		logsPane.setBounds(500, 275, 300, 250);
		author.setBounds(300, 520, 400, 40);

		excelPreviewLbl.setBounds(30, 10, 250, 40);
		dbPreviewLbl.setBounds(30, 240, 250, 40);

		clear.setEnabled(false);
		viewBtn.setEnabled(false);
		importBtn.setEnabled(false);
		readFileBtn.setEnabled(false);

		/**
		 * onClick open file
		 */
		readFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {

						progressBar.setVisible(true);
						try {
							ReadExcelFile();
							progressBar.setVisible(false);
							connect.setEnabled(false);

						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});

		/**
		 * onClick on Clear Button
		 */
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				logs.setText(text);
				reset();

			}
		});
		/**
		 * onClick on Connect
		 */
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				logs.setText("");

				new Thread(new Runnable() {
					@Override
					public void run() {

						logs.setText("Connecting to DB ...");
						progressBar.setVisible(true);
						try {
							dbName = dbNameTxt.getText();
							userName = userNameTxt.getText();
							pwd = pwdTxt.getText();

							errorconnected = false;

							if (dbNameTxt.getText().equals("") || userNameTxt.getText().equals("")) {
								logs.setText("DB name and DB user name can't be empty!");
							} else {
								projectDB = new Database(dbName, userName, pwd);

								if (!errorconnected) {
									logs.setText("Connected Successfully!");
									readFileBtn.setEnabled(true);
								}
							}

						} catch (Exception e1) {
							logs.setText(e1.getMessage());
						}

						progressBar.setVisible(false);

					}
				}).start();

			}
		});

		/**
		 * onClick on View
		 */
		viewBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				logs.setText("");

				new Thread(new Runnable() {
					@Override
					public void run() {

						progressBar.setVisible(true);

						try {
							int temp;

							if (sheetNumCb.getSelectedItem().toString().equals("All")) {
								temp = numberOfSheets;

							} else {
								temp = 1;
							}
							paneT = new JTabbedPane(JTabbedPane.TOP);
							TableModel model = new DefaultTableModel();
							for (int t = 0; t < temp; t++) {
								GeneralTable table = new GeneralTable();
								if (!sheetNumCb.getSelectedItem().toString().equals("All")) {
									int selectedSheet = sheetNumCb.getSelectedIndex();
									table = mapper.map(selectedSheet);
									model = new DefaultTableModel(mapper.getData(), mapper.getHeader());
									logs.setText(
											"Preview excel table for sheet: " + (selectedSheet + 1) + "\n");
								} else {
									table = mapper.map(t);
									logs.setText("Preview excel table for sheet: " + (t + 1) + "\n");
									model = new DefaultTableModel(mapper.getData(), mapper.getHeader());

								}
								
								JTable excelTable = new JTable();
								excelTable.setModel(model);
								excelTable.setAutoCreateRowSorter(true);
								excelTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
								JScrollPane scrollExcelTable = new JScrollPane(excelTable);
								scrollExcelTable.setBounds(30, 45, 400, 200);
								excelTable.setVisible(true);

								paneT.add(scrollExcelTable, "Sheet" + t);
								paneT.revalidate();
								paneT.repaint();

							}

							add(paneT);

							paneT.setBounds(30, 45, 400, 200);
							paneT.setVisible(true);

							progressBar.setVisible(false);

						} catch (InvalidExcelFileException e1) {
							System.out.println("Invalid Excel file.");
							JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Invalid Excel file.", "Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (FileNotFoundException e1) {
							System.out.println("File not found.");
							JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);

						} catch (NullPointerException e1) {
							JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel, "Rows are empty (Null).", "Error",
									JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();

						} catch (Exception e1) {
							System.out.println("Error occured. Unable to execute mapping.");
							// JPanel panel = new JPanel();
							// JOptionPane.showMessageDialog(panel," Uncorrect sheet number", "Error",
							// JOptionPane.ERROR_MESSAGE);

							e1.printStackTrace();
						}

					}
				}).start();

			}
		});

		/**
		 * onClick on import
		 */
		importBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				logs.setText("");

				new Thread(new Runnable() {
					@Override
					public void run() {

						progressBar.setVisible(true);

						try {

							int temp2;

							if (sheetNumCb.getSelectedItem().toString().equals("All")) {
								temp2 = numberOfSheets;

							} else {
								temp2 = 1;
							}
							 pane = new JTabbedPane(JTabbedPane.TOP);
							for (int n = 0; n < temp2; n++) {
								GeneralTable table2;
								if (!sheetNumCb.getSelectedItem().toString().equals("All")) {
									int selectedSheet = sheetNumCb.getSelectedIndex();
									table2 = mapper.map(selectedSheet);
									logs.setText(
											"Preview imported to DB table for sheet: " + (selectedSheet + 1) + "\n");
								} else {
									table2 = mapper.map(n);
									logs.setText("Preview imported to DB table for sheet: " + (n + 1) + "\n");
								}

								projectDB.createTable(table2);
								projectDB.addTuples(table2);
								projectDB.getTableTuples(table2);
								ResultSet rs = projectDB.getTableTuples(table2);

								DefaultTableModel model2 = buildTableModel(rs);
								JTable dbTable = new JTable();
								dbTable.setModel(model2);
								dbTable.setAutoCreateRowSorter(true);
								dbTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
								JScrollPane scrollDBTable = new JScrollPane(dbTable);
								scrollDBTable.setBounds(30, 275, 400, 200);
								dbTable.setVisible(true);

								pane.add(scrollDBTable, "Sheet" + n);
								pane.revalidate();
								pane.repaint();

							}

							add(pane);
							pane.setBounds(30, 275, 400, 200);
							pane.setVisible(true);


							progressBar.setVisible(false);

						} catch (Exception e1) {
							System.out.println("Error occured. Unable to execute mapping.");
							// JPanel panel = new JPanel();
							// JOptionPane.showMessageDialog(panel," Uncorrect sheet number", "Error",
							// JOptionPane.ERROR_MESSAGE);

							e1.printStackTrace();
						}

					}
				}).start();

			}
		});

	}

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}

	/**
	 * Reset All
	 */
	public void reset() {
		sheetNumCb.removeAllItems();
		textfilepath.setText("");
		sheetNumCb.setEnabled(false);
		viewBtn.setEnabled(false);
		importBtn.setEnabled(false);
		clear.setEnabled(false);
		if (pane != null) {
			pane.setVisible(false);
			pane.removeAll();
		}
		if (paneT != null) {
			paneT.setVisible(false);
			paneT.removeAll();
		}
		connect.setEnabled(true);

	}

	/**
	 * Read an input excel file
	 */
	public void ReadExcelFile() throws FileNotFoundException {

		int returnedValue = chooser.showOpenDialog(this);
		if (returnedValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			file = selectedFile.getPath();
			String filenamewithext = selectedFile.getName();
			String extention = filenamewithext.substring(filenamewithext.indexOf("."), filenamewithext.length());
			String filenamewithoutext = filenamewithext.substring(0, filenamewithext.indexOf("."));

			if (extention.equals(".csv")) {
				System.out.println("here");
				file = csvToXLSX(file, filenamewithoutext);
			}

			textfilepath.setText(file);
			try {

				mapper = new ExcelToObjectMapper(file);
				numberOfSheets = mapper.SheetNumber;
				numberOfSheets = numberOfSheets - 1;

				for (int i = 0; i < numberOfSheets; i++) {
					sheetNumCb.addItem(i + 1 + "");
				}
				sheetNumCb.addItem("All");
				sheetNumCb.setEnabled(true);
				viewBtn.setEnabled(true);
				importBtn.setEnabled(true);
				clear.setEnabled(true);

			} catch (InvalidExcelFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static String csvToXLSX(String csvFileAddress, String filenamewithoutext) {
		try {
			String xlsxFileAddress = filenamewithoutext + ".xlsx"; // xlsx file address
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet1");
			String currentLine = null;
			int RowNum = 0;
			BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
			while ((currentLine = br.readLine()) != null) {
				String str[] = currentLine.split(",");
				XSSFRow currentRow = sheet.createRow(RowNum);
				for (int i = 0; i < str.length; i++) {
					currentRow.createCell(i).setCellValue(str[i]);
				}
				RowNum++;
			}

			FileOutputStream fileOutputStream = new FileOutputStream(xlsxFileAddress);
			workBook.write(fileOutputStream);
			fileOutputStream.close();
			return xlsxFileAddress;
		} catch (Exception ex) {
			System.out.println(ex.getMessage() + "Exception in try");
		}
		return csvFileAddress;
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				DBExcelImport dbExcelImport = new DBExcelImport();

			}
		});

	}

}
