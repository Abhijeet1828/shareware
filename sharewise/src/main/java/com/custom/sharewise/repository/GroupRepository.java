package com.custom.sharewise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

	Optional<Group> findFirstByGroupIdAndIsActiveTrue(Long groupId);

	boolean existsByGroupIdAndIsActiveTrue(Long groupId);

	boolean existsByGroupIdAndCreatedByAndIsActiveTrue(Long groupId, Long createdBy);

}
