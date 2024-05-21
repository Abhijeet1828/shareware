package com.custom.sharewise.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public class BaseModel {

	@Column(name = "created_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimestamp;

	@Column(name = "modified_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

}
