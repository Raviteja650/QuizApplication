package com.QuizApplication.QuizApplication.repositories;

import com.QuizApplication.QuizApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	User findByEmail(String email);

	@Query(value = "select * from users where fullname = ?1",nativeQuery = true)
	User findByName(String fullname);

}
