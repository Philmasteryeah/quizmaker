package org.phimaster.quizmaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.philmaster.quizmaker.exceptions.QuizException;
import org.philmaster.quizmaker.exceptions.ResourceUnavailableException;
import org.philmaster.quizmaker.exceptions.UnauthorizedActionException;
import org.philmaster.quizmaker.exceptions.UserAlreadyExistsException;
import org.philmaster.quizmaker.model.User;
import org.philmaster.quizmaker.repository.UserRepository;
import org.philmaster.quizmaker.service.UserService;
import org.philmaster.quizmaker.service.UserServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests {

	private UserService service;

	// Mocks
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	private User user = new User();

	@BeforeAll
	public void before() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);

		service = new UserServiceImpl(userRepository, passwordEncoder);

		user.setEmail("a@a.com");
		user.setPassword("Password");
	}

	@Test
	public void saveNewUserShouldSucceed() throws UserAlreadyExistsException {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
		when(userRepository.save(user)).thenReturn(user);

		User returned = service.saveUser(user);

		verify(userRepository, times(1)).save(user);
		assertTrue(user.equals(returned));
	}

	@Test
	public void testSaveNewUserMailExistsShouldFail() throws UserAlreadyExistsException {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);
		Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
			service.saveUser(user);
		});
	}

	@Test
	public void testDeleteUser() throws UnauthorizedActionException, ResourceUnavailableException {
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		service.delete(user.getId());

		verify(userRepository, times(1)).delete(user);
	}

	@Test
	public void testDeleteUnexistentUser() throws QuizException {
		when(userRepository.findById(user.getId())).thenReturn(null);
		Assertions.assertThrows(ResourceUnavailableException.class, () -> {
			service.delete(user.getId());
		});
	}

	@Test
	public void testDeleteFromWrongUser() throws QuizException {
		when(userRepository.findById(user.getId())
				.get()).thenReturn(user);
		Assertions.assertThrows(UnauthorizedActionException.class, () -> {
			doThrow(new UnauthorizedActionException()).when(userRepository)
					.delete(user);

			service.delete(user.getId());
		});
	}

	@Test
	public void testFindUserByUsername_shouldntFind() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			when(userRepository.findByEmail(user.getEmail())).thenThrow(new UsernameNotFoundException("test"));
			service.loadUserByUsername("test");
		});
	}

	@Test
	public void testFindUserByUsername_shouldFind() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

		UserDetails localUser = service.loadUserByUsername(user.getEmail());

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		assertNotNull(localUser);
	}

	@Test
	public void testUpdatePasswordShouldEncrypt() {
		final String clearPass = "clearPassword";
		final String encodedPass = "encodedPassword";
		when(passwordEncoder.encode(clearPass)).thenReturn(encodedPass);
		when(userRepository.save(user)).thenReturn(user);

		User newUser = service.updatePassword(user, clearPass);

		verify(passwordEncoder, times(1)).encode(clearPass);
		verify(userRepository, times(1)).save(user);
		assertEquals(encodedPass, newUser.getPassword());
	}
}
