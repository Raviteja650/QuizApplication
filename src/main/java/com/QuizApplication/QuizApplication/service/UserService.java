package com.QuizApplication.QuizApplication.service;



import com.QuizApplication.QuizApplication.dto.UserDto;
import com.QuizApplication.QuizApplication.model.User;

import java.util.List;

public interface UserService {
	
	User save (UserDto userDto);


    List<User> findAll();

    void deleteById(long theId);

    User findById(long theId);

    void updateUser(UserDto userDto, Long id);

    User findByUserName(String loggedInUser);

    boolean isEmailAlreadyExist(String email);

    boolean isNameAlreadyExist(String fullname);

    User findByEmail(String email);

    boolean findNameExist(String fullname);
}
