package org.philmaster.quizmaker.controller.web;

import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.service.UserService;
import org.philmaster.quizmaker.service.usermanagement.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class UserManagementController {

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService;

	@GetMapping(value = "/user/login")
	@PreAuthorize("permitAll")
	public String login(@ModelAttribute User user) {
		return "login";
	}

	@GetMapping(value = "/user/login-error")
	@PreAuthorize("permitAll")
	public String loginError(@ModelAttribute User user, Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@GetMapping(value = "/user/forgotPassword")
	@PreAuthorize("permitAll")
	public String forgotPassword() {
		return "forgotPassword";
	}

	@PostMapping(value = "/user/forgotPassword")
	@PreAuthorize("permitAll")
	public ModelAndView forgotPassword(String email) {
		User user = userService.findByEmail(email);
		userManagementService.resendPassword(user);

		ModelAndView mav = new ModelAndView();
		mav.addObject("header", messageSource.getMessage("label.forgotpassword.success.header", null, null));
		mav.addObject("subheader", messageSource.getMessage("label.forgotpassword.success.subheader", null, null));
		mav.setViewName("simplemessage");

		return mav;
	}

	@GetMapping(value = "/user/{user_id}/resetPassword")
	@PreAuthorize("permitAll")
	public ModelAndView resetPassword(@PathVariable Long user_id, String token) {
		User user = userService.find(user_id);
		userManagementService.verifyResetPasswordToken(user, token);

		ModelAndView mav = new ModelAndView();
		mav.addObject("user", user);
		mav.addObject("token", token);
		mav.setViewName("resetPassword");

		return mav;
	}

	@PostMapping(value = "/user/{user_id}/resetPassword")
	@PreAuthorize("permitAll")
	public String resetPassword(@PathVariable Long user_id, String token, String password) {
		User user = userService.find(user_id);
		userManagementService.verifyResetPasswordToken(user, token);

		userManagementService.updatePassword(user, password);

		return "login";
	}
}
