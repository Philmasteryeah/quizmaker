package org.philmaster.quizmaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Bean
	public nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect layoutDialect() {
		return new nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect(); // enables thymeleaf layout:decorate
	}

}