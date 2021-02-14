package org.philmaster.quizmaker.service.accesscontrol;

import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.model.AuthenticatedUser;
import org.philmaster.quizmaker.model.BaseModel;
import org.philmaster.quizmaker.model.UserOwned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AccessControlServiceUserOwned<T extends BaseModel & UserOwned>
		implements AccessControlService<T>
{

	private static final Logger logger = LoggerFactory.getLogger(AccessControlServiceUserOwned.class);

	@Override
	public void canUserCreateObject(AuthenticatedUser user, T object) throws UnauthorizedActionException {
		if (!canUserModifyObject(user, object)) {
			logger.error("The user " + user.getId() + " can't create this object");
			throw new UnauthorizedActionException(
					"User " + user.getUsername() + " is not allowed to perform this action");
		}
	}

	@Override
	public void canCurrentUserCreateObject(T object) throws UnauthorizedActionException {
		canUserCreateObject(getCurrentUser(), object);
	}

	@Override
	public void canUserReadObject(AuthenticatedUser user, Long id) throws UnauthorizedActionException {
		// By default, anyone can read objects
	}

	@Override
	public void canCurrentUserReadObject(Long id) throws UnauthorizedActionException {
		canUserReadObject(getCurrentUser(), id);
	}

	@Override
	public void canUserReadAllObjects(AuthenticatedUser user) throws UnauthorizedActionException {
		// By default, anyone can read objects
	}

	@Override
	public void canCurrentUserReadAllObjects() throws UnauthorizedActionException {
		canUserReadAllObjects(getCurrentUser());
	}

	@Override
	public void canUserUpdateObject(AuthenticatedUser user, T object) throws UnauthorizedActionException {
		if (!canUserModifyObject(user, object)) {
			logger.error("The user " + ((user != null) ? user.getId() : "null") + " can't update this object");
			throw new UnauthorizedActionException("User " + ((user != null) ? user.getUsername() : "null")
					+ " is not allowed to perform this action");
		}
	}

	@Override
	public void canCurrentUserUpdateObject(T object) throws UnauthorizedActionException {
		canUserUpdateObject(getCurrentUser(), object);
	}

	@Override
	public void canUserDeleteObject(AuthenticatedUser user, T object) throws UnauthorizedActionException {
		if (!canUserModifyObject(user, object)) {
			logger.error("The user " + ((user != null) ? user.getId() : "null") + " can't delete this object");
			throw new UnauthorizedActionException("User " + ((user != null) ? user.getUsername() : "null")
					+ " is not allowed to perform this action");
		}
	}

	@Override
	public void canCurrentUserDeleteObject(T object) throws UnauthorizedActionException {
		canUserDeleteObject(getCurrentUser(), object);
	}

	private boolean canUserModifyObject(AuthenticatedUser user, T object) {
		if (user == null) {
			return false;
		}

		return object.getUser()
				.equals(user.getUser());
	}

	private AuthenticatedUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication.getPrincipal() == null || authentication.getPrincipal() instanceof String) {
			return null;
		}

		return (AuthenticatedUser) authentication.getPrincipal();
	}
}
