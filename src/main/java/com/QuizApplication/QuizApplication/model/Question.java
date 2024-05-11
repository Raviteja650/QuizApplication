package com.QuizApplication.QuizApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotBlank
    private String question;
    @NotBlank
    private String subject;
    @NotBlank
    private String questionType;


    @ElementCollection
    private List<String> choices;


    @ElementCollection
    private List<String> correctAnswers;

    public Question()
    {
        super();
    }

    public Question(String question, String subject, String questionType, List<String> choices, List<String> correctAnswers) {
        this.question = question;
        this.subject = subject;
        this.questionType = questionType;
        this.choices = choices;
        this.correctAnswers = correctAnswers;
    }
}
