package org.philmaster.quizmaker.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "answer")
public class Answer extends BaseModel implements UserOwned {

	@Size(min = 1, max = 50, message = "The answer should be less than 50 characters")
	@NotEmpty(message = "No answer text provided.")
	@Column(nullable = false)
	private String text;

	@ManyToOne
	@JsonIgnore
	private Question question;

	@Column(name = "a_order")
	private Integer order;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
	private Calendar dateCreated;

	@Override
	@JsonIgnore
	public User getUser() {
		return question.getUser();
	}

	@Override
	public String toString() {
		return "Answer [text=" + text + ", question=" + question + ", order=" + order + ", dateCreated=" + dateCreated
				+ "]";
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
