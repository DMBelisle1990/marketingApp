package com.vl.marketing.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputChecker {

	private static Pattern dateFormat = Pattern.compile("([0-9]{2}-){2}[0-9]{2}");
	
	public static boolean isValidDate(String date) {
		Matcher m = dateFormat.matcher(date);
		return m.matches();
	}
	
	
}
