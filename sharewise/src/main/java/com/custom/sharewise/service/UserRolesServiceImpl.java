package com.custom.sharewise.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.sharewise.model.UserRoles;
import com.custom.sharewise.repository.UserRolesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager")
public class UserRolesServiceImpl implements UserRolesService {

	private final UserRolesRepository userRolesRepository;

	@Override
	public void addRoleForUser(Long userId, String role) {
		UserRoles userRoles = UserRoles.builder().userId(userId).role(role).build();
		userRoles.setCreatedTimestamp(new Date());
		userRoles.setModifiedTimestamp(new Date());

		userRolesRepository.save(userRoles);
	}

}
