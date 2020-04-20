import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import exception.InvalidExcelFileException;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;
import java.awt.Color;

public class Importer {
	
	static String file;
	static int sheetNo;
	private JFrame Excel_Importer;
	private JTextField textField;
	private JTextField textField_1;
	private JTable Exceltable;
	private JTable DBtabel;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Importer window = new Importer();
					window.Excel_Importer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Importer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Database projectDB = new Database();
		Excel_Importer = new JFrame();
		Excel_Importer.setTitle("Excel Importer Program");
		Excel_Importer.setBounds(100, 100, 700,650);
		Excel_Importer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Excel_Importer.getContentPane().setLayout(null);
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("excel table file", "xlsx", "xlsm" );
        chooser.setFileFilter(filter);
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(153, 180, 209), 1, true));
		panel.setBounds(28, 29, 619, 162);
		Excel_Importer.getContentPane().add(panel);
		
		JLabel lblNewLabel = new JLabel("Excel File Directory");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		
		JLabel lblSheetNumber = new JLabel("Sheet Number");
		lblSheetNumber.setFont(new Font("Times New Roman", Font.BOLD, 16));
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setText("Click Open file ");
        textField.setEditable(false);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnedValue = chooser.showOpenDialog(Excel_Importer);
            	if(returnedValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                file = selectedFile.getPath();
                textField.setText(file);}
			}
		});
		
		JButton btnOpenFile_1 = new JButton("Rest");
		btnOpenFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
            	textField_1.setText("");
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSheetNumber, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnOpenFile)
							.addGap(30)
							.addComponent(btnOpenFile_1, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(46, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSheetNumber, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpenFile)
						.addComponent(btnOpenFile_1))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(SystemColor.activeCaption));
		panel_1.setBounds(28, 220, 619, 162);
		Excel_Importer.getContentPane().add(panel_1);
		
		JButton btnViewData = new JButton("View Data");
		Exceltable = new JTable();
		btnViewData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            int Sheetnumber;
                while(true) {
            	Sheetnumber=(Integer.valueOf(textField_1.getText()));
            	if (Sheetnumber>0) {
            	sheetNo=Sheetnumber-1;

            	break;
            	}
            	//else
            	if(Sheetnumber==0) {
            		JOptionPane.showMessageDialog(null,
                            "Error: Sheet number range is starting from 1",
                            "Error: MESSAGE",
                            JOptionPane.WARNING_MESSAGE);
            		}
            	textField_1.setText("");
            	Sheetnumber=(Integer.valueOf(textField_1.getText()));
                }//End while loop

            	try {
            		ExcelToObjectMapper mapper = new ExcelToObjectMapper(file);
        			int count = mapper.SheetNumber;

                            while(sheetNo >count) {
                            	if(sheetNo >count) {
                            		JOptionPane.showMessageDialog(null,
                                            "Error: The file contines only :" +count +" Sheets",
                                            "Error: MESSAGE",
                                            JOptionPane.WARNING_MESSAGE);
                            		}
                            	else if (Sheetnumber==0) {
                            		JOptionPane.showMessageDialog(null,
                            				"Error: Sheet number range is starting from 1",
                                            "Error: MESSAGE",
                                            JOptionPane.WARNING_MESSAGE);
                            	}
                               else
                            	break;

                            	textField_1.setText("");
                            	Sheetnumber=(Integer.valueOf(textField_1.getText()));

                                }//End while loop
                            sheetNo=Sheetnumber-1;
                        	GeneralTable table = mapper.map(sheetNo);
                  //*********excelTable*************************

	            	
	             
	                DefaultTableModel model = new DefaultTableModel(mapper.getData(),mapper.getHeader());
	                Exceltable.setModel(model);
	                Exceltable.setAutoCreateRowSorter(true);
	                Exceltable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	                model = new DefaultTableModel(mapper.getData(),mapper.getHeader());
	                Exceltable.setModel(model);
	                JScrollPane scroll = new JScrollPane(Exceltable);
	                scroll.setBounds(200, 150, 500, 200);
	                Exceltable.setVisible(true);
	              


	              //*********excelTable*************************


            	 }
            	catch (InvalidExcelFileException e1) {
                     System.out.println("Invalid Excel file.");
                     JPanel panel = new JPanel();
                     JOptionPane.showMessageDialog(panel, "Invalid Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
                 }
            	catch (FileNotFoundException e1) {
                     System.out.println("File not found.");
                     JPanel panel = new JPanel();
                     JOptionPane.showMessageDialog(panel, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);

                 } catch (Exception e1) {
                     System.out.println("Error occured. Unable to execute mapping.");
                    // JPanel panel = new JPanel();
                    // JOptionPane.showMessageDialog(panel," Uncorrect sheet number", "Error", JOptionPane.ERROR_MESSAGE);

                     e1.printStackTrace();
                 }





            }});
		
		JScrollBar scrollBar = new JScrollBar();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(24)
					.addComponent(btnViewData, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 298, Short.MAX_VALUE)
					.addComponent(Exceltable, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
					.addGap(152)
					.addComponent(scrollBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(70)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnViewData)
						.addComponent(Exceltable, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(61, Short.MAX_VALUE))
				.addComponent(scrollBar, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(SystemColor.activeCaption));
		panel_2.setBounds(28, 405, 619, 162);
		Excel_Importer.getContentPane().add(panel_2);
		
		JButton btnImportData = new JButton("Import Data");
		
		DBtabel = new JTable();
		btnImportData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            	ExcelToObjectMapper mapper;
				try {
					mapper = new ExcelToObjectMapper(file);
					GeneralTable table = mapper.map(sheetNo);
					//***********database table*****************
	                
	            	projectDB.createTable(table);
	            	projectDB.addTuples(table);
	            	projectDB.getTableTuples(table);
	                Vector v= projectDB.getTableTuples(table);
	            
	                DefaultTableModel model2 = new DefaultTableModel(v, mapper.getHeader());
	                DBtabel.setModel(model2);
	                DBtabel.setAutoCreateRowSorter(true);
	                DBtabel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	                model2 = new DefaultTableModel(v,mapper.getHeader());
	                DBtabel.setModel(model2);
	                JScrollPane scroll2 = new JScrollPane(DBtabel);
	                scroll2.setBounds(200, 400,500, 200);
	                DBtabel.setVisible(true);
	             



	              //***********database table*****************
				}  catch (Exception e1) {
                    System.out.println("Error occured. Unable to execute mapping.");
                   // JPanel panel = new JPanel();
                   // JOptionPane.showMessageDialog(panel," Uncorrect sheet number", "Error", JOptionPane.ERROR_MESSAGE);

                    e1.printStackTrace();
                }



            }});
		
		JScrollBar scrollBar_1 = new JScrollBar();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(27)
					.addComponent(btnImportData)
					.addPreferredGap(ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
					.addComponent(DBtabel, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
					.addGap(145)
					.addComponent(scrollBar_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(67)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnImportData)
								.addComponent(DBtabel, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)))
						.addComponent(scrollBar_1, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
	}
}
