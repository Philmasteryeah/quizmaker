package org.philmaster.quizmaker.controller.rest.v1;

import java.util.List;

import org.philmaster.quizmaker.controller.utils.RestVerifier;
import org.philmaster.quizmaker.model.Answer;
import org.philmaster.quizmaker.model.Question;
import org.philmaster.quizmaker.service.AnswerService;
import org.philmaster.quizmaker.service.QuestionService;
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
@RequestMapping(AnswerController.ROOT_MAPPING)
public class AnswerController {

	public static final String ROOT_MAPPING = "/api/answers";

	@Autowired
	AnswerService answerService;

	@Autowired
	QuestionService questionService;

	@PostMapping(value = "")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	public Answer save(Answer answer, BindingResult result, @RequestParam long question_id) {

		RestVerifier.verifyModelResult(result);

		Question question = questionService.find(question_id);
		return questionService.addAnswerToQuestion(answer, question);
	}

	@PostMapping(value = "/updateAll")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void updateAll(@RequestBody List<Answer> answers) {
		for (int i = 0; i < answers.size(); i++) {
			Answer answer = answers.get(i);
			answer.setOrder(i + 1);

			answerService.update(answer);
		}
	}

	@GetMapping(value = "/{answer_id}")
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Answer find(@PathVariable Long answer_id) {

		return answerService.find(answer_id);
	}

	@PostMapping(value = "/{answer_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Answer update(@PathVariable Long answer_id, Answer answer, BindingResult result) {

		RestVerifier.verifyModelResult(result);

		answer.setId(answer_id);
		return answerService.update(answer);
	}

	@DeleteMapping(value = "/{answer_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long answer_id) {
		Answer answer = answerService.find(answer_id);
		answerService.delete(answer);
	}
}
