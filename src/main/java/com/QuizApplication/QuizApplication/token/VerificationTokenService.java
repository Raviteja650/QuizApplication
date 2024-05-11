package com.QuizApplication.QuizApplication.token;



import com.QuizApplication.QuizApplication.model.User;

import java.util.Optional;

public interface VerificationTokenService {

    String validateToken(String token);

    void saveVerificationTokenForUser(User user, String token);

    Optional<VerificationToken> findByToken(String token);

    void deleteUserToken(User user);

}
