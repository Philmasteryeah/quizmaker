package org.philmaster.quizmaker.controller.web;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.philmaster.quizmaker.controller.utils.RestVerifier;
import org.philmaster.quizmaker.exceptions.ModelVerificationException;
import org.philmaster.quizmaker.exceptions.UserAlreadyExistsException;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.service.UserService;
import org.philmaster.quizmaker.service.usermanagement.RegistrationService;

@Controller
@RequestMapping("/user")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
	public String showRegistrationForm(@ModelAttribute User user) {
		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	@PreAuthorize("permitAll")
	public ModelAndView signUp(@ModelAttribute User user, BindingResult result) {
		User newUser;
		ModelAndView mav = new ModelAndView();

		try {
			RestVerifier.verifyModelResult(result);
			newUser = registrationService.startRegistration(user);
		} catch (ModelVerificationException e) {
			mav.setViewName("registration");
			return mav;
		} catch (UserAlreadyExistsException e) {
			result.rejectValue("email", "label.user.emailInUse");
			mav.setViewName("registration");
			return mav;
		}

		return registrationStepView(newUser, mav);
	}

	@RequestMapping(value = "/{user_id}/continueRegistration", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
	public ModelAndView nextRegistrationStep(@PathVariable Long user_id, String token) {
		User user = userService.find(user_id);
		registrationService.continueRegistration(user, token);

		ModelAndView mav = new ModelAndView();
		return registrationStepView(user, mav);
	}

	private ModelAndView registrationStepView(User user, ModelAndView mav) {

		if (!registrationService.isRegistrationCompleted(user)) {
			mav.addObject("header", messageSource.getMessage("label.registration.step1.header", null, null));
			mav.addObject("subheader", messageSource.getMessage("label.registration.step1.subheader", null, null));
			mav.setViewName("simplemessage");
		} else {
			mav.addObject("header", messageSource.getMessage("label.registration.step2.header", null, null));
			mav.addObject("subheader", messageSource.getMessage("label.registration.step2.subheader", null, null));
			mav.setViewName("simplemessage");
		}

		return mav;
	}
}
