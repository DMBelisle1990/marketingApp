package com.vl.marketing.util;

import java.util.Random;

import com.vl.marketing.db.DBA;
import com.vl.marketing.model.Authorization;

/**
 * This class is used to generate fake data for testing purposes
 * Currently the list of companies is obtained from this class, move this functionality out
 */
public class DummyData {

	private String[] promos = {"BER", "PP", "COOP"};
	private static String[] companies = {"AAFES", "Amazon", "Amazon Canada", "Best Buy", "Best Buy.com", "BJs Wholesale Club",
										 "Bluestem Brands", "Frys Electronics", "HSN Direct International", "Ingram Micro",
										 "Kohls", "NEXCOM", "PriceSmart", "QVC", "Sears Holdings", "Sears Holdings/Kmart", 
										 "Synnex Canada", "Target.com", "Tiger Direct", "Walmart.com"};
	
	private String[] descriptions = {"Retail Ad 9D", "Retail Ad", "Description", "Retail", "9D @ $68"};
	private String[] states = {"pending", "rejected", "approved", "accepted", "running", "POP", "archived"};
	private DBA db = new DBA();
	
	
	public DummyData() {
		
	}
	
	public void populateTable(int numEntries) {
		for(int i = 0; i < numEntries; i++) {
			int day = randInt(1, 12);
			int month = randInt(1, 31);
			int year = randInt(2010, 2016);
			// This ugly block generates a random authorization
			db.addAuthorization(new Authorization(
				           companies[randInt(companies.length - 1)],
				           promos[randInt(promos.length - 1)],
				           descriptions[randInt(descriptions.length - 1)],
				           day + "/" + month + "/" + year,
				           (day/2 + 5) + "/" + (month/2 + 5) + "/" + (year + 1),
				           "R" + randInt(1000) + promos[randInt(promos.length - 1)] + randInt(1000),
				           randInt(1000) + promos[randInt(promos.length - 1)] + randInt(1000),
				           states[randInt(states.length - 1)],
				           randInt(200, 1000) * 1.0,
				           randInt(100, 2000) * 1.0));
		}
	}
	
	public static int randInt(int max) {
		return randInt(0, max);
	}
	
	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public static String[] getCompanies() {
		return companies;
	}
}

