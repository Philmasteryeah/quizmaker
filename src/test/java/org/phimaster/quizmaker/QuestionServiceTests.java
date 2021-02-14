package org.phimaster.quizmaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.invocation.InvocationOnMock;
import org.philmaster.quizmaker.exceptions.ActionRefusedException;
import org.philmaster.quizmaker.exceptions.QuizException;
import org.philmaster.quizmaker.exceptions.ResourceUnavailableException;
import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.model.Answer;
import org.philmaster.quizmaker.model.Question;
import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.repository.QuestionRepository;
import org.philmaster.quizmaker.service.AnswerService;
import org.philmaster.quizmaker.service.QuestionService;
import org.philmaster.quizmaker.service.QuestionServiceImpl;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionServiceTests {

	private static final int DEFAULT_NUMBER_OF_ANSWERS = 10;

	private QuestionService service;

	// Mocks
	private QuestionRepository questionRepository;
	private AnswerService answerService;

	private User user = new User();
	private Quiz quiz = new Quiz();
	private Question question = new Question();

	@BeforeAll
	public void before() {
		questionRepository = mock(QuestionRepository.class);
		answerService = mock(AnswerService.class);
		service = new QuestionServiceImpl(questionRepository, answerService);

		user.setId(1l);
		quiz.setUser(user);
		quiz.setId(1l);
		question.setQuiz(quiz);
		question.setId(1l);

	}

	// Save

	@Test
	public void testSaveQuestionShouldSave() throws UnauthorizedActionException {
		service.save(question);
		verify(questionRepository, times(1)).save(question);
	}

	// Find

	@Test
	public void testFindExistingQuestion() throws ResourceUnavailableException {
		when(questionRepository.findById(question.getId())
				.get()).thenReturn(question);

		Question returned = service.find(question.getId());

		verify(questionRepository, times(1)).findById(question.getId());
		assertNotNull(returned);
		assertEquals(question.getId(), returned.getId());
	}

	@Test
	public void testFindNonExistingQuestion() throws ResourceUnavailableException {
		Assertions.assertThrows(ResourceUnavailableException.class, () -> {
			when(questionRepository.findById(quiz.getId())).thenReturn(null);

			service.find(quiz.getId());
		});
	}

	// Update

	@Test
	public void testUpdateShouldUpdate() throws QuizException {
		question.setText("test");
		question.setOrder(1);

		when(questionRepository.findById(question.getId())
				.get()).thenReturn(question);
		when(questionRepository.save(question)).thenReturn(question);
		Question returned = service.update(question);

		verify(questionRepository, times(1)).save(question);
		assertEquals(returned.getText(), question.getText());
	}

	@Test
	public void testUpdateShouldUpdate_noOrder() throws QuizException {
		question.setText("test");

		when(questionRepository.findById(question.getId())
				.get()).thenReturn(question);
		when(questionRepository.save(question)).thenReturn(question);
		Question returned = service.update(question);

		verify(questionRepository, times(1)).save(question);
		assertEquals(returned.getText(), question.getText());
	}

	@Test
	public void testUpdateUnexistentQuestion() throws QuizException {
		question.setText("test");
		Assertions.assertThrows(ResourceUnavailableException.class, () -> {
			when(questionRepository.findById(question.getId())).thenReturn(null);

			service.update(question);
		});
	}

	@Test
	public void testUpdateFromWrongUser() throws QuizException {
		question.setText("test");
		Assertions.assertThrows(UnauthorizedActionException.class, () -> {
			when(questionRepository.findById(question.getId())
					.get()).thenReturn(question);
			doThrow(new UnauthorizedActionException()).when(questionRepository)
					.save(question);

			service.update(question);
		});
	}

	// Delete

	@Test
	public void testDelete_QuizIsNotPublished_ShouldDelete() throws QuizException {
		question.getQuiz()
				.setIsPublished(false);

		service.delete(question);

		verify(questionRepository, times(1)).delete(question);
	}

	@Test
	public void testDelete_IsInvalid_ShouldDelete() throws QuizException {
		question.getQuiz()
				.setIsPublished(true);
		question.setIsValid(false);

		service.delete(question);

		verify(questionRepository, times(1)).delete(question);
	}

	@Test
	public void testDelete_SeveralValidQuestions_ShouldDelete() throws QuizException {
		question.getQuiz()
				.setIsPublished(true);
		question.setIsValid(true);
		when(questionRepository.countByQuizAndIsValidTrue(question.getQuiz())).thenReturn(2);

		service.delete(question);

		verify(questionRepository, times(1)).delete(question);
	}

	@Test
	public void testDelete_SeveralValidQuestions_ShouldntDelete() throws QuizException {
		question.getQuiz()
				.setIsPublished(true);
		question.setIsValid(true);
		Assertions.assertThrows(ActionRefusedException.class, () -> {
			when(questionRepository.countByQuizAndIsValidTrue(question.getQuiz())).thenReturn(1);

			service.delete(question);

			verify(questionRepository, times(1)).delete(question);
		});
	}

	@Test
	public void testDeleteFromWrongUser() throws QuizException {
		Assertions.assertThrows(UnauthorizedActionException.class, () -> {

			doThrow(new UnauthorizedActionException()).when(questionRepository)
					.delete(question);

			service.delete(question);
		});
	}

	@Test
	public void testCheckAnswer_answerFound_shouldReturnCorrect() {
		Answer correctAnswer = new Answer();
		correctAnswer.setId(1l);
		question.setCorrectAnswer(correctAnswer);
		question.setIsValid(true);

		boolean isCorrect = service.checkIsCorrectAnswer(question, correctAnswer.getId());

		assertTrue(isCorrect);
	}

	@Test
	public void testCheckAnswer_answerFound_shouldReturnIncorrect() {
		Answer correctAnswer = new Answer();
		correctAnswer.setId(1l);
		question.setCorrectAnswer(correctAnswer);
		question.setIsValid(true);

		boolean isCorrect = service.checkIsCorrectAnswer(question, correctAnswer.getId() + 1);

		assertFalse(isCorrect);
	}

	@Test
	public void testCheckAnswer_questionIsInvalid_shouldReturnIncorrect() {
		Answer correctAnswer = new Answer();
		correctAnswer.setId(1l);
		question.setCorrectAnswer(correctAnswer);
		question.setIsValid(false);

		boolean isCorrect = service.checkIsCorrectAnswer(question, correctAnswer.getId());

		assertFalse(isCorrect);
	}

	@Test
	public void testCheckAnswer_questionDoesntHaveCorrectAnswerSet_shouldReturnIncorrect() {
		question.setIsValid(true);

		boolean isCorrect = service.checkIsCorrectAnswer(question, 1l);

		assertFalse(isCorrect);
	}

	@Test
	public void testGetCorrectAnswer_noCorrectAnswerSet_shouldReturnNull() {
		List<Answer> answers = generateAnswers(DEFAULT_NUMBER_OF_ANSWERS);
		question.setAnswers(answers);

		Answer correctAnswer = service.getCorrectAnswer(question);

		assertNull(correctAnswer);
	}

	@Test
	public void testGetCorrectAnswer_correctAnswerSet_shouldReturnIt() {
		Answer answer = new Answer();
		answer.setId(1l);
		question.setCorrectAnswer(answer);
		question.setIsValid(true);

		Answer correctAnswer = service.getCorrectAnswer(question);

		assertEquals(answer, correctAnswer);
	}

	@Test
	public void testSetCorrectAnswer_shouldSetIt() {
		Answer answer = new Answer();
		answer.setId(1l);

		service.setCorrectAnswer(question, answer);

		assertEquals(answer, question.getCorrectAnswer());
	}

	@Test
	public void testAddAnswerToQuestion_firstAnswer_shouldEnableQuestionAndMarkItAsCorrect() {
		when(answerService.countAnswersInQuestion(question)).thenReturn(0);
		question.setIsValid(false);
		question.setCorrectAnswer(null);
		Answer answer = new Answer();
		answer.setId(1l);

		when(answerService.save(any(Answer.class))).thenAnswer(new org.mockito.stubbing.Answer<Answer>() {
			@Override
			public Answer answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Answer) args[0];
			}
		});

		service.addAnswerToQuestion(answer, question);

		assertTrue(question.getIsValid());
		assertEquals(answer, question.getCorrectAnswer());
		verify(answerService, times(1)).save(answer);
		verify(questionRepository, times(2)).save(question);
	}

	@Test
	public void testAddAnswerToQuestion_firstAnswerButValid_shouldMarkItAsCorrect() {
		when(answerService.countAnswersInQuestion(question)).thenReturn(0);
		question.setIsValid(true);
		question.setCorrectAnswer(null);
		Answer answer = new Answer();
		answer.setId(1l);

		when(answerService.save(any(Answer.class))).thenAnswer(new org.mockito.stubbing.Answer<Answer>() {
			@Override
			public Answer answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Answer) args[0];
			}
		});

		service.addAnswerToQuestion(answer, question);

		assertTrue(question.getIsValid());
		assertEquals(answer, question.getCorrectAnswer());
		verify(answerService, times(1)).save(answer);
		verify(questionRepository, times(1)).save(question);
	}

	@Test
	public void testAddAnswerToQuestion_notFirstAnswerInvalidQuestion_shouldNotMarkItAsCorrect_shouldMarkItAsValid() {
		when(answerService.countAnswersInQuestion(question)).thenReturn(1);
		question.setIsValid(false);
		question.setCorrectAnswer(null);
		Answer answer = new Answer();
		answer.setId(1l);

		when(answerService.save(any(Answer.class))).thenAnswer(new org.mockito.stubbing.Answer<Answer>() {
			@Override
			public Answer answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Answer) args[0];
			}
		});

		service.addAnswerToQuestion(answer, question);

		assertTrue(question.getIsValid());
		verify(answerService, times(1)).save(answer);
		verify(questionRepository, times(1)).save(question);
	}

	@Test
	public void testAddAnswerToQuestion_notFirstAnswer_shouldNotMarkItAsCorrect() {
		when(answerService.countAnswersInQuestion(question)).thenReturn(1);
		question.setIsValid(true);
		question.setCorrectAnswer(null);
		Answer answer = new Answer();
		answer.setId(1l);

		when(answerService.save(any(Answer.class))).thenAnswer(new org.mockito.stubbing.Answer<Answer>() {
			@Override
			public Answer answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Answer) args[0];
			}
		});

		service.addAnswerToQuestion(answer, question);

		assertTrue(question.getIsValid());
		verify(answerService, times(1)).save(answer);
		verify(questionRepository, never()).save(question);
	}

	private List<Answer> generateAnswers(int numberOfAnswers) {
		List<Answer> list = new ArrayList<>();

		for (int i = 0; i < numberOfAnswers; i++) {
			Answer answer = new Answer();
			answer.setId((long) i);
			list.add(answer);
		}

		return list;
	}

}
