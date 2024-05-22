package com.custom.sharewise.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseModel {

	@Column(name = "created_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimestamp;

	@Column(name = "modified_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

}
