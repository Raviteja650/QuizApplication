package com.QuizApplication.QuizApplication.controller;


import com.QuizApplication.QuizApplication.dto.UserDto;
import com.QuizApplication.QuizApplication.model.Question;
import com.QuizApplication.QuizApplication.model.Result;
import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.service.IQuestionService;
import com.QuizApplication.QuizApplication.service.ScoreService;
import com.QuizApplication.QuizApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@Slf4j
public class QuestionController {


    private final IQuestionService questionService;

    private UserService userService;

    private final ScoreService scoreService;

    public QuestionController(IQuestionService questionService, UserService userService, ScoreService scoreService) {
        this.questionService = questionService;
        this.userService = userService;
        this.scoreService = scoreService;
    }
    @GetMapping("/add")
    public String showAddQuestionForm(@ModelAttribute("loggedInUser") String loggedInUser,Model model) {
        log.info("displayed");

        List<String> choices = Arrays.asList("Choice 1", "Choice 2", "Choice 3", "Choice 4");
        List<String> subjectOptions = questionService.getPreviousSubjects();
        model.addAttribute("subjectOptions", subjectOptions);

        Question question = new Question();
        question.setQuestion("Sample Question");
        question.setSubject("Sample Subject");
        question.setQuestionType("Sample Type");
        question.setChoices(choices);
        model.addAttribute("question", question);
        return "Addquestion";
    }

    @PostMapping("/saveQuestion")
    public String saveQuestion(@ModelAttribute Question question) {
        // Call the service layer to save the question
        log.info("saved method");
        System.out.println(question.getQuestionType()+" "+question.getChoices()+" "+question.getSubject()+" "+question.getCorrectAnswers()+" "+question.getQuestion());
        questionService.saveQuestion(question);
        log.info("saved");
        return "redirect:/allQuestion";
    }


    @GetMapping("/allQuestion")
    public String getAllQuestions(@ModelAttribute("loggedInUser") String loggedInUser,Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        //model.addAttribute("isQuestionDeleted", false);
        return "GetAllQuestions";
    }
    @GetMapping("/after")
    public String after()
    {
        return "after";
    }


    @GetMapping("/delete-question")
    public String deleteQuestion(@RequestParam String id) {
        questionService.deleteQuestion(id);
        return "redirect:/allQuestion";
    }


    @GetMapping("/edit-question")
    public String showUpdateForm(@RequestParam String id, Model model) {
        Optional<Question> question = questionService.getQuestionById(id);
        if(question.isPresent()) {
            model.addAttribute("question", question.get());
            model.addAttribute("choices", question.get().getChoices());
            return "updateQuestion";
        }
        else
            return "redirect:/allQuestion";

    }

    @PostMapping("/update-question")
    public String updateQuestion(@ModelAttribute("question") Question question) {
        questionService.updateQuestion(question.getId(),question.getQuestion(),question.getChoices(),question.getCorrectAnswers());
        return "redirect:/allQuestion";
    }

    @GetMapping("/takeQuiz")
    public String takeQuiz(Model model)
    {
        List<String> subjectOptions = questionService.getPreviousSubjects();
        model.addAttribute("subjectOptions", subjectOptions);
        return "takeQuiz";
    }

//    @GetMapping("/quizPage")
//    public String getQuestionsForUser(@RequestParam String subject,
//            @RequestParam(required = false) Integer numberOfQuestions, Model model){
//
//        System.out.println(numberOfQuestions);
//        log.info(String.valueOf(numberOfQuestions));
//        if(numberOfQuestions==null)
//            return "redirect:/takeQuiz";
//
//        List<Question> allQuestions = questionService.getQuestionsForUser(numberOfQuestions, subject);
//
//        List<Question> mutableQuestions = new ArrayList<>(allQuestions);
//        Collections.shuffle(mutableQuestions);
//
//        int availableQuestions = Math.min(numberOfQuestions, mutableQuestions.size());
//        System.out.println(availableQuestions);
//        List<Question> questions = mutableQuestions.subList(0, availableQuestions);
//        model.addAttribute("questions", questions);
//        model.addAttribute("subject", subject);
//        return "quizPage";
//    }

    @GetMapping("/getCorrectAnswer")
    @ResponseBody
    public String getCorrectAnswer(@RequestParam String questionText) {
        return questionService.getCorrectAnswer(questionText);
    }

    @GetMapping("/result")
    public String save_Score(@RequestParam String score,@RequestParam String loggedInUser,@RequestParam String subject,@RequestParam
    int Questions,Model model)
    {
        //return questionService.saveScore(score);
        model.addAttribute("score", score);
        model.addAttribute("user",loggedInUser);
        model.addAttribute("subject",subject);
        model.addAttribute("Questions",Questions);
        scoreService.saveScore(score,loggedInUser,subject,Questions);
        return "score-page";
    }

    @GetMapping("/history")
    public String history(Model model,@RequestParam("loggedInUser") String loggedInUser)
    {
        List<Result> results=scoreService.getHistroyForUser(loggedInUser);
        model.addAttribute("scoreHistory",results);
        return "history";
    }

    @GetMapping("/quizPage")
    public String getQuestionsForUser(@RequestParam String subject,
                                      @RequestParam(required = false) Integer numberOfQuestions, Model model,@ModelAttribute("loggedInUser") String loggedInUser){

        System.out.println(numberOfQuestions);
        log.info(String.valueOf(numberOfQuestions));
        if(numberOfQuestions==null)
            return "redirect:/takeQuiz";

        List<Question> allQuestions = questionService.getQuestionsForUser(numberOfQuestions, subject);

        List<Question> mutableQuestions = new ArrayList<>(allQuestions);
        Collections.shuffle(mutableQuestions);

        int availableQuestions = Math.min(numberOfQuestions, mutableQuestions.size());
        System.out.println(availableQuestions);
        List<Question> questions = mutableQuestions.subList(0, availableQuestions);
        model.addAttribute("questions", questions);
        model.addAttribute("availableQuestions",availableQuestions);
        model.addAttribute("subject", subject);
        return "quizPage";
    }

    @GetMapping("/allUsers")
    public String listEmployees(@ModelAttribute("loggedInUser") String loggedInUser,Model theModel) {

        // get the employees from db
        List<User> theEmployees =userService.findAll();

        // add to the spring model
        theModel.addAttribute("employees", theEmployees);

        return "AllUsers";
    }

//    @GetMapping("/user_Page")
//    public String NavBar(@ModelAttribute("loggedInUser") String loggedInUser) {
//        return "NavBar";
//    }

    @GetMapping("/user_Page")
    public String NavBar(Model model) {
        String loggedInUser = getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        return "NavBar";
    }

    @ModelAttribute("loggedInUser")
    public String getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String username = userDetails.getUsername();
            log.info("Attempting to find user with username '{}'", username);
            User user = userService.findByUserName(username);
            if (user != null) {
                log.info("User found with fullname '{}'", user.getFullname());
                return user.getFullname();
            } else {
                log.error("User with username '{}' not found in the database.", username);
                return "User not found"; // or handle appropriately
            }
        }
        return null;
    }
    @GetMapping("/admin_nav")
    public String admin_nav()
    {
        return "Admin_NavBar";
    }

    @GetMapping("/success")
    public String success(@ModelAttribute("loggedInUser") String loggedInUser)
    {
        return "success";
    }



    @GetMapping("/updateForm")
    public String showForm(@ModelAttribute("loggedInUser") String loggedInUser,@RequestParam("user") String name,Model model)
    {
        User user=userService.findByUserName(name);
        model.addAttribute("loggedInUser",loggedInUser);
        model.addAttribute("user",user);
        return "user-update-form";
    }

    @PostMapping("/updateUser")
    public String update(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes)
    {

        User user1=userService.findByEmail(user.getEmail());
        String user_name=user1.getFullname();
        log.info(user_name);
        UserDto userDto=new UserDto();
        userDto.setEmail(user1.getEmail());
        userDto.setRole(user1.getRole());
        userDto.setPassword(user1.getPassword());
        if(userService.findNameExist(user.getFullname()))
        {
            redirectAttributes.addFlashAttribute("message","Already name exists. please enter a new fullname.");
            return "redirect:/updateForm?user=" + user_name;
        }
        userDto.setFullname(user.getFullname());

        userService.updateUser(userDto,user.getId());
        scoreService.updateUserName(user_name,userDto.getFullname());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User updatedUser = userService.findByUserName(userDto.getFullname());
        UserDetails updatedUserDetails = new org.springframework.security.core.userdetails.User(
                updatedUser.getFullname(), updatedUser.getPassword(), authentication.getAuthorities());
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);


        redirectAttributes.addFlashAttribute("message","Changes are done");
        return "redirect:/updateForm?user=" + userDto.getFullname();

    }





}
