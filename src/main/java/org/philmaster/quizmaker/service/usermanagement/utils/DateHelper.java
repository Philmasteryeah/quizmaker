package org.philmaster.quizmaker.service.usermanagement.utils;

import java.util.Date;

public interface DateHelper {
	Date getCurrentDate();

	Date getExpirationDate(Date from, int offset);
}
