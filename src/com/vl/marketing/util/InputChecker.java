package com.vl.marketing.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputChecker {

	private static Pattern dateFormat = Pattern.compile("([0-9]{2}-){2}[0-9]{2}");
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
	
	public static boolean isValidDate(String date) {
		Matcher m = dateFormat.matcher(date);
		return m.matches();
	}
	
	public static String formatDate(LocalDate date) {
		String[] splitDate = date.toString().split("-");
		String month = splitDate[1];
		String day = splitDate[2];
		
		if(month.substring(0, 1).equals("0")) month = month.substring(1);
		if(day.substring(0, 1).equals("0")) day = day.substring(1);

		return month + "/" + day + "/" + splitDate[0];
	}
	
	public static LocalDate formatDateString(String date) {
		return LocalDate.parse(date, formatter.withLocale(Locale.ENGLISH));
	}
	
	
}
