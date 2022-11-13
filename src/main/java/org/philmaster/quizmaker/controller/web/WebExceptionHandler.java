package org.philmaster.quizmaker.controller.web;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.servlet.ModelAndView;

import org.philmaster.quizmaker.controller.utils.ErrorInfo;
import org.philmaster.quizmaker.exceptions.ActionRefusedException;
import org.philmaster.quizmaker.exceptions.InvalidParametersException;
import org.philmaster.quizmaker.exceptions.InvalidTokenException;
import org.philmaster.quizmaker.exceptions.ModelVerificationException;
import org.philmaster.quizmaker.exceptions.ResourceUnavailableException;
import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.exceptions.UserAlreadyExistsException;

@ControllerAdvice("org.philmaster.quizmaker.controller.web")
public class WebExceptionHandler {

	@ExceptionHandler(UnauthorizedActionException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ModelAndView unauthorizedAction(ServerRequest req, Exception ex) {
		return setModelAndView(req.path(), ex, HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ModelAndView userExists(ServerRequest req, Exception ex) {
		return setModelAndView(req.path().toString(), ex, HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(ResourceUnavailableException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView resourceUnavailable(ServerRequest req, Exception ex) {
		return setModelAndView(req.path().toString(), ex, HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(ModelVerificationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView modelVerificationError(ServerRequest req, Exception ex) {
		return setModelAndView(req.path().toString(), ex, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(InvalidTokenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView invalidTokenError(ServerRequest req, Exception ex) {
		return setModelAndView(req.path().toString(), ex, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(InvalidParametersException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView invalidParametersError(ServerRequest req, Exception ex) {
		return setModelAndView(req.path().toString(), ex, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(ActionRefusedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ModelAndView actionRefusedError(ServerRequest req, Exception ex) {
		return setModelAndView(req.path().toString(), ex, HttpStatus.FORBIDDEN.value());
	}

	private ModelAndView setModelAndView(String url, Exception ex, Integer httpCode) {
		ErrorInfo errorInfo = new ErrorInfo(url, ex, httpCode);
		ModelAndView mav = new ModelAndView();

		mav.addObject("errorInfo", errorInfo);
		mav.setViewName("error/404");
		return mav;
	}
}
