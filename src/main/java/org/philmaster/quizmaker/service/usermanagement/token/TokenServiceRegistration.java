package org.philmaster.quizmaker.service.usermanagement.token;

import org.philmaster.quizmaker.model.RegistrationToken;
import org.philmaster.quizmaker.repository.TokenRepository;
import org.philmaster.quizmaker.service.usermanagement.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceRegistration extends TokenServiceAbs<RegistrationToken> {

	//@Value("${quiz.tokens.registration_mail.timeout}")
	private Integer expirationTimeInMinutes = 86400;

	@Autowired
	public TokenServiceRegistration(TokenRepository<RegistrationToken> mailTokenRepository,
			TokenGenerator tokenGenerator) {
		super(tokenGenerator, mailTokenRepository);
	}

	@Override
	protected RegistrationToken create() {
		return new RegistrationToken();
	}

	@Override
	protected int getExpirationTimeInMinutes() {
		return expirationTimeInMinutes;
	}

	public void setExpirationTimeInMinutes(Integer expirationTimeInMinutes) {
		this.expirationTimeInMinutes = expirationTimeInMinutes;
	}
}
