package com.custom.sharewise.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "user_roles")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoles extends BaseModel implements Serializable {

	private static final long serialVersionUID = 4704137515716979498L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_roles_id")
	private Long userRolesId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "role", nullable = false)
	private String role;

}
