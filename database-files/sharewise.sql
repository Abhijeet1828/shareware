CREATE TABLE `user` (
	`user_id` INT AUTO_INCREMENT,
	`user_password` VARCHAR(255),
	`first_name` VARCHAR(100),
	`last_name` VARCHAR(100),
	`mobile_no` BIGINT NOT NULL,
	`email` VARCHAR(100) NOT NULL,
	`created_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`user_id`)
);

CREATE TABLE `user_groups` (
	`group_id` INT AUTO_INCREMENT,
	`name` VARCHAR(500) NOT NULL,
	`description` MEDIUMTEXT,
	`is_active` BOOLEAN DEFAULT TRUE,
	`created_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`group_id`)
);

CREATE TABLE `user_group_mapping` (
	`user_group_mapping_id` INT AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`group_id` INT NOT NULL,
	`is_removed` BOOLEAN DEFAULT FALSE,
	`created_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`user_group_mapping_id`)
);

CREATE TABLE `group_expenses` (
	`group_expenses_id` INT AUTO_INCREMENT,
	`group_id` INT NOT NULL,
	`paid_by` INT NOT NULL,
	`split_between` VARCHAR(500) NOT NULL,
	`total_amount` FLOAT(11,3) NOT NULL,
	`category` VARCHAR(100),
	`is_deleted` BOOLEAN DEFAULT FALSE,
	`created_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`group_expenses_id`)
);

CREATE TABLE `group_transactions` (
	`group_transactions_id` INT AUTO_INCREMENT,
	`group_id` INT NOT NULL,
	`group_expenses_id` INT,
	`paid_by` INT NOT NULL,
	`paid_to` INT NOT NULL,
	`amount` FLOAT(11,3) NOT NULL,
	`transaction_type` VARCHAR(20),
	`is_deleted` BOOLEAN,
	`created_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`group_transactions_id`)
);

CREATE TABLE `master_tx_details` (
	`master_tx_details_id` INT AUTO_INCREMENT,
	`group_id` INT,
	`created_by` INT,
	`request_type` VARCHAR(100),
	`group_expenses_id` INT(20),
	`group_transactions_id` INT(20),
	`request` LONGTEXT,
	`created_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`modified_timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`master_tx_details_id`)
);