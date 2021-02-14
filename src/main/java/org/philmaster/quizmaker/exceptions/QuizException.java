package org.philmaster.quizmaker.exceptions;

public class QuizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QuizException() {
		super();
	}

	public QuizException(String message) {
		super(message);
	}
}
