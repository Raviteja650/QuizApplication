package com.QuizApplication.QuizApplication.token;


import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerficationTokenServiceImpls implements VerificationTokenService{

    private final VerificationTokenRepo verificationTokenRepo;
    private final UserRepository userRepository;
    @Override
    public String validateToken(String token) {

        Optional<VerificationToken> verificationToken=verificationTokenRepo.findByToken(token);
        if(verificationToken.isEmpty())
            return "Invalid";
        User user=verificationToken.get().getUser();
        Calendar calendar=Calendar.getInstance();
        if( (verificationToken.get().getExpirationTime().getTime()- calendar.getTime().getTime())<=0) {
            verificationTokenRepo.deleteToken(token);
            return "Expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "VALID";
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken=new VerificationToken(user,token);
        verificationTokenRepo.save(verificationToken);

    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepo.findByToken(token);
    }

    @Override
    public void deleteUserToken(User user) {
        verificationTokenRepo.deleteById(user.getId());
    }


}
