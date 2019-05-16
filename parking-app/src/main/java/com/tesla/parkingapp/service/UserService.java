package com.tesla.parkingapp.service;

import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.web.UserRegistrationDto;

public interface UserService {
	 public User findUserByEmail(String email);
	 
	 public void saveUser(UserRegistrationDto user);
}
