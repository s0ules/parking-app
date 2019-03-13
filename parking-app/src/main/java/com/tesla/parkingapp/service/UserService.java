package com.tesla.parkingapp.service;

import com.tesla.parkingapp.model.User;

public interface UserService {
	 public User findUserByEmail(String email);
	 
	 public void saveUser(User user);
}
