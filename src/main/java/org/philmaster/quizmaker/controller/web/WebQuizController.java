package org.philmaster.quizmaker.controller.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.philmaster.quizmaker.controller.utils.RestVerifier;
import org.philmaster.quizmaker.exceptions.ModelVerificationException;
import org.philmaster.quizmaker.model.Answer;
import org.philmaster.quizmaker.model.AuthenticatedUser;
import org.philmaster.quizmaker.model.Question;
import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.service.QuestionService;
import org.philmaster.quizmaker.service.QuizService;
import org.philmaster.quizmaker.service.UserService;
import org.philmaster.quizmaker.service.accesscontrol.AccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebQuizController {

	@Autowired
	QuizService quizService;

	@Autowired
	QuestionService questionService;

	@Autowired
	UserService userService;

	@Autowired
	AccessControlService<Quiz> accessControlServiceQuiz;

	@Autowired
	AccessControlService<Question> accessControlServiceQuestion;

	@GetMapping(value = "/quizList")
	@PreAuthorize("permitAll")
	public String getUserList(@RequestParam(value = "name", required = false) String name, Model model,
			@PageableDefault(page = 0, size = 10) @SortDefault.SortDefaults({
					@SortDefault(sort = "name", direction = Direction.DESC),
					@SortDefault(sort = "description", direction = Direction.ASC) }) Pageable pageable) {

		// Page<Quiz> quizzes = quizService.search(name, pageable);

		Page<Quiz> quizzes = quizService.findAll(pageable); // TODO

		model.addAttribute("quizzes", quizzes);
		return "pages/quizList";
	}

	@GetMapping(value = "/quizDetail")
	@PreAuthorize("permitAll")
	public ModelAndView quizDetail(@RequestParam(required = false) String id) {
		Quiz quiz = id != null ? quizService.find(Long.valueOf(id)) : new Quiz();

		System.err.println(quiz);
		// accessControlServiceQuiz.canCurrentUserUpdateObject(quiz); TODO

		ModelAndView mav = new ModelAndView();
		mav.addObject("quiz", quiz);
		mav.setViewName("/pages/quizDetail");

		return mav;
	}

	@GetMapping(value = "/editAnswer/{question_id}")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView editAnswer(@PathVariable long question_id) {
		Question question = questionService.find(question_id);
		accessControlServiceQuestion.canCurrentUserUpdateObject(question);

		ModelAndView mav = new ModelAndView();
		mav.addObject("question", question);
		mav.setViewName("editAnswers");

		return mav;
	}

	@GetMapping(value = "/quiz/{id}")
	@PreAuthorize("permitAll")
	public ModelAndView getQuiz(@PathVariable long id) {
		Quiz quiz = quizService.find(id);

		ModelAndView mav = new ModelAndView();
		mav.addObject("quiz", quiz);
		mav.setViewName("quizView");

		return mav;
	}

	@GetMapping(value = "/quiz/{quiz_id}/play")
	@PreAuthorize("permitAll")
	public ModelAndView playQuiz(@PathVariable long quiz_id) {
		Quiz quiz = quizService.find(quiz_id);

		ModelAndView mav = new ModelAndView();
		mav.addObject("quiz", quiz);
		mav.setViewName("playQuiz");

		return mav;
	}

//	@PostMapping(value = "/createQuiz")
//	@PreAuthorize("isAuthenticated()")
//	public String newQuiz(@AuthenticationPrincipal AuthenticatedUser user, Quiz quiz, BindingResult result,
//			Model model) {
//		Quiz newQuiz;
//		System.err.println("asd " + user); // TODO User must exist
//		try {
//
////			RestVerifier.verifyModelResult(result);
////
//			// User user2 = user.getUser();
//			User user2 = userService.findByUsername(user.getUsername());
//
//			newQuiz = quizService.save(quiz, user2);
//
//		} catch (ModelVerificationException e) {
//			return "quizDetail";
//		}
//
//		return "redirect:/quizDetail/" + newQuiz.getId();
//	}

	@PostMapping(value = "/saveQuiz")
	@PreAuthorize("isAuthenticated()")
	public String saveQuiz(@AuthenticationPrincipal UserDetails authUser, @Valid @ModelAttribute("quiz") Quiz quiz,
			BindingResult bindingResult, Model model) {

		try {
			RestVerifier.verifyModelResult(bindingResult);
		} catch (ModelVerificationException e) {
			return "pages/quizDetail";
		}

		System.err.println("asd " + quiz); //
		try {

//			RestVerifier.verifyModelResult(result);
//
			// TODO not adding new
			// User user2 = user.getUser();
			User user2 = userService.findByUsername(authUser.getUsername());
			Answer answer = new Answer();
			answer.setText("asd");
			List<Answer> answers = new ArrayList<>();
			answers.add(answer);

			Question q = new Question();
			List<Question> quests = new ArrayList<>();
			quests.add(q);
			quiz.setQuestions(quests);

			quizService.save(quiz, user2);

		} catch (Exception e) {
			bindingResult.rejectValue(null, "error.object", e.getMessage());
			return "pages/quizDetail";
		}

		return "redirect:/quizList";
	}
}
