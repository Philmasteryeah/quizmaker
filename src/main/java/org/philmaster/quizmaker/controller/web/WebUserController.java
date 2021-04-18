package org.philmaster.quizmaker.controller.web;

import org.philmaster.quizmaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
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

}
