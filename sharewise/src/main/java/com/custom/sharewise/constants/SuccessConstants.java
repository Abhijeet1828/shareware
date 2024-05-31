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
	USER_LOGIN(2001, "User logged in successfully"),
	UPDATE_USER(2002, "User details updated successfully"),
	UPDATE_PASSWORD(2003, "Password updated successfully"),
	CREATE_GROUP(2004, "Group created successfully"),
	UPDATE_GROUP(2005, "Group details updated successfully"),
	DELETE_GROUP(2006, "Group deleted successfully"),
	ADD_MEMBER_TO_GROUP(2007, "User added to the group successfully"),
	REMOVE_MEMBER_FROM_GROUP(2008, "User removed from the group successfully"),
	ADD_GROUP_EXPENSE(2009, "Group expense added successfully"),
	ADD_USER_TRANSACTION(2010, "User transaction added successfully"),
	UPDATE_GROUP_EXPENSE(2011, "Group expense updated successfully");

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
