package model.data_retrieval;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.Arrays;

import org.apache.commons.io.*;

/**
 * DataGrabber gets the data from the CAL FIRE REST interface
 * which interacts with the PermitView application. The class
 * provides no methods for use by the view except when the
 * PMT code is inputed. The data will be saved as "data.txt"
 * in the src folder.
 * 
 * @author Theodore Tan (theodoresinhantan@gmail.com)
 * @version 1.0
 */
public class DataGrabber {
	/** String for the permit code to append to URL */
	protected String pmtCode;
	
	/**
	 * Function inputCode validates if PMT code is correct, can be changed 
	 * to increase security guarantee and not breaking the program.
	 * 
	 * @param code the inputed permit code
	 * @return true, if the code is valid; false, if the code is invalid.
	 */
	public boolean inputCode(String code) {
		code = code.toUpperCase();
		System.out.println(code);
		String[] combs = {"SUB", "CUP", "MUP", "DRC", "PMT"};
		boolean valid = true;
		if (code.replaceAll("\\D", "").length() != 9 || !code.contains("-")) {
			System.out.println("ERROR: Code should be format '[3-leter code]xxxx-xxxxx' with hyphen " +
			 "in the middle. (e.g. 'PMT2014-03045').");
			valid = false;
		}
		else if(!Arrays.asList(combs).contains(code.substring(0,3))){
			System.out.println("ERROR: Wrong 3 letter code. Code should be format '[3-leter code]xxxx-xxxxx' with hyphen " +
			 "in the middle. (e.g. 'PMT2014-03045').");
			valid = false;
		}
		else {
			pmtCode = code;
			valid = true;
		}
		
		return valid;
	}
	
	/**
	 * Function getJSONFile will get the permit info data from
	 * CAL FIRE database using the permit code that the user
	 * inputs, appending the code to the URL from the constructor.
	 * 
	 * @throws IOException Error is thrown when the file fails to be created.
	 */
	public void getJSONFile() throws IOException{
		URL url = new URL("https://www.sloplanning.org/PermitInfo/api/values?permit="+pmtCode);
		//System.out.println("url is: " + url);
		File file = new File("data.json");
		System.out.println("getting json file...");
		FileUtils.copyURLToFile(url, file);
		System.out.println("success!");
	}
}
