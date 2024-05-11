package com.QuizApplication.QuizApplication.Event;


import com.QuizApplication.QuizApplication.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String comformationUrl;

    public RegistrationCompleteEvent(User user, String comformationUrl) {
        super(user);
        this.user = user;
        this.comformationUrl = comformationUrl;
    }

}
