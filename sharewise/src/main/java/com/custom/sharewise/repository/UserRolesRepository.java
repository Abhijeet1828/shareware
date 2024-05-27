package com.custom.sharewise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.UserRoles;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

}
