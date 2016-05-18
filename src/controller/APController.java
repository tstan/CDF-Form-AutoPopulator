package controller;

import org.json.*;
import model.data_retrieval.*;
import model.doc_population.*;
import model.doc_population.SubstituteText.ExtensionFilter;

import java.util.*;
import java.io.*;

/**
 * APController is the class that contains the main method of the AP program. AP
 * stands for "auto population". It controls the data flow into model object and
 * updates the view whenever data changes, keeping view and model separate.
 * 
 * @author Theodore Tan (theodoresinhantan@gmail.com)
 * @version 1.0
 */
public class APController {

	/**
	 * Updates the UI when called in order to refresh with new populating data.
	 */
	public void updateView() {
		// TODO
	}

	/**
	 * Main method that combines the model and view into a cohesive software
	 * program.
	 * 
	 * @param args
	 *            list of arguments
	 */
	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in);
		String code = null, check;
		boolean invalid = true;
		APController controller = new APController();
		DataGrabber dg = new DataGrabber();

		// get JSON from database
		System.out.println("enter pmt code ([PMT]xxxx-xxxxx)...");
		code = input.next();
		while (!dg.inputCode(code)) {
			System.out.println("\nre-enter pmt code");
			code = input.next();
		}
		dg.getJSONFile();

		//begin text substitution
		//*************************
		final String docxDir = "src/forms"; // directory with
		// the original
		// .docx file
		
		final String docsubDir = ".";
		final String[] docxNames = {"Fire Safety Plan Application.docx", 
				"Project File Cover Sheet.docx",
				"Exemption-Refund Letter.docx",
				"Fire Life Clearance Letter.docx",
				"FSP - Commercial.docx",
				"FSP - Residential.docx",
				"Parcel map.docx",
				"Project File Cover Sheet.docx",
				"Reduced Setback Letter.docx"}; // file name of the original .docx
		// file
		final String[] docxSubNames = {"output/_FILLED_Fire Safety Plan Application.docx", 
				"output/_FILLED_Project File Cover Sheet.docx",
				"output/_FILLED_Exemption-Refund Letter.docx",
				"output/_FILLED_Fire Life Clearance Letter.docx",
				"output/_FILLED_FSP - Commercial.docx",
				"output/_FILLED_FSP - Residential.docx",
				"output/_FILLED_Parcel map.docx",
				"output/_FILLED_Project File Cover Sheet.docx",
				"output/_FILLED_Reduced Setback Letter.docx"}; // file name of the .docx
		// file created with
		// substituted texts

		try {
			for (int j = 0; j < docxNames.length; j++) {
				String docxName = docxNames[j];
				String docxSubName = docxSubNames[j];
				// 1. unzip docx file
				ZipUtility.unzip(new File(docxDir, docxName), new File(docxDir,
						SubstituteText.stripFileExt(docxName)));

				// 2a. get list of XML files in /word in unzipped folder
				FilenameFilter ff = new ExtensionFilter("xml");
				File XMLdir = new File(new File(docxDir, SubstituteText.stripFileExt(docxName)),
						"word");
				String[] XMLfiles = XMLdir.list(ff);

				// 2b. read xml files and do text substitution
				if (XMLfiles != null) {
					for (int i = 0; i < XMLfiles.length; i++) {
						SubstituteText.substituteText(new File(XMLdir, XMLfiles[i]), new File(
								XMLdir, "_" + XMLfiles[i]));
					}
				}

				// 3. zip contents back to docx file
				ZipUtility.zipDirectory(new File(docxDir, SubstituteText.stripFileExt(docxName)),
						new File(docsubDir, docxSubName));

				// 4. delete unzipped folder
				SubstituteText.cleanDirectory(new File(docxDir, SubstituteText.stripFileExt(docxName)));

				System.out.println("END doc " + j);
			}
		} catch (Exception e) {
			System.out.println("permit may be missing some information on PermitView...");
			System.out.println(e);
		}
		//****************************
		//end text substitution

		System.out.println("The filled forms will be in the 'output' folder.");
		// close input stream
		input.close();
	}

}
