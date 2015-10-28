package com.vl.marketing.util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Item;

import javafx.collections.ObservableList;

public class PDFGenerator {

	public void setFields(String company, String startDate, String endDate, double forecast, String status, String vlMarketingNumber, double actual, String promoDescription, String marketingNumber, String promoType) throws IOException, DocumentException {

		PdfReader reader = new PdfReader("blankWithFields.pdf");
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(company + "-" + vlMarketingNumber + ".pdf"));
		AcroFields form = stamper.getAcroFields();

		DBA database = new DBA();
		LinkedHashMap<String, String> hashMap = database.getCustomerInfo(company);
		ObservableList<Item> items = database.getItems(vlMarketingNumber);        

		form.setField("untitled1", company);
		form.setField("untitled2", hashMap.get("address"));
		form.setField("untitled3", hashMap.get("city"));
		form.setField("untitled4", hashMap.get("zip"));
		form.setField("untitled5", hashMap.get("webAddress"));
		form.setField("untitled6", hashMap.get("name"));
		form.setField("untitled7", hashMap.get("title"));
		form.setField("untitled8", hashMap.get("phone"));
		form.setField("untitled9", hashMap.get("fax"));
		form.setField("untitled10", hashMap.get("email"));
		form.setField("untitled11", startDate);
		form.setField("untitled12", endDate);
		//        form.setField("untitled13", );
		//        form.setField("untitled14", );
		form.setField("untitled15", promoDescription);
		//        form.setField("untitled16", );
		//        form.setField("untitled17", );
		//        form.setField("untitled18", );
		//        form.setField("untitled19", );
		//        form.setField("untitled20", );
		//        form.setField("untitled21", );
		//        form.setField("untitled22", );
		//        form.setField("untitled23", );

		// filling out items fields
		String vlNum = "";
		String SKU = "";
		String type = "";
		String SRP = "";
		String normalCost = "";
		String promoPrice = "";
		String promoCost = "";
		String BER = "";

		for(int i = 0; i < items.size(); i++) {
			 vlNum += items.get(i).getVlNum() + "\n";
			 SKU += items.get(i).getSku().toString() + "\n";
			 type += items.get(i).getType() + "\n";
			 SRP += items.get(i).getSRP().toString() + "\n";
			 normalCost += items.get(i).getNormalCost().toString() + "\n";
			 promoPrice += items.get(i).getPromoPrice().toString() + "\n";
			 promoCost += items.get(i).getPromoCost().toString() + "\n";
			 BER += items.get(i).getBer().toString() + "\n";
		}

		form.setField("untitled24", vlNum);
		form.setField("untitled25", SKU);
		form.setField("untitled26", type);
		form.setField("untitled27", SRP);
		form.setField("untitled28", normalCost);
		form.setField("untitled29", promoPrice);
		form.setField("untitled30", promoCost);
		form.setField("untitled31", BER);



		stamper.close();
		reader.close();
	}

}
