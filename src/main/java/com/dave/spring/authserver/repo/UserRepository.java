package com.dave.spring.authserver.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dave.spring.authserver.model.User;

@Component
@Service
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByEmail(String email);
}
