package com.QuizApplication.QuizApplication.token;


import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public VerificationToken generareNewToken(String oldToken) {
        VerificationToken verificationToken=verificationTokenRepo.find(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusMinutes(3);

        Date expirationDate = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());
        verificationToken.setExpirationTime(expirationDate);
        verificationTokenRepo.save(verificationToken);
        return  verificationToken;
    }

    @Override
    public String getTokenByUser(Long id) {
        return verificationTokenRepo.getTokenByUser(id);
    }


}
