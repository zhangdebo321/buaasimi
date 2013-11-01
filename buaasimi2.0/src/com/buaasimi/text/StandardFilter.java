package com.buaasimi.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandardFilter implements Filter {
	
	//将换行符替换为一个换行符
	private static String blank_reg = "[ \\t\\x0B\\f]{2,}";
	private static String newline_reg = "[ \\t\\x0B\\f\\r]*[\\n]+[ \\t\\x0B\\f\\r]*";

	@Override
	public String filtrate(String input) {
		// TODO Auto-generated method stub
		Matcher matcher = Pattern.compile(blank_reg).matcher(input);
		while(matcher.find())
			input = input.replaceAll(matcher.group(), " ");
		matcher = Pattern.compile(newline_reg).matcher(input);
		while(matcher.find())
			input = input.replaceAll(matcher.group(), "\n");
		return input;
	}

}
