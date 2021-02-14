package org.philmaster.quizmaker.service.usermanagement;

import org.philmaster.quizmaker.model.User;

public interface RegistrationService {
	User startRegistration(User user);

	User continueRegistration(User user, String token);

	boolean isRegistrationCompleted(User user);
}
