import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;

import exception.InvalidExcelFileException;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
/**
 * Project to import from excel files to DB
 * 
 * @Author Alya Alshammeri, Munerah H. Alzaidan, Nourah Alshahrani
 * @Instructor Dr. Sofien Gannoni
 * @Course KSU CCIS CSC581 DB
 * @Date March 2020
 */
public class DBExcelImport extends JFrame  {
		
    static String file;
    static int sheetNo;
  
    
	public static void main(String[] args) {
		
		Database projectDB = new Database();
		JFrame frame = new JFrame("Excel Importer");
		frame.setSize(600,600); 
        Container container = frame.getContentPane();
        GroupLayout groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.preferredLayoutSize(container);
  
        JLabel label_1 = new JLabel(" Excel File Directory :");
        JLabel label_2 = new JLabel(" Sheet Number : ");
        JLabel label_3 = new JLabel("Done importing table from Excel file");
        label_3.hide();
        JLabel label_4 = new JLabel("Done storing the table to the database");
        label_4.hide();
        JTextField textField_1 = new JTextField();
        JTextField textField_2 = new JTextField();
        JButton button_1 = new JButton("Import");
        JButton button_2 = new JButton("Clear");
      
        
        //************************************
        
        button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	file= textField_1.getText()+".xlsx";
            	sheetNo=(Integer.valueOf(textField_2.getText()))-1;
            	
            	try {
            	ExcelToObjectMapper mapper = new ExcelToObjectMapper(file);
            	GeneralTable table = mapper.map(sheetNo);
            	
            
            	//*********excelTable*************************

            	label_3.show(true);
                JTable table1 = new JTable();
                DefaultTableModel model = new DefaultTableModel(mapper.getData(),mapper.getHeader());
                table1.setModel(model);
                table1.setAutoCreateRowSorter(true);
                model = new DefaultTableModel(mapper.getData(),mapper.getHeader());
                table1.setModel(model);
                JScrollPane scroll = new JScrollPane(table1);
                scroll.setBounds(150, 200, 360, 150);
                table1.setVisible(true); 
                frame.add(scroll);
                
              
              //*********excelTable*************************
                
                
            	//***********database table*****************
                label_4.show(true);
            	projectDB.createTable(table);
            	projectDB.addTuples(table);
            	projectDB.getTableTuples(table); 
                Vector v= projectDB.getTableTuples(table);
                JTable table2 = new JTable();
                DefaultTableModel model2 = new DefaultTableModel(v, mapper.getHeader());
                table2.setModel(model2);
                table2.setAutoCreateRowSorter(true);
                model2 = new DefaultTableModel(v,mapper.getHeader());
                table2.setModel(model2);
                JScrollPane scroll2 = new JScrollPane(table2);
                scroll2.setBounds(150, 400,360, 150);
                table2.setVisible(true); 
                frame.add(scroll2);
                
                
                
              //***********database table*****************
            	
            	
            	 } catch (InvalidExcelFileException e1) {
                     System.out.println("Invalid Excel file.");
                 }  catch (FileNotFoundException e1) {
                     System.out.println("File not found.");
                 } catch (Exception e1) {
                     System.out.println("Error occured. Unable to execute mapping.");
                     e1.printStackTrace();
                 }
            }});
        
        //************************************
   
        button_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	textField_1.setText("");	
            	textField_2.setText("");
            	
            }});
        
        
        //************************************
     
        groupLayout.setHorizontalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(label_1)
                    .addComponent(label_2)
                    .addComponent(label_3)
                    .addComponent(label_4)
           )
                
                
             
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.DEFAULT_SIZE, 20)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                  
                    .addComponent(textField_1,  GroupLayout.DEFAULT_SIZE,200,360)
                    .addComponent(textField_2, GroupLayout.DEFAULT_SIZE,200,360)
 
                    .addGroup(groupLayout.createSequentialGroup()
                      
                        .addComponent(button_1, GroupLayout.DEFAULT_SIZE,100,180)
                        .addComponent(button_2, GroupLayout.DEFAULT_SIZE,100,180)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.DEFAULT_SIZE,20)
                        
                    )
                )
              
        );

 
        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(label_1)
                    .addComponent(textField_1)
                )
                
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.DEFAULT_SIZE, 20)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(label_2)
                    .addComponent(textField_2)
                )
                
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.DEFAULT_SIZE, 20)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(button_1)
                    .addComponent(button_2)
                  
                    
                    
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.DEFAULT_SIZE, 40)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label_3)
                        
                      
                        
                        
                    )
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,GroupLayout.DEFAULT_SIZE, 200)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label_4)
                       
                      
                        
                        
                    )

                
                
        );

        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
 
    }
		
	}
		
		
	


     
	 

	 

