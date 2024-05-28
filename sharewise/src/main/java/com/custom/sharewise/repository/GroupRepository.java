package com.custom.sharewise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	
}
