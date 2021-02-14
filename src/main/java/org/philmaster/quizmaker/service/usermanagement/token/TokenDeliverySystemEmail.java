package org.philmaster.quizmaker.service.usermanagement.token;

import org.philmaster.quizmaker.model.TokenModel;
import org.philmaster.quizmaker.model.TokenType;
import org.philmaster.quizmaker.model.User;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TokenDeliverySystemEmail implements TokenDeliverySystem {

	// private static final String BASE_CONFIG_URI = "quiz.tokens.%s";

//	@Autowired
//	private MessageSource messageSource;
	// private JavaMailSender mailSender;

//	@Autowired
//	public TokenDeliverySystemEmail(MessageSource messageSource, JavaMailSender mailSender) {
//		this.messageSource = messageSource;
//		//this.mailSender = mailSender;
//	}

	@Override
	public void sendTokenToUser(TokenModel token, User user, TokenType tokenType) {
//		String base_config = String.format(BASE_CONFIG_URI, tokenType.toString().toLowerCase());
//		String url = String.format(messageSource.getMessage(base_config + ".url", null, null), user.getId(),
//				token.getToken());

		try {
			// sendmail
		} catch (Exception e) {
			// This runs on a thread so it is too late to notify the user. A
			// re-try mechanism could be put in place.
			e.printStackTrace();
		}
	}
}
