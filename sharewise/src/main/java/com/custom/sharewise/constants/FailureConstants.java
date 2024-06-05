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
	UPDATE_USER_ERROR(-2003, "Error while updating user details"),
	PASSWORDS_DO_NOT_MATCH(-2004, "Passwords do not match"),
	ADD_USER_TO_GROUP_ERROR(-2005, "Error while adding user to the group"),
	ADD_ROLE_FOR_USER_ERROR(-2006, "Error while adding role to the user"),
	CREATE_GROUP_ERROR(-2007, "Error while creating group"),
	UPDATE_GROUP_ERROR(-2008, "Error while updating group details"),
	USER_NOT_GROUP_ADMIN(-2009, "User does not have privileges to edit the group details"),
	DELETE_GROUP_ERROR(-2010, "Error while deleting group"),
	GROUP_NOT_FOUND(-2011, "No existing group found"),
	USER_NOT_MAPPED_TO_GROUP(-2012, "User is not mapped to the group"),
	USER_NOT_FOUND(-2013, "User not found"),
	REMOVE_USER_FROM_GROUP_ERROR(-2014, "Error while removing user from the group"),
	ADD_GROUP_EXPENSE_ERROR(-2015, "Error while adding group expense"),
	ADD_GROUP_TRANSACTION_ERROR(-2016, "Error while adding group transaction"),
	GROUP_EXPENSE_NOT_FOUND(-2017, "Group expense not found"),
	DELETE_GROUP_TRANSACTIONS_ERROR(-2018, "Error while deleting group transactions"),
	UPDATE_GROUP_EXPENSE_ERROR(-2019, "Error while updating group expense"),
	DELETE_GROUP_EXPENSE_ERROR(-2020, "Error while deleting group expense"),
	RESTORE_GROUP_TRANSACTION_ERROR(-2021, "Error while restoring group transactions"),
	RESTORE_GROUP_EXPENSE_ERROR(-2022, "Error while restoring group expense"),
	USER_TRANSACTION_NOT_FOUND(-2023, "User transaction not found"),
	UPDATE_USER_TRANSACTION_ERROR(-2024, "Error while updating user transaction"),
	DELETE_USER_TRANSACTION_ERROR(-2025, "Error while deleting user transaction"),
	RESTORE_USER_TRANSACTION_ERROR(-2026, "Error while restoring user transaction"),
	SIMPLIFY_PAYMENTS_ERROR(-2027, "Error while fetching simplified payments"),
	FETCH_GROUP_MEMBERS_ERROR(-2028, "Error while fetching group members"),
	FETCH_PAYMENTS_SUMMARY_ERROR(-2029, "Error while fetching group payment summary");

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
