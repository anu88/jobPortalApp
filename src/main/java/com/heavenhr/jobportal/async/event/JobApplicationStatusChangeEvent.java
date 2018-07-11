package com.heavenhr.jobportal.async.event;

import org.springframework.context.ApplicationEvent;

import com.heavenhr.jobportal.dao.ApplicationStatusEnum;

public class JobApplicationStatusChangeEvent extends ApplicationEvent {

	private static final long serialVersionUID = -5262798597139281867L;

	private final String candidateEmail;

	private final ApplicationStatusEnum applicationStatus;

	public JobApplicationStatusChangeEvent(final Object source, final String candidateEmail,
			final ApplicationStatusEnum applicationStatus) {
		super(source);
		this.candidateEmail = candidateEmail;
		this.applicationStatus = applicationStatus;
	}

	public String getCandidateEmail() {
		return candidateEmail;
	}

	public ApplicationStatusEnum getApplicationStatus() {
		return applicationStatus;
	}

}
