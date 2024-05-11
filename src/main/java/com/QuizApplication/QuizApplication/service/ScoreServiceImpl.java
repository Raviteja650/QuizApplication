package com.QuizApplication.QuizApplication.service;


import com.QuizApplication.QuizApplication.model.Result;
import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.repositories.ScoreRepository;
import com.QuizApplication.QuizApplication.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {


    private final UserService userService;

    private ScoreRepository scoreRepository;

    public ScoreServiceImpl(UserService userService, ScoreRepository scoreRepository) {
        this.userService = userService;
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void saveScore(String score, String loggedInUser, String subject, int questions) {

        User user=userService.findByUserName(loggedInUser);

        Result result=new Result();
        result.setScore(score);
        result.setSubject(subject);
        result.setNumberOfQuestions(questions);
        result.setDateTime(LocalDateTime.now());
        result.setUser_name(user.getFullname());

        scoreRepository.save(result);

    }

    @Override
    public List<Result> getHistroyForUser(String loggedInUser) {

        return scoreRepository.findAllByName(loggedInUser);


    }

    @Override
    public void updateUserName(String userName, String fullname) {

        List<Result> results=scoreRepository.findAllByName(userName);

        for(Result result:results)
        {
            result.setUser_name(fullname);
            scoreRepository.save(result);
        }
    }


}
