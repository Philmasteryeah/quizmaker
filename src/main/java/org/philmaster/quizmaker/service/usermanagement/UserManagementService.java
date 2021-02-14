package org.philmaster.quizmaker.service.usermanagement;

import org.philmaster.quizmaker.model.User;

public interface UserManagementService {

	void resendPassword(User user);

	void verifyResetPasswordToken(User user, String token);

	void updatePassword(User user, String password);

}
