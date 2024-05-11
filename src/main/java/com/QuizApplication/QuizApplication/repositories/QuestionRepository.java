package com.QuizApplication.QuizApplication.repositories;


import com.QuizApplication.QuizApplication.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question,String> {

    @Query("SELECT DISTINCT q.subject FROM Question q")
    List<String> findDistinctSubject();

    @Query(value = "SELECT * FROM questions WHERE subject = ?1 ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    List<Question> findRandomQuestionsBySubject(String subject, int numberOfQuestions);


    Page<Question> findBySubject(String subject, Pageable pageable);

    @Query(value = "SELECT * FROM question WHERE question = ?1", nativeQuery = true)
    Question findBySubjectId(String questionText);

    @Query(value = "SELECT correct_answers from question_correct_answers where question_id = ?1", nativeQuery = true)
    String findSubjectAnswer(String questionId);
}
