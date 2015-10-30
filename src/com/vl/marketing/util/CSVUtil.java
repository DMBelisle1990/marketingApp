package com.vl.marketing.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CSVUtil {

	public static void openInExcel(String file) {
		File myCSVFile = new File(file); //reference to your file here 
//		String execString = "excel "; //+ myCSVFile.getAbsolutePath();
//		Runtime run = Runtime.getRuntime();
//		try {
//		    Process pp = run.exec(execString);
//		} catch(Exception e) {
//		    e.printStackTrace();
//		}
		
		Desktop dt = Desktop.getDesktop();
		try {
			dt.open(myCSVFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("temp3.csv", "UTF-8");
		writer.println("a, kevin, a, a");
		writer.println("c, b, b, b");
		writer.close();
		openInExcel("temp3.csv");
	}
}
