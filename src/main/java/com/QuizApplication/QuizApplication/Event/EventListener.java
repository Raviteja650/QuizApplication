package com.QuizApplication.QuizApplication.Event;


import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.token.VerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component

public class EventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender mailSender;
    private User user;

    @Autowired
    public EventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        user=event.getUser();

        String token= UUID.randomUUID().toString();

        verificationTokenService.saveVerificationTokenForUser(user,token);

        String url=event.getComformationUrl()+"/verifyEmail?token="+token;

        try{
            sendVerificationEmail(url);
        }catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendVerificationEmail(String url) throws MessagingException,UnsupportedEncodingException {

        String subject="Verification Email";
        String mailContent = "<p> Hi, "+ user.getFullname()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a  class=\"button-link\" href=\"" +url+ "\">Activate your account</a>"+
                "<p> Thank you <br> Online Quiz Application Service";
        emailMessage(subject,mailContent, mailSender, user);
    }

    private void emailMessage(String subject, String mailContent, JavaMailSender mailSender, User user) throws MessagingException,UnsupportedEncodingException {

        MimeMessage message=mailSender.createMimeMessage();
        var messageHelper=new MimeMessageHelper(message);
        messageHelper.setFrom("ravitejab650@gmail.com");
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent,true);
        mailSender.send(message);
    }
}
