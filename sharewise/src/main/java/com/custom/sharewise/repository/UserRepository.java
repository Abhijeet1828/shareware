package com.custom.sharewise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.sharewise.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
