package com.vl.marketing.util;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.exceptions.COSVisitorException;

public class PDFGenerator {
	
	public void setField(PDDocument pdfDocument, String name, String value) throws IOException {
		PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		PDField field = acroForm.getField(name);
		if(field != null) {
			field.setValue(value);
		}
		else {
			System.err.println("No field found with name:" + name);
		}
	}
	
}
