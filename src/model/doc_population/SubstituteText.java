package model.doc_population;

/**
 * This function is for the clean replacement of placeholders 
 * in .docx files (e.g. %text%) with other text.
 * Require ZipUtility class in the same package to work.
 */
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
 
public class SubstituteText {
    public static class ExtensionFilter implements FilenameFilter {
        String ext;
 
        public ExtensionFilter(String ext) {
            this.ext = "." + ext;
        }
 
        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }
 
    /**
     * Function to substitute placeholders with other text IMPORTANT:
     * Placeholders must start and end with %
     */
    public static boolean substituteText(File origFile, File tmpFile) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(origFile), "UTF-8"));
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tmpFile), "UTF-8"));
        
    	JSONTokener tokener = new JSONTokener(new FileReader(new File("data.JSON")));
		JSONObject root = new JSONObject(tokener);
 
        // Names of placeholders, starting and ending with % (to be updated
        // accordingly)
        String permitnum = "%csm_caseno%";
        String apn = "%prc_parcel_no%";
        String ownername = "%csm_name_last%";
        String x4 = "%owner_address%";
        String x5 = "%owner_phone%";
        String x6 = "%contractor_name%";
        String x7 = "%contractor_address%";
        String x8 = "%contractor_phone%";
        String x9 = "%csm_address%";
        String x10 = "%occ_group%";
        String x11 = "%type_const1%";
        String x12 = "%type_const2%";
        String x13 = "%plan_area%";
        String x14 = "%community%";
        String x15 = "%description%";
        String x16 = "%parcel_size%";
        // Values to replace placeholders (to be updated accordingly)
        String var1 = escapeHTML(root.get("csm_caseno").toString()); // %csm_caseno%
        String var2 = escapeHTML(root.get("prc_parcel_no").toString()); // %prc_parcel_no%
        String var3 = escapeHTML(root.get("csm_name_last").toString()); // %csm_name_last%
        String jsa = root.get("applicants").toString();
        String var4 = "N/A"; /* owner info */
        String var5 = "N/A";
        String var6 = "N/A"; /* contractor details */
        String var7 = "N/A";
        String var8 = "N/A";
        String var9 = "N/A"; /* project details */
        String var10 = escapeHTML(root.get("pmt_occ_group").toString());
        String var11 = escapeHTML(root.get("pmt_type_const1").toString());
        String var12 = escapeHTML(root.get("pmt_type_const2").toString());
        String var13 = escapeHTML(root.get("plan_area").toString());
        String var14 = escapeHTML(root.get("community").toString());
        String var15 = escapeHTML(root.get("description").toString());
        String var16 = escapeHTML(root.get("EstimatedAcres").toString() + " acres");
        JSONArray addr = root.getJSONArray("applicants");
        int index = 0;
        while (index < addr.length() && !addr.getJSONObject(index).get("role_description").equals("Owner")) {
        	index++;
        }
        var4 = escapeHTML(addr.getJSONObject(index).get("csp_address").toString());
        var5 = escapeHTML(addr.getJSONObject(index).get("cpc_comm_detail").toString());
        
        index = 0;
        while (index < addr.length() && (!addr.getJSONObject(index).get("role_description").equals("Contractor") 
        		|| !addr.getJSONObject(index).get("role_description").equals("Agent"))) {
        	index++;
        }
        index--;
        
    	var6 = escapeHTML(addr.getJSONObject(index).get("csp_name").toString());
    	var7 = escapeHTML(addr.getJSONObject(index).get("csp_address").toString());
    	var8 = escapeHTML(addr.getJSONObject(index).get("cpc_comm_detail").toString());
    	
    	addr = root.getJSONArray("addresses");
    	var9 = escapeHTML(addr.getJSONObject(0).get("csm_address").toString());
    	
        //*****************
        //begin replacing
        String line;
        for (int i = 1; ((line = reader.readLine()) != null); i++) {
            int cursor = 0;
            // Print to file and flush for every 1000 lines
            // To prevent memory error
            if (i % 1000 == 0) {
                writer.write(sb.toString());
                writer.flush();
                sb = new StringBuffer();
            }
 
            int startIdx = 0;
            int endIdx = 0;
            String result = "";
            while ((startIdx = line.indexOf("%", cursor)) > -1
                    && (endIdx = line.indexOf("%", startIdx + 1)) > -1) {
                result += line.substring(cursor, startIdx);
                cursor = endIdx + 1;
 
                String substring = line.substring(startIdx, cursor);
                String stripXML = stripXMLHTMLTags(substring);
 
                if (stripXML != null && !stripXML.equals("")) {
                    // if is placeholder, replace with text accordingly
                    if (stripXML.equals(permitnum))
                        result += var1;
                    else if (stripXML.equals(apn))
                        result += var2;
                    else if (stripXML.equals(ownername))
                        result += var3;
                    else if (stripXML.equals(x4))
                        result += var4;
                    else if (stripXML.equals(x5))
                    	result += var5;
                    else if (stripXML.equals(x6))
                    	result += var6;
                    else if (stripXML.equals(x7))
                    	result += var7;
                    else if (stripXML.equals(x8))
                    	result += var8;
                   else if (stripXML.equals(x9))
                	   result += var9;
                    else if (stripXML.equals(x10))
                    	result += var10;
                    else if (stripXML.equals(x11))
                    	result += var11;
                    else if (stripXML.equals(x12))
                    	result += var12;
                    else if (stripXML.equals(x13))
                    	result += var13;
                    else if (stripXML.equals(x14))
                    	result += var14;
                    else if (stripXML.equals(x15))
                    	result += var15;
                    else if (stripXML.equals(x16))
                    	result += var16;
                    else {
                        result += (substring.substring(0,
                                substring.length() - 1));
                        cursor = endIdx;
                    }
                }
            }
 
            result += line.substring(cursor);
            line = result;
 
            sb.append(line);
            sb.append("\r\n");
        }
        writer.write(sb.toString());
        writer.flush();
        writer.close();
 
        reader.close();
 
        origFile.delete();
        // Rename file (or directory)
        return tmpFile.renameTo(origFile);
    }
 
    /**
     * Clean directory - delete all files and folders
     */
    public static void cleanDirectory(File d) {
        if (d.exists()) {
            File[] files = d.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() && files[i].listFiles().length > 0) {
                    cleanDirectory(files[i]);
                }
                files[i].delete();
            }
            d.delete();
        }
    }
 
    /**
     * Strip string of XML and HTML tags
     */
    private static String stripXMLHTMLTags(String s) {
        return s.replaceAll("\\<.*?>", "");
    }
 
    /**
     * Strip file name of extension
     */
    public static String stripFileExt(String f) {
        return f.substring(0, f.lastIndexOf("."));
    }
 
    /**
     * Escape valid html tags
     */
    public static String escapeHTML(String s) {
        if (s == null)
            return s;
        return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                .replaceAll("'", "'").replaceAll("/", "/")
                .replaceAll("'", "'");
    }
}