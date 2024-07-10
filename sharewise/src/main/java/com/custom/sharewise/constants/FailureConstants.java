package com.custom.sharewise.constants;

/**
 * Enum for storing the failure HTTP codes and messages that will be used
 * in this application.
 * 
 * 
 * @author Abhijeet
 *
 */
public enum FailureConstants {

	INTERNAL_SERVER_ERROR(-1999, "Oops! Looks like something went wrong."),
	LOGIN_ERROR(-2001, "Error while logging in"),
	USER_ALREADY_EXISTS(-2002, "User already exists with the emailID"),
	PASSWORDS_DO_NOT_MATCH(-2004, "Passwords do not match"),
	USER_NOT_GROUP_ADMIN(-2009, "User does not have privileges to edit the group details"),
	GROUP_NOT_FOUND(-2011, "No existing group found"),
	USER_NOT_MAPPED_TO_GROUP(-2012, "User is not mapped to the group"),
	USER_NOT_FOUND(-2013, "User not found"),
	GROUP_EXPENSE_NOT_FOUND(-2017, "Group expense not found"),
	USER_TRANSACTION_NOT_FOUND(-2023, "User transaction not found");
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
