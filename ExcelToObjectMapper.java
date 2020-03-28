import exception.InvalidExcelFileException;
import exception.InvalidObjectFieldNameException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;


/**
 * A simple Excel to Object mapper utility using Apache POI.
 * This class provides utility methods, to read an Excel file and convert each rows of
 * the excel file into appropriate model object as specified and return all rows of excel
 * file as list of given object type.
 */

public class ExcelToObjectMapper {
    private Workbook workbook;
	 static Vector Columns = new Vector();
	 static Vector data = new Vector();

    public ExcelToObjectMapper(String fileUrl) throws IOException, InvalidExcelFileException {
          try {
        	int count=1;
            workbook = createWorkBook(fileUrl);
            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");//
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();//
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                System.out.println("Sheet "+ count+" :"+ sheet.getSheetName());
                count++;
            }
        } catch (InvalidFormatException e) {
            throw new InvalidExcelFileException(e.getMessage());
        }
    }

    /**
     * Read data from Excel file and convert each rows into list of object.
     * @return List of array of objects. list of rows each row of different objects.
     * @throws Exception if failed to generate mapping.
     */
    public GeneralTable map(int n) throws Exception {

        Sheet sheet = workbook.getSheetAt(n);
        DataFormatter formatter = new DataFormatter();

        // Create New table + Set table Name
        GeneralTable gtable = new GeneralTable();
        gtable.setTableName(sheet.getSheetName());
        
        // Headers Reading
        Columns.clear();
        int header = sheet.getFirstRowNum();
        int lastHeaderCell = sheet.getRow(header).getLastCellNum();
        String[] headers = new String[(lastHeaderCell)];
        
        for (int h=0; h<lastHeaderCell;h++) {
        	headers[h] = sheet.getRow(header).getCell(h).getStringCellValue();
            String s=	headers[h].replace(' ', '_');
             headers[h]=s;
            System.out.print(headers[h]  + " ");
            Columns.add(headers[h]);
        }
        System.out.println();
        
        gtable.setHeaders(headers);       
        
        // Read first row after header to take types
        int firstRowAfterHeader = header + 1;
        int lastfirstRowAfterHeaderCell = sheet.getRow(firstRowAfterHeader).getLastCellNum();
        String[] types = new String[(lastfirstRowAfterHeaderCell )];

        for (int t=0; t<lastHeaderCell;t++) {
        	types[t] = sheet.getRow(firstRowAfterHeader).getCell(t).getCellType().toString();
            System.out.print(types[t]  + " ");
        }
        System.out.println();
        gtable.setTypes(types);       

        // Read Rows
        ArrayList<Object[]> rows = new ArrayList();
        data.clear();       
        int lastRow = sheet.getLastRowNum();
        for (int i=1; i<=lastRow;i++) {
            Object[] row = new Object[headers.length];
            Vector d = new Vector();

            if (sheet.getRow(i) == null){
            	continue;
            }
            for (int c=0; c<row.length;c++) {
                Cell cell = sheet.getRow(i).getCell(c);
                
              switch(types[c]) {
              case "STRING":
            	  row[c] = cell.getStringCellValue();
              	break;
              case "NUMERIC":
            
              	String str = formatter.formatCellValue(cell);
              	int intNum;
              	double doubleNum = Double.parseDouble(str);
              	if ((doubleNum % 1) == 0) {
              		intNum = (int) doubleNum;
              		row[c] = intNum;
              	} else {
              		row[c] = doubleNum;
              	}
			      
              	 //case "BOOLEAN":
            	//  row[c] = cell.getBooleanCellValue();
              	//break;
			      
              default:
            	  break;
              }
              d.add(row[c]);  
            }
            rows.add(row);
            
            d.add("\n");
            data.add(d);
        }
        gtable.setRows(rows);  
        return gtable;

    }

    /**
     * Read value from Cell and set it to given field of given object.
     * Note: supported data Type: String, Date, int, long, float, double and boolean.
     * @param obj Object whom given field belong.
     * @param field Field which value need to be set.
     * @param cell Apache POI cell from which value needs to be retrived.
     */
    private void setObjectFieldValueFromCell(Object obj, Field field, Cell cell){
        Class<?> cls = field.getType();
        field.setAccessible(true);
        if(cls == String.class) {
            try {
                field.set(obj, cell.getStringCellValue());
            }catch (Exception e) {
                try {
                    field.set(obj, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if(cls == Date.class) {
            try {
                Date date = cell.getDateCellValue();
                field.set(obj, date);
            }catch (Exception e) {
                try {
                    field.set(obj, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if(cls == int.class || cls == long.class || cls == float.class || cls == double.class ){
            double value = cell.getNumericCellValue();
            try {
                if (cls == int.class) {
                    field.set(obj, (int) value);
                }
                else if (cls == long.class) {
                    field.set(obj, (long) value);
                }
                else if (cls == float.class) {
                    field.set(obj, (float) value);
                }
                else {
                    //Double value
                    field.set(obj, value);
                }
            }catch (Exception e) {
                try {
                    field.set(obj, null);
                } catch (IllegalAccessException e1) {
                    System.out.println("chinna - ");
                    e1.printStackTrace();
                }
            }
        }
        /*else if(cls == boolean.class) {
            boolean value = cell.getBooleanCellValue();
            try {
                field.set(obj, value);
            }catch (Exception e) {
                try {
                    field.set(obj, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }/
        /*else if(cls == Collection.class) {
            double value = cell.getNumericCellValue();
            try {
                field.set(obj, value);
            }catch (Exception e) {
                try {
                    field.set(obj, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }*/
        else {
            // Unsupported data type.
        }

    }

    /**
     * Create Apache POI @{@link Workbook} for given excel file.
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    private Workbook createWorkBook(String file) throws IOException, InvalidFormatException {
        InputStream inp = new FileInputStream(file);
        return WorkbookFactory.create(inp);
    }

    /**
     * Read first row/header of Excel file, match given header name and return its index.
     * @param headerName
     * @param workbook
     * @return Index number of header name.
     * @throws InvalidObjectFieldNameException
     */
    private int getHeaderIndex(String headerName, Workbook workbook) throws Exception {
        Sheet sheet = workbook.getSheetAt(0);
        int totalColumns = sheet.getRow(0).getLastCellNum();
        int index = -1;
        for (index=0; index<totalColumns;index++) {
            Cell cell = sheet.getRow(0).getCell(index);
            if(cell.getStringCellValue().toLowerCase().equals(headerName.toLowerCase())) {
                break;
            }
        }
        if(index == -1) {
            throw new InvalidObjectFieldNameException("Invalid object field name provided.");
        }
        return index;
    }
    
    
    public Vector getData() {
    	return data;

    }

    public Vector getHeader() {
    	return Columns;
    }
}





