package com.custom.sharewise.constants;

/**
 * Enum for storing the common failure HTTP codes and messages that can be used
 * across Spring Boot projects.
 * 
 * 
 * @author Abhijeet
 *
 */
public enum FailureConstants {

	INTERNAL_SERVER_ERROR(-1999, "Oops! Looks like something went wrong."),
	SIGN_UP_ERROR(-2000, "Error while creating user"),
	LOGIN_ERROR(-2001, "Error while logging in"),
	USER_ALREADY_EXISTS(-2002, "User already exists with the emailID"),
	UPDATE_USER_ERROR(-2003, "Error while updating user details");

	private final int failureCode;
	private final String failureMsg;

	private FailureConstants(int failureCode, String failureMsg) {
		this.failureCode = failureCode;
		this.failureMsg = failureMsg;
	}

	public int getFailureCode() {
		return failureCode;
	}

	public String getFailureMsg() {
		return failureMsg;
	}

	@Override
	public String toString() {
		return Integer.toString(failureCode) + "-" + failureMsg;
	}

}
