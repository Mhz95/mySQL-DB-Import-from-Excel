import java.io.FileNotFoundException;
import java.util.List;

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

        String file = "/Users/Mhz/Downloads/DB/DBExcelImport/students.xlsx";
        try {
            ExcelToObjectMapper mapper = new ExcelToObjectMapper(file);
            List<Student> students = mapper.map(Student.class);
            for (Student student : students) {
                String res = "Name : " + student.getName() + ", Address : " + student.getUniversity() + ", Number : "+ student.getNumber();
                System.out.println(res);
                
                // Add to DB
                projectDB.addStudents(student); 
            }
            
            System.out.println("---- Done reading from excel & writing to DB ----");	
            
            projectDB.getStudents(); 
    		
            System.out.println("---- Done reading from DB ----");


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
