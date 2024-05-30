package com.custom.sharewise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.UserGroupMapping;

@Repository
public interface UserGroupMappingRepository extends JpaRepository<UserGroupMapping, Long> {

	Optional<UserGroupMapping> findFirstByGroupIdAndUserIdAndIsRemovedFalse(Long groupId, Long userId);

	boolean existsByGroupIdAndUserIdAndIsRemovedFalse(Long groupId, Long userId);
	
	Optional<UserGroupMapping> findFirstByGroupIdAndUserId(Long groupId, Long userId);

}
