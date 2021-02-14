package org.philmaster.quizmaker.service.accesscontrol.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.philmaster.quizmaker.model.Quiz;
import org.philmaster.quizmaker.service.accesscontrol.AccessControlService;


@Aspect
@Component
public class AccessControlAspectsQuiz {

	@Autowired
	private AccessControlService<Quiz> accessControlService;

	public void setAccessControlService(AccessControlService<Quiz> accessControlService) {
		this.accessControlService = accessControlService;
	}

	@Around("execution(* org.philmaster.quizmaker.repository.QuizRepository.save(..)) && args(quiz,..)")
	public Object save(ProceedingJoinPoint proceedingJoinPoint, Quiz quiz) throws Throwable {
		if (quiz.getId() == null) {
			accessControlService.canCurrentUserCreateObject(quiz);
		} else {
			accessControlService.canCurrentUserUpdateObject(quiz);
		}

		return proceedingJoinPoint.proceed();
	}

	@Around("execution(* org.philmaster.quizmaker.repository.QuizRepository.find(Long)) && args(id)")
	public Object find(ProceedingJoinPoint proceedingJoinPoint, Long id) throws Throwable {
		accessControlService.canCurrentUserReadObject(id);

		return proceedingJoinPoint.proceed();
	}

	@Around("execution(* org.philmaster.quizmaker.repository.QuizRepository.findAll())")
	public Object findAll(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		accessControlService.canCurrentUserReadAllObjects();
		return proceedingJoinPoint.proceed();
	}

	@Around("execution(* org.philmaster.quizmaker.repository.QuizRepository.delete(..)) && args(quiz)")
	public Object delete(ProceedingJoinPoint proceedingJoinPoint, Quiz quiz) throws Throwable {
		accessControlService.canCurrentUserDeleteObject(quiz);

		return proceedingJoinPoint.proceed();
	}

}
