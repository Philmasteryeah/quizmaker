package org.philmaster.quizmaker.model.support;

import javax.persistence.Id;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {

	@Column(name = "username", length = 64, nullable = false)
	String username;

	@Id
	@Column(name = "series", length = 64, nullable = false)
	String series;

	@Column(name = "token", length = 64, nullable = false)
	String token;

	@Column(columnDefinition = "TIMESTAMP", nullable = false)
	Calendar last_used;
}
