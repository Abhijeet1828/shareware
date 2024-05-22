package com.custom.sharewise.constants;

/**
 * Enum for storing the common success HTTP codes and messages that can be used
 * across Spring Boot projects.
 * 
 * @author Abhijeet
 *
 */
public enum SuccessConstants {

	USER_SIGN_UP(2000, "User signed up successfully"),
	USER_LOGIN(2001, "User logged in successfully");

	private final int successCode;
	private final String successMsg;

	private SuccessConstants(int successCode, String successMsg) {
		this.successCode = successCode;
		this.successMsg = successMsg;
	}

	public int getSuccessCode() {
		return successCode;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	@Override
	public String toString() {
		return Integer.toString(successCode) + "-" + successMsg;
	}

}
