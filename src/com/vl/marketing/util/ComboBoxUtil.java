package com.vl.marketing.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.vl.marketing.model.Authorization;

import javafx.collections.ObservableList;

public class ComboBoxUtil {

	
	Set<String> customers = new LinkedHashSet<String>();
	Set<String> vlMarketingNums = new LinkedHashSet<String>();
	Set<String> marketingNums = new LinkedHashSet<String>();
	
	public ComboBoxUtil(ObservableList<Authorization> authorizations) {
		customers.add("All Companies");
		for(Authorization auth : authorizations) {
			customers.add(auth.getCompany());
			vlMarketingNums.add(auth.getVlMarketingNum());
			marketingNums.add(auth.getMarketingNum());
		}
	}
	
	public Set<String> getCustomers() {
		return customers;
	}
	
	public Set<String> getVlMarketingNums() {
		return vlMarketingNums;
	}
	
	public Set<String> getMarketingNums() {
		return marketingNums;
	}
	
	public Set<String> getPromoTypes() {
		Set<String> promoTypes = new HashSet<String>(Arrays.asList("BER", "PP", "COOP"));
		return promoTypes;
	}
	
	public Set<String> getStates() {
		Set<String> states = new HashSet<String>(Arrays.asList("pending", "rejected", "approved", "accepted", "running", "POP", "archived"));
		return states;
	}
	
	
	public boolean isBefore(String dbDate, String calendarDate) {
		String[] dbDateArray = dbDate.split("/");
		String[] calendarDateArray = calendarDate.replace("-", "/").split("/");
		
		// Swap to yyyy/mm/dd format for proper priority checking
		String year = dbDateArray[2];
		dbDateArray[2] = dbDateArray[1];
		dbDateArray[1] = dbDateArray[0];
		dbDateArray[0] = year;
		
		int int1, int2;
		for(int i = 0; i <= 2; i++) {
			int1 = Integer.parseInt(dbDateArray[i]);
			int2 = Integer.parseInt(calendarDateArray[i]);
			if(int1 != int2) {
				return int1 < int2 ? true : false;
			}
		}
		return true;
	}
}
