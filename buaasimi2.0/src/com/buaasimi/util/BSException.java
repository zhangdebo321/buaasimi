package com.buaasimi.util;

public class BSException extends Exception {
	
	private String errorCode;
	private String errorMsg;
	
	public BSException(String msg){
		this.errorMsg = msg;
	}
	
	public BSException(String code, String msg){
		this.errorCode = code;
		this.errorMsg = msg;
	}
	
	public String toString(){
		if(this.errorCode != null)
			return this.errorCode + " " + this.errorMsg;
		return this.errorMsg;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3171737890257145294L;

}
