package com.QuizApplication.QuizApplication.service;



import com.QuizApplication.QuizApplication.model.Question;
import com.QuizApplication.QuizApplication.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService implements IQuestionService{

    private final QuestionRepository questionRepository;

    @Override
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public List<String> getPreviousSubjects() {
        return questionRepository.findDistinctSubject();
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Optional<Question> getQuestionById(String id) {
        return questionRepository.findById(id);
    }

    @Override
    public void updateQuestion(String id,String question, List<String> choices, List<String> correctAnswers) {

        Question question1=questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found with id: " + id));

        question1.setQuestion(question);
        question1.setChoices(choices);
        question1.setCorrectAnswers(correctAnswers);

        questionRepository.save(question1);
    }

    @Override
    public List<Question> getQuestionsForUser(Integer numOfQuestions, String subject) {
        Pageable pageable = PageRequest.of(0, numOfQuestions);
        return questionRepository.findBySubject(subject, pageable).getContent();
    }

    @Override
    public String getCorrectAnswer(String questionText) {

        Question questionId=questionRepository.findBySubjectId(questionText);
        System.out.println(questionId.getId()+" "+questionId.getQuestion()+" "+questionId.getChoices()+" "+questionId.getCorrectAnswers());
        String id=questionId.getId();
        log.info(id);

        String correctAnswer=questionRepository.findSubjectAnswer(id);
        log.info(correctAnswer);

        return correctAnswer;
    }

}
