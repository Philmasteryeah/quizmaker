package org.philmaster.quizmaker.service.usermanagement.token;

import org.springframework.scheduling.annotation.Async;

import org.philmaster.quizmaker.model.TokenModel;
import org.philmaster.quizmaker.model.TokenType;
import org.philmaster.quizmaker.model.User;

public interface TokenDeliverySystem {
	@Async
	void sendTokenToUser(TokenModel token, User user, TokenType tokenType);
}
