package com.custom.sharewise.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByUserId(Long userId);

	@Query(value = "SELECT new com.custom.sharewise.dto.UserDto(u.userId, u.firstName) FROM User u WHERE u.userId IN :userIds")
	List<UserDto> findUsersByIdList(@Param("userIds") Set<Long> userIds);

	default Map<Long, UserDto> findUsersByIdListAsMap(Set<Long> userIds) {
		return findUsersByIdList(userIds).stream().collect(Collectors.toMap(UserDto::userId, Function.identity()));
	}
}
