package org.philmaster.quizmaker.service.accesscontrol.aspects;

import org.aspectj.lang.ProceedingJoinPoint;

import org.philmaster.quizmaker.model.BaseModel;

public interface AccessControlAspects<T extends BaseModel> {
	Object save(ProceedingJoinPoint proceedingJoinPoint, T object) throws Throwable;

	Object find(ProceedingJoinPoint proceedingJoinPoint, Long id) throws Throwable;

	Object findAll(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

	Object delete(ProceedingJoinPoint proceedingJoinPoint, T object) throws Throwable;
}