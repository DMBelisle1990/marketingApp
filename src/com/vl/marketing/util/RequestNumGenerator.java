package com.vl.marketing.util;

import java.util.LinkedHashMap;

public class RequestNumGenerator {
	
	private static LinkedHashMap<String, String> clientAbbreviations = new LinkedHashMap<String, String>();
	static {
		clientAbbreviations.put("Best Buy","BBY");
		clientAbbreviations.put("Frys Electronics", "FR");
		clientAbbreviations.put("AAFES", "AAE");
		clientAbbreviations.put("Tiger Direct", "TD");
		clientAbbreviations.put("Target.com", "TG");
		clientAbbreviations.put("Walmart.com", "WM");
		clientAbbreviations.put("Ingram Micro", "IM");
		clientAbbreviations.put("bjs.com", "BJ");
		clientAbbreviations.put("K Mart Corporation", "KM");
		clientAbbreviations.put("Sears US Stores", "SHCS");
		clientAbbreviations.put("NEXCOM", "NV");
		clientAbbreviations.put("Nomorerack.com", "NMR");
	}
	
	public static String generate(String company, String promo, String startDate) {
		String abbr = clientAbbreviations.get(company) == null ? company.substring(0, 2).toUpperCase() : clientAbbreviations.get(company);
		return "R" + abbr + (promo == null ? "" : promo)  
				   + startDate.replace("-", "").substring(2)
				   + "-0#";
	}
}
