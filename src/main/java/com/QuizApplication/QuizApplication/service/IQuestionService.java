package com.QuizApplication.QuizApplication.service;



import com.QuizApplication.QuizApplication.model.Question;

import java.util.List;
import java.util.Optional;


public interface IQuestionService {

    void saveQuestion(Question question);


    List<String> getPreviousSubjects();

    List<Question> getAllQuestions();


    void deleteQuestion(String id);

    Optional<Question> getQuestionById(String id);

    void updateQuestion(String id,String question, List<String> choices, List<String> correctAnswers);

    List<Question> getQuestionsForUser(Integer numOfQuestions, String subject);


    String getCorrectAnswer(String questionText);
}
