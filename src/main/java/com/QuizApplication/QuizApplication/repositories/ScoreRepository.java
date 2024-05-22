package com.QuizApplication.QuizApplication.repositories;


import com.QuizApplication.QuizApplication.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Result,Long> {

    @Query(value = "select * from result where user_name = ?1 order by user_id desc", nativeQuery = true)
    List<Result> findAllByName(String loggedInUser);

    @Transactional
    @Modifying
    @Query(value = "delete from result where user_name = ?1",nativeQuery = true)
    void deleteUser(String fullname);
}
