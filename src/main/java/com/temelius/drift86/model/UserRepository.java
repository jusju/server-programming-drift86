package com.temelius.drift86.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
	User findByUsername(String username);
	List<User> findAllByOrderByRoleAsc();
	}
