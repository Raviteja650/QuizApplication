package com.QuizApplication.QuizApplication.service;

import com.QuizApplication.QuizApplication.dto.UserDto;
import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()) , "ADMIN", userDto.getFullname());
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteById(long theId) {
		userRepository.deleteById(theId);
	}

	@Override
	public User findById(long theId) {
		Optional<User> result =userRepository.findById(theId);

		User theEmployee = null;

		if (result.isPresent()) {
			theEmployee = result.get();
		}
		else {
			// we didn't find the employee
			throw new RuntimeException("Did not find employee id - " + theId);
		}

		return theEmployee;
	}

	@Override
	public void updateUser(UserDto userDto, Long id) {


		User existingEmployee = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

		existingEmployee.setFullname(userDto.getFullname());
		existingEmployee.setEmail(userDto.getEmail());
		existingEmployee.setRole(userDto.getRole());
		existingEmployee.setPassword(userDto.getPassword());

		userRepository.save(existingEmployee);
	}

	@Override
	public User findByUserName(String loggedInUser) {
		User user=userRepository.findByName(loggedInUser);
		return user;
	}

	@Override
	public boolean isEmailAlreadyExist(String email) {
		Optional<User> user= Optional.ofNullable(userRepository.findByEmail(email));
		return user.isPresent();
	}

	@Override
	public boolean isNameAlreadyExist(String fullname) {
		Optional<User> user= Optional.ofNullable(userRepository.findByName(fullname));
		return user.isPresent();
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean findNameExist(String fullname) {
		Optional<User> user= Optional.ofNullable(userRepository.findByName(fullname));
		if(user.isPresent())
			return true;
		return false;
	}


}
