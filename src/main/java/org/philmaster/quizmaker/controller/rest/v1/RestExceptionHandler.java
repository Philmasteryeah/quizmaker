package org.philmaster.quizmaker.controller.rest.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.philmaster.quizmaker.controller.utils.ErrorInfo;
import org.philmaster.quizmaker.exceptions.ActionRefusedException;
import org.philmaster.quizmaker.exceptions.InvalidParametersException;
import org.philmaster.quizmaker.exceptions.InvalidTokenException;
import org.philmaster.quizmaker.exceptions.ModelVerificationException;
import org.philmaster.quizmaker.exceptions.ResourceUnavailableException;
import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.exceptions.UserAlreadyExistsException;

@ControllerAdvice("org.philmaster.quizmaker.controller.rest.v1")
public class RestExceptionHandler {

	@ExceptionHandler(UnauthorizedActionException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorInfo unauthorizedAction(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ErrorInfo userExists(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(ResourceUnavailableException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorInfo resourceUnavailable(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(ModelVerificationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo modelVerificationError(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(InvalidTokenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo invalidTokenError(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(InvalidParametersException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo invalidParametersError(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(ActionRefusedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorInfo actionRefusedError(ServerRequest req, Exception ex) {
		return new ErrorInfo(req.path(), ex, HttpStatus.FORBIDDEN.value());
	}
}
