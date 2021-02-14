package org.philmaster.quizmaker.exceptions;

public class InvalidTokenException extends QuizException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
		super();
	}

	public InvalidTokenException(String message) {
		super(message);
	}
}