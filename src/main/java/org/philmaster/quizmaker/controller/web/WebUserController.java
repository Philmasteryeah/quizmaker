package org.philmaster.quizmaker.controller.web;

import java.util.Map;

import javax.validation.Valid;

import org.philmaster.quizmaker.controller.utils.RestVerifier;
import org.philmaster.quizmaker.exceptions.ModelVerificationException;
import org.philmaster.quizmaker.model.AuthenticatedUser;
import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebUserController {

	@Autowired
	UserService userService;

	@GetMapping(value = "/user/{user_id}/quizzes")
	@PreAuthorize("permitAll")
	public String getQuizzesForUser(@PathVariable Long user_id) {
		userService.find(user_id);

		// TODO: Unimplemented
		return "error/404";
	}

	@GetMapping(value = "/userList")
	@PreAuthorize("permitAll")
	public String getUserList(@RequestParam(value = "search", required = false) String search, Model model,
			@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
					@SortDefault(sort = "username", direction = Direction.DESC),
					@SortDefault(sort = "email", direction = Direction.ASC) }) Pageable pageable) {
		model.addAttribute("users", userService.findAllBySearch(search, pageable));
		return "pages/userList";
	}

	@GetMapping(value = "/userDetail")
	@PreAuthorize("permitAll")
	public ModelAndView userDetail() {
		User user = new User();

		System.err.println(user + "-new-" + user);
		// accessControlServiceQuiz.canCurrentUserUpdateObject(user); TODO

		ModelAndView mav = new ModelAndView();
		mav.addObject("user", user);
		mav.setViewName("/pages/userDetail");

		return mav;
	}

	@GetMapping(value = "/userDetail/{id}")
	@PreAuthorize("permitAll")
	public ModelAndView userDetail(@PathVariable long id) {
		User user =  userService.find(id);

		System.err.println(user + "-detail user-" + id);
		// accessControlServiceQuiz.canCurrentUserUpdateObject(user); TODO

		ModelAndView mav = new ModelAndView();
		mav.addObject("user", user);
		mav.setViewName("/pages/userDetail");

		return mav;
	}

//	@PostMapping(value = "/createUser")
//	@PreAuthorize("permitAll")
//	public String createUser(@AuthenticationPrincipal AuthenticatedUser auser, User user, BindingResult result,
//			Model model) {
//		User u = new User();
//		System.err.println(auser + " asd " + result); // TODO User must exist
//
//		try {
//
////			RestVerifier.verifyModelResult(result);
////
//			u = userService.saveUser(u);
//
//		} catch (ModelVerificationException e) {
//			return "userDetail";
//		}
//
//		return "redirect:/userDetail/" + u.getId();
//	}
	

    @PostMapping("/saveUser")
    public String doCreateUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model) {
    	try {
        	RestVerifier.verifyModelResult(bindingResult);
		} catch (ModelVerificationException e) {
			 return "pages/userDetail";
		}
    	
    	
        if (bindingResult.hasErrors()) {
            return "pages/userDetail";
        }
        try {
        	userService.saveUser(user);	
		} catch (Exception e) {
			return "pages/userDetail";
		}
              
        System.err.println("create user "+user.getUsername());
        //userService.createUser(formData.toParameters());

        return "redirect:/userList";
    }



}
