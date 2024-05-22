package com.QuizApplication.QuizApplication.service;



import com.QuizApplication.QuizApplication.model.Result;

import java.util.List;

public interface ScoreService {
    void saveScore(String score, String loggedInUser, String subject, int questions);

    List<Result> getHistroyForUser(String loggedInUser);

    void updateUserName(String userName, String fullname);

    void deleteuser(String fullname);
}
