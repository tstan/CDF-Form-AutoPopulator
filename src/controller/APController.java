package controller;
import model.data_retrieval.*;
import java.util.*;
import java.io.*;

/**
 * APController is the class that contains the main method of 
 * the AP program. AP stands for "auto population".
 * It controls the data flow into model object and 
 * updates the view whenever data changes, keeping view and model separate. 
 * 
 * @author Theodore Tan (theodoresinhantan@gmail.com)
 * @version 1.0
 */
public class APController {
	
	/**
	 * Updates the UI when called in order to refresh
	 * with new populating data.
	 */
	public void updateView() {
		//TODO
	}
	
	/**
	 * Main method that combines the model and view into a cohesive
	 * software program.
	 * 
	 * @param args list of arguments
	 */
	public static void main(String[] args) throws IOException{
		Scanner input = new Scanner(System.in);
		String code = null, check;
		boolean invalid = true;
		APController controller = new APController();
		DataGrabber dg = new DataGrabber();

		//get JSON from database
		System.out.println("enter pmt code (xxxx-xxxxx)...");
		code = input.next();
		while (!dg.inputCode(code)) {
			System.out.println ("\nre-enter pmt code");
			code = input.next();
		}
		dg.getJSONFile();
		
		//printing files for debugging/implementation purposes
		String fileName = "data.JSON";
        //Instantiate the BufferedReader Class
        BufferedReader bufferReader = new BufferedReader( new FileReader(fileName));
        //Variable to hold the one char data
        int out;
        // Read file line by line and print on the console
        while ((out = bufferReader.read()) != -1)	{
        	System.out.print((char)out);
        	if((char)out == ',')
        		System.out.println();
        }
        //Close the buffer reader
        bufferReader.close();
		
		//close input stream
		input.close();
	}

}
