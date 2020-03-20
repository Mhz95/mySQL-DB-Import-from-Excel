import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import exception.InvalidExcelFileException;
/**
 * Project to import from excel files to DB
 * 
 * @Author Alya Alshammeri, Munerah H. Alzaidan, Norah Alshahrani
 * @Instructor Dr. Sofien Gannoni
 * @Course KSU CCIS CSC581 DB
 * @Date March 2020
 */
public class DBExcelImport {

	 public static void main(String[] args) {
		Database projectDB = new Database();
		Scanner sc = new Scanner(System.in);
		int con=-1;
		String file;
		while(true) {
		// Read file name from user
		System.out.println("Enter file name: ");
		 file = sc.nextLine();
		file= "./"+file+".xlsx";

		
        try {
            ExcelToObjectMapper mapper = new ExcelToObjectMapper(file);
            System.out.println("Enter sheet number: ");
    	     int n1= sc.nextInt()-1;
            GeneralTable table = mapper.map(n1);

        	System.out.println();

            for(int n = 0 ; n<table.getRows().size(); n++) {
            	Object[] r = table.getRows().get(n);
            	for(int y = 0 ; y<r.length; y++) {
                  System.out.print(r[y] + " ");
            	}
            	System.out.println();
            }
            
            System.out.println("---- Done reading from excel ----");	
            
            projectDB.createTable(table); 
            System.out.println("---- Done Create table at DB ----");

            projectDB.addTuples(table); 
            System.out.println("---- Done Insert rows at DB ----");

            projectDB.getTuples(table); 
            System.out.println("---- Done reading from DB ----");
	    System.out.println("If you want to read anthor file please enter 1 or 0 to end the program ");
            con = sc.nextInt();
    		if(con==0)break;
    		if(con==1) System.out.println("continue reading... ");
    		String temp= sc.nextLine();


        } catch (InvalidExcelFileException e) {
            System.out.println("Invalid Excel file.");
        }  catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (Exception e) {
            System.out.println("Error occured. Unable to execute mapping.");
            e.printStackTrace();
        }
		}
	 }	 
	 
}
