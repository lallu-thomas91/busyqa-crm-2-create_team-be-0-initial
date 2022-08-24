package com.busyqa.coop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busyqa.coop.jpa.User;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
	
	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
}
