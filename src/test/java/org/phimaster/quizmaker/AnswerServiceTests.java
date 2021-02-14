package org.phimaster.quizmaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.philmaster.quizmaker.exceptions.ActionRefusedException;
import org.philmaster.quizmaker.exceptions.QuizException;
import org.philmaster.quizmaker.exceptions.ResourceUnavailableException;
import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.model.Answer;
import org.philmaster.quizmaker.model.AuthenticatedUser;
import org.philmaster.quizmaker.model.Question;
import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.repository.AnswerRepository;
import org.philmaster.quizmaker.service.AnswerServiceImpl;
import org.philmaster.quizmaker.service.QuestionService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnswerServiceTests {

	private AnswerServiceImpl service;

	// Mocks
	private AnswerRepository answerRepository;
	private QuestionService questionService;

	private User internalUser = new User();
	private AuthenticatedUser user = new AuthenticatedUser(internalUser);
	private Quiz quiz = new Quiz();
	private Question question = new Question();
	private Answer answer = new Answer();

	@BeforeAll
	public void before() {
		answerRepository = mock(AnswerRepository.class);
		questionService = mock(QuestionService.class);

		service = new AnswerServiceImpl(answerRepository);
		service.setQuestionService(questionService);

		internalUser.setId(1l);
		quiz.setUser(user.getUser());
		quiz.setId(1l);
		question.setQuiz(quiz);
		answer.setQuestion(question);
		answer.setId(1l);

	}

	// Save

	@Test
	public void testSaveAnswerShouldSave() throws UnauthorizedActionException {
		service.save(answer);
		verify(answerRepository, times(1)).save(answer);
	}

	// Find

	@Test
	public void testFindExistingAnswer() throws ResourceUnavailableException {
		when(answerRepository.findById(answer.getId()).get()
				).thenReturn(answer);

		Answer returned = service.find(answer.getId());

		verify(answerRepository, times(1)).findById(answer.getId());
		assertNotNull(returned);
		assertEquals(answer.getId(), returned.getId());
	}

	@Test
	public void testFindNonExistingQuestion() throws ResourceUnavailableException {

		Assertions.assertThrows(ResourceUnavailableException.class, () -> {
			when(answerRepository.findById(quiz.getId()).get()).thenReturn(null);
			service.find(quiz.getId());
		});

	}

	// Update

	@Test
	public void testUpdateShouldUpdate() throws QuizException {
		answer.setText("test");
		answer.setOrder(1);
		when(answerRepository.findById(answer.getId())
				.get()).thenReturn(answer);
		when(answerRepository.save(answer)).thenReturn(answer);

		Answer returned = service.update(answer);

		verify(answerRepository, times(1)).save(answer);
		assertEquals(returned.getText(), answer.getText());
		assertEquals(returned.getOrder(), (Integer) 1);
	}

	@Test
	public void testUpdateUnexistentAnswer() throws QuizException {

		Assertions.assertThrows(ResourceUnavailableException.class, () -> {
			answer.setText("test");

			when(answerRepository.findById(answer.getId())).thenReturn(null);

			service.update(answer);

		});
	}

	@Test
	public void testUpdateFromWrongUser() throws QuizException {
		answer.setText("test");
		Assertions.assertThrows(UnauthorizedActionException.class, () -> {

			when(answerRepository.findById(answer.getId()).get()).thenReturn(answer);
			doThrow(new UnauthorizedActionException()).when(answerRepository)
					.save(answer);

			service.update(answer);
		});
	}

	// Delete

	@Test
	public void testDelete_isNotCorrect_ShouldDelete() throws QuizException {
		when(questionService.checkIsCorrectAnswer(question, answer.getId())).thenReturn(false);

		service.delete(answer);

		verify(answerRepository, times(1)).delete(answer);
	}

	@Test
	public void testDelete_isCorrect_ShouldNotDelete() throws QuizException {
		Assertions.assertThrows(ActionRefusedException.class, () -> {

			when(questionService.checkIsCorrectAnswer(question, answer.getId())).thenReturn(true);

			service.delete(answer);

			verify(answerRepository, never()).delete(answer);

		});
	}

	@Test
	public void testDeleteFromWrongUser() throws QuizException {
		answer.setText("test");

		Assertions.assertThrows(UnauthorizedActionException.class, () -> {
			doThrow(new UnauthorizedActionException()).when(answerRepository)
					.delete(answer);

			service.delete(answer);
		});
	}

}
