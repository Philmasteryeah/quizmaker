package org.philmaster.quizmaker.service.accesscontrol;

import org.springframework.stereotype.Service;

import org.philmaster.quizmaker.model.Question;

@Service
public class AccessControlServiceQuestion extends AccessControlServiceUserOwned<Question> {

}
