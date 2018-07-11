package com.heavenhr.jobportal.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heavenhr.jobportal.dao.ApplicationStatusEnum;
import com.heavenhr.jobportal.dao.JobApplication;

import io.swagger.annotations.ApiModelProperty;

public class JobApplicationDto {

	@ApiModelProperty(readOnly = true)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ApplicationStatusEnum applicationStatus;

	@ApiModelProperty(required = true)
	@NotBlank(message = "Please enter valid resume text.")
	private String resumeText;

	@ApiModelProperty(required = true)
	@Email(message = "Please enter a valid email id.")
	private String candidateEmail;

	public JobApplicationDto() {
	}

	public JobApplicationDto(final JobApplication jobApplication) {
		this.applicationStatus = jobApplication.getApplicationStatus();
		this.resumeText = jobApplication.getResumeText();
		this.candidateEmail = jobApplication.getEmailId();
	}

	public String getResumeText() {
		return resumeText;
	}

	public String getCandidateEmail() {
		return candidateEmail;
	}

	public void setResumeText(final String resumeText) {
		this.resumeText = resumeText;
	}

	public void setCandidateEmail(final String emailId) {
		this.candidateEmail = emailId;
	}

	public JobApplication toEntity() {
		final JobApplication jobApplication = new JobApplication();
		jobApplication.setApplicationStatus(
				this.applicationStatus == null ? ApplicationStatusEnum.APPLIED : this.applicationStatus);
		jobApplication.setEmailId(this.candidateEmail);
		jobApplication.setResumeText(this.resumeText);
		return jobApplication;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
