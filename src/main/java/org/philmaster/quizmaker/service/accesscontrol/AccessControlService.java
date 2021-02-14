package org.philmaster.quizmaker.service.accesscontrol;

import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.model.AuthenticatedUser;
import org.philmaster.quizmaker.model.BaseModel;

public interface AccessControlService<T extends BaseModel> {
	void canUserCreateObject(AuthenticatedUser user, T object) throws UnauthorizedActionException;

	void canCurrentUserCreateObject(T object) throws UnauthorizedActionException;

	void canUserReadObject(AuthenticatedUser user, Long id) throws UnauthorizedActionException;

	void canCurrentUserReadObject(Long id) throws UnauthorizedActionException;

	void canUserReadAllObjects(AuthenticatedUser user) throws UnauthorizedActionException;

	void canCurrentUserReadAllObjects() throws UnauthorizedActionException;

	void canUserUpdateObject(AuthenticatedUser user, T object) throws UnauthorizedActionException;

	void canCurrentUserUpdateObject(T object) throws UnauthorizedActionException;

	void canUserDeleteObject(AuthenticatedUser user, T object) throws UnauthorizedActionException;

	void canCurrentUserDeleteObject(T object) throws UnauthorizedActionException;
}
