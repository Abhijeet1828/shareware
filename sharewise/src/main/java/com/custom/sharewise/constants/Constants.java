package com.custom.sharewise.constants;

/**
 * This class stores the constants used in this application.
 * 
 * @author Abhijeet
 *
 */
public final class Constants {

	private Constants() {
		throw new IllegalStateException("Constants class cannot be instantiated");
	}

	// Version Constants
	public static final String API_VERSION_1 = "/api/v1";
	public static final String API_VERSION_2 = "/api/v2";

	// Controller Mapping Constants
	public static final String AUTHENTICATION_CONTROLLER = "/auth";
	public static final String USER_CONTROLLER = "/user";
	public static final String GROUP_CONTROLLER = "/group";
	public static final String GROUP_MEMBER_CONTROLLER = "/group-member";
	public static final String EXPENSES_CONTROLLER = "/expenses";
	public static final String TRANSACTIONS_CONTROLLER = "/transactions";
	public static final String PAYMENT_CONTROLLER = "/payment";
	public static final String SUMMARY_CONTROLLER = "/summary";

	// User Roles
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	// Validation Type Constants
	public static final String VALIDATION_USER_ID = "userId";
	public static final String VALIDATION_GROUP_ID = "groupId";
	public static final String VALIDATION_GROUP_ADMIN = "groupAdmin";
	public static final String VALIDATION_USER_GROUP = "userGroup";

	// Transaction Types
	public static final String TRANSACTION_TYPE_GROUP_EXPENSE = "GROUP_EXPENSE";
	public static final String TRANSACTION_TYPE_USER_PAYMENT = "USER_PAYMENT";

}
