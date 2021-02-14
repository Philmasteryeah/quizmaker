package org.philmaster.quizmaker.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import org.philmaster.quizmaker.controller.rest.v1.UserController;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService,
			PasswordEncoder encoder) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
	}

	@Configuration
	@Order(1)
	public static class RestWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.antMatcher(
							"/api/**")
					.authorizeRequests()
					.anyRequest()
						.permitAll() // change later
				.and()
					.httpBasic()
				.and()
					.csrf()
						.disable()
					.logout()
						.logoutUrl(UserController.ROOT_MAPPING + "/logout")
						.logoutSuccessUrl(UserController.ROOT_MAPPING + "/logoutDummy")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true);
		}
	}

	@Configuration
	public static class WebWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		PersistentTokenRepository persistentTokenRepository;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.formLogin()
					.loginPage("/user/login")
					.failureUrl("/user/login-error")
					.defaultSuccessUrl("/", true)
				.and()
					.rememberMe()
					.tokenRepository(persistentTokenRepository)
				.and()
					.csrf()
						.disable()
					.logout()
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true);

		}
		
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			  auth.inMemoryAuthentication().withUser("admin@local.de").password("123").roles("ADMIN");
		}
	}


}