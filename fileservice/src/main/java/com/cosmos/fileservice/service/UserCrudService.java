package com.cosmos.fileservice.service;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cosmos.fileservice.domain.User;

@Repository
public interface UserCrudService extends CrudRepository<User, String> {
	
	Optional<User> findByUsername(String username);

}
