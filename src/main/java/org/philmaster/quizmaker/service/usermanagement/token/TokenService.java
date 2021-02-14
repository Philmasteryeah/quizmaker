package org.philmaster.quizmaker.service.usermanagement.token;

import java.util.Date;

import org.philmaster.quizmaker.exceptions.InvalidTokenException;
import org.philmaster.quizmaker.model.TokenModel;
import org.philmaster.quizmaker.model.User;

public interface TokenService<T extends TokenModel> {
	T generateTokenForUser(User user);

	void validateTokenForUser(User user, String token) throws InvalidTokenException;

	void invalidateToken(String token);

	void invalidateExpiredTokensPreviousTo(Date date);
}
