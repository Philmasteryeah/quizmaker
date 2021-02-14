package org.philmaster.quizmaker.service.usermanagement.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.philmaster.quizmaker.model.TokenModel;
import org.philmaster.quizmaker.model.TokenType;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.service.accesscontrol.AccessControlServiceUserOwned;

public class TokenDeliverySystemConsole implements TokenDeliverySystem {

	private static final Logger logger = LoggerFactory.getLogger(AccessControlServiceUserOwned.class);
	private static final String CONFIG_URI = "quiz.tokens.%s.url";

	private Environment env;

	@Autowired
	public TokenDeliverySystemConsole(Environment env) {
		this.env = env;
	}

	@Override
	public void sendTokenToUser(TokenModel token, User user, TokenType tokenType) {
		String config_uri = String.format(CONFIG_URI, tokenType.toString().toLowerCase());
		String url = String.format(env.getProperty(config_uri), user.getId(), token.getToken());
		logger.info(url);
	}

}
