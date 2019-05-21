package com.tesla.parkingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tesla.parkingapp.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
	@Query("SELECT email FROM User where email like %:keyword%")
	public List<String> search(@Param("keyword") String email);
}
