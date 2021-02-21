package org.philmaster.quizmaker.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

	@GetMapping(value = "/")
	public String home() {
		return "index";
	}

//	@GetMapping(value = "/userList")
//	public String userList() {
//		return "pages/userList";
//	}

	@GetMapping(value = "/dashboard")
	public String dashboard() {
		return "pages/dashboard";
	}

	@GetMapping(value = "/quizList")
	public String quizList() {
		return "pages/quizList";
	}

	@GetMapping(value = "/500")
	public String error500() {
		return "error/500";
	}

	@GetMapping(value = "/404")
	public String error404() {
		return "error/404";
	}

}
