package com.buaasimi.text;

import com.buaasimi.util.BSException;

public class ExtractException extends BSException {
	private String filePath;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtractException(String msg,String filePath) {
		super(msg);
		this.filePath = filePath;
		// TODO Auto-generated constructor stub
	}
	
	public ExtractException(String code, String msg,String filePath) {
		super(code, msg);
		this.filePath = filePath;
		// TODO Auto-generated constructor stub
	}

	public String getFilePath() {
		return filePath;
	}


}
