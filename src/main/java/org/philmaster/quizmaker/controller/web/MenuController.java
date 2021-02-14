package org.philmaster.quizmaker.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

	@GetMapping(value = "/")
	public String home() {
		return "index";
	}

	@GetMapping(value = "pages/userList")
	public String userList() {
		return "pages/userList";
	}

	@GetMapping(value = "pages/dashboard")
	public String dashboard() {
		return "pages/dashboard";
	}

}
