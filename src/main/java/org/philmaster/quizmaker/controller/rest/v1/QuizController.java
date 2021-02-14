package org.philmaster.quizmaker.controller.rest.v1;

import java.util.List;

import org.philmaster.quizmaker.controller.utils.RestVerifier;
import org.philmaster.quizmaker.model.AuthenticatedUser;
import org.philmaster.quizmaker.model.Question;
import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.model.support.Response;
import org.philmaster.quizmaker.model.support.Result;
import org.philmaster.quizmaker.service.QuestionService;
import org.philmaster.quizmaker.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(QuizController.ROOT_MAPPING)
public class QuizController {

	public static final String ROOT_MAPPING = "/api/quizzes";

	private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

	@Autowired
	private QuizService quizService;

	@Autowired
	private QuestionService questionService;

	@GetMapping(value = "")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Page<Quiz> findAll(Pageable pageable,
			@RequestParam(required = false, defaultValue = "false") Boolean published) {

		if (published) {
			return quizService.findAllPublished(pageable);
		} else {
			return quizService.findAll(pageable);
		}
	}

	@GetMapping(value = "/search")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Page<Quiz> searchAll(Pageable pageable, @RequestParam(required = true) String filter,
			@RequestParam(required = false, defaultValue = "false") Boolean onlyValid) {

		return quizService.search(filter, pageable);
	}

	@PostMapping(value = "")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	public Quiz save(@AuthenticationPrincipal AuthenticatedUser user, Quiz quiz, BindingResult result) {

		logger.debug("The Quiz " + quiz.getName() + " is going to be created");

		RestVerifier.verifyModelResult(result);

		return quizService.save(quiz, user.getUser());
	}

	@GetMapping(value = "/{quiz_id}")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Quiz find(@PathVariable Long quiz_id) {

		return quizService.find(quiz_id);
	}

	@PostMapping(value = "/{quiz_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Quiz update(@PathVariable Long quiz_id, Quiz quiz, BindingResult result) {

		RestVerifier.verifyModelResult(result);

		quiz.setId(quiz_id);
		return quizService.update(quiz);
	}

	@DeleteMapping(value = "/{quiz_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long quiz_id) {
		Quiz quiz = quizService.find(quiz_id);
		quizService.delete(quiz);
	}

	@GetMapping(value = "/{quiz_id}/questions")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public List<Question> findQuestions(@PathVariable Long quiz_id,
			@RequestParam(required = false, defaultValue = "false") Boolean onlyValid) {

		Quiz quiz = quizService.find(quiz_id);

		if (onlyValid) {
			return questionService.findValidQuestionsByQuiz(quiz);
		} else {
			return questionService.findQuestionsByQuiz(quiz);
		}

	}

	@PostMapping(value = "/{quiz_id}/publish")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void publishQuiz(@PathVariable long quiz_id) {
		Quiz quiz = quizService.find(quiz_id);
		quizService.publishQuiz(quiz);
	}

	@PostMapping(value = "/{quiz_id}/submitAnswers")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Result playQuiz(@PathVariable long quiz_id, @RequestBody List<Response> answersBundle) {
		Quiz quiz = quizService.find(quiz_id);
		return quizService.checkAnswers(quiz, answersBundle);
	}

}
