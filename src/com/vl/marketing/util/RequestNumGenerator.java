package com.vl.marketing.util;

import java.util.LinkedHashMap;

public class RequestNumGenerator {

	private static LinkedHashMap<String, String> clientAbbreviations = new LinkedHashMap<String, String>();
	private static LinkedHashMap<String, String> promoAbbreviations = new LinkedHashMap<String, String>();
	static {
		clientAbbreviations.put("Best Buy","BBY");
		clientAbbreviations.put("Frys Electronics", "FR");
		clientAbbreviations.put("aafes", "AAE");
		clientAbbreviations.put("tiger", "TD");
		clientAbbreviations.put("Wintec", "WI");
		clientAbbreviations.put("Target", "TG");
		clientAbbreviations.put("Walmart", "WM");
		clientAbbreviations.put("Ingram Micro", "IM");
		clientAbbreviations.put("bjs.com", "BJ");
		clientAbbreviations.put("K Mart Corporation", "KM");
		clientAbbreviations.put("Sears US Stores", "SHCS");
		clientAbbreviations.put("Nexcom", "NV");
		clientAbbreviations.put("Nomorerack.com", "NMR");
		promoAbbreviations.put("Back End Rebate", "BER");
		promoAbbreviations.put("Price Protection", "PP");
		promoAbbreviations.put("Back End Rebate", "BER");
		promoAbbreviations.put("Coop", "COOP");
	}
	
	public static String generate(String company, String promo, String startDate) {
		return "R" + clientAbbreviations.get(company) + (promo == null ? "" : promoAbbreviations.get(promo))  
				   + startDate.replace("-", "");
	}
}
