package org.philmaster.quizmaker.controller.rest.v1;

import java.util.List;

import org.philmaster.quizmaker.controller.utils.RestVerifier;
import org.philmaster.quizmaker.model.Answer;
import org.philmaster.quizmaker.model.Question;
import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.service.AnswerService;
import org.philmaster.quizmaker.service.QuestionService;
import org.philmaster.quizmaker.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(QuestionController.ROOT_MAPPING)
public class QuestionController {

	public static final String ROOT_MAPPING = "/api/questions";

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuizService quizService;

	@Autowired
	private AnswerService answerService;

	@PostMapping(value = "")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	public Question save(Question question, BindingResult result, @RequestParam Long quiz_id) {

		RestVerifier.verifyModelResult(result);

		Quiz quiz = quizService.find(quiz_id);
		question.setQuiz(quiz);

		return questionService.save(question);
	}

	@PostMapping(value = "/updateAll")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void updateAll(@RequestBody List<Question> questions) {
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			question.setOrder(i + 1);

			questionService.update(question);
		}
	}

	@GetMapping(value = "/{question_id}")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Question find(@PathVariable Long question_id) {

		return questionService.find(question_id);
	}

	@PostMapping(value = "/{question_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Question update(@PathVariable Long question_id, Question question, BindingResult result) {

		RestVerifier.verifyModelResult(result);

		question.setId(question_id);
		return questionService.update(question);

	}

	@DeleteMapping(value = "/{question_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long question_id) {
		Question question = questionService.find(question_id);
		questionService.delete(question);
	}

	@GetMapping(value = "/{question_id}/answers")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public List<Answer> findAnswers(@PathVariable Long question_id) {
		Question question = questionService.find(question_id);
		return answerService.findAnswersByQuestion(question);
	}

	@GetMapping(value = "/{question_id}/correctAnswer")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Answer getCorrectAnswer(@PathVariable Long question_id) {
		Question question = questionService.find(question_id);
		return questionService.getCorrectAnswer(question);
	}

	@PostMapping(value = "/{question_id}/correctAnswer")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void setCorrectAnswer(@PathVariable Long question_id, @RequestParam Long answer_id) {

		Question question = questionService.find(question_id);
		Answer answer = answerService.find(answer_id);
		questionService.setCorrectAnswer(question, answer);
	}

}
