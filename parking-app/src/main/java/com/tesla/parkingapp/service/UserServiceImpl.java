package com.tesla.parkingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tesla.parkingapp.model.Role;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.repository.RoleRepository;
import com.tesla.parkingapp.repository.UserRepository;
import com.tesla.parkingapp.web.UserRegistrationDto;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRespository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(UserRegistrationDto userDto) {
		String role = "SITE_USER";
		if (userDto.getCompany())
			role = "FIRMA_USER";
		
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

		// user.setActive(1);
		Role userRole = roleRespository.findByRole(role);
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
}
