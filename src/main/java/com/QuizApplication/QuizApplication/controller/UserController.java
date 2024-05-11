package com.QuizApplication.QuizApplication.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.QuizApplication.QuizApplication.Event.RegistrationCompleteEvent;
import com.QuizApplication.QuizApplication.dto.UserDto;
import com.QuizApplication.QuizApplication.model.User;
import com.QuizApplication.QuizApplication.service.IQuestionService;
import com.QuizApplication.QuizApplication.service.UserService;
import com.QuizApplication.QuizApplication.token.VerificationToken;
import com.QuizApplication.QuizApplication.token.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class UserController {
	
	@Autowired
	UserDetailsService userDetailsService;

	private final IQuestionService questionService;

	private final ApplicationEventPublisher publisher;

	private final VerificationTokenService verificationTokenService;
	
	@Autowired
	private UserService userService;

	public UserController(IQuestionService questionService, ApplicationEventPublisher publisher, VerificationTokenService verificationTokenService) {
		this.questionService = questionService;
		this.publisher = publisher;
		this.verificationTokenService = verificationTokenService;
	}

	private static final String EMAIL_REGEX =
			"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
					"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	public static boolean isValidEmail(String email) {
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}
	@GetMapping("/")
	public String intro_Page()
	{
		return "IntroPage";
	}

	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "register";
	}
	
	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto, RedirectAttributes redirectAttributes, Model model, final HttpServletRequest request) {
		String[] email=userDto.getEmail().split("@");
		String suffix=email[1];
		System.out.println(email[0]+"   "+email[1]);
		if(userService.isEmailAlreadyExist(userDto.getEmail()) || !suffix.equalsIgnoreCase("gmail.com"))
		{
			redirectAttributes.addFlashAttribute("message", "This email is already registered.");
			return "redirect:/registration";
		}
		else if(userService.isNameAlreadyExist(userDto.getFullname()))
		{
			redirectAttributes.addFlashAttribute("message", "This name is already exist.");
			return "redirect:/registration";
		}
		else if(!isValidEmail(userDto.getEmail()))
		{
			redirectAttributes.addFlashAttribute("message", "Please enter a valid email address.");
			return "redirect:/registration";
		}
		else {
			userService.save(userDto);

			User user=userService.findByEmail(userDto.getEmail());
			publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
			redirectAttributes.addFlashAttribute("message", "Registration Successful, please check your mail to activate your account.");
			return "redirect:/registration";
		}
	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://"+
				request.getServerName()+
				":"+
				request.getServerPort()+
				request.getContextPath();
	}

	@GetMapping("/verifyEmail")
	public String verifyEmail(@RequestParam("token") String token, Model model,RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		Optional<VerificationToken> verificationToken = verificationTokenService.findByToken(token);
		if (verificationToken.isPresent() && verificationToken.get().getUser().isEnabled()) {
			String encodedMessage = URLEncoder.encode("Your account is now activated. You can login now.", StandardCharsets.UTF_8.toString());
			return "redirect:/login?message=" + encodedMessage;
		}
		String verificationResult = verificationTokenService.validateToken(token);
		switch (verificationResult.toLowerCase()) {
			case "expired":
				model.addAttribute("message", "The verification token has expired. Please try again or contact support for assistance.");
				return "redirect:/error";
			case "valid":
				String encodedMessage = URLEncoder.encode("Your account is now activated. You can login now.", StandardCharsets.UTF_8.toString());
				return "redirect:/login?message=" + encodedMessage;
			default:
				model.addAttribute("message", "The verification token has been Invalid. Please try again or contact support for assistance.");
				return "redirect:/error";
		}
	}



	@GetMapping("/error")
	public String errorForm(@RequestParam("message") String message, Model model)
	{
		model.addAttribute("message",message);
		return "error-page";
	}


	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	


	@GetMapping("/access-denied")
	public String accessPage()
	{
		return "accessed-denied";
	}






	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") long theId) {
		User user=userService.findById(theId);
		verificationTokenService.deleteUserToken(user);
		// delete the employee
		userService.deleteById(theId);
		// redirect to /employees/list
		return "redirect:/allUsers";

	}

	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") long theId,
									Model theModel) {

		// get the employee from the service
		User theEmployee = userService.findById(theId);

		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("employee", theEmployee);

		// send over to our form
		return "UserForm";
	}



	@PostMapping("/update")
	public String updateUser(@ModelAttribute("employee") User theEmployee) {

		UserDto userDto=new UserDto();
		User user=userService.findByUserName(theEmployee.getFullname());
		userDto.setFullname(theEmployee.getFullname());
		userDto.setEmail(theEmployee.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setRole(theEmployee.getRole());
		// save the employee
		userService.updateUser(userDto,theEmployee.getId());

		// use a redirect to prevent duplicate submissions
		return "redirect:/allUsers";
	}





}
