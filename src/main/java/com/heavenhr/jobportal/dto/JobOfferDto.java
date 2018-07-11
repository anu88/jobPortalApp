package com.heavenhr.jobportal.dto;

import java.text.ParseException;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heavenhr.jobportal.constant.ApplicationConstant;
import com.heavenhr.jobportal.dao.JobOffer;
import com.heavenhr.jobportal.exception.ValidationException;

import io.swagger.annotations.ApiModelProperty;

public class JobOfferDto {

	private static final String DATE_FORMAT = "dd-mm-yyyy";

	@NotBlank(message = "Please enter a valid job title.")
	@ApiModelProperty(required = true)
	private String jobTitle;

	@DateTimeFormat(pattern = DATE_FORMAT)
	@ApiModelProperty(required = true)
	private String startDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@ApiModelProperty(readOnly = true)
	private Long numberOfApplications;

	public JobOfferDto() {

	}

	public JobOfferDto(final JobOffer jobOffer) {
		this.jobTitle = jobOffer.getJobTitle();
		this.startDate = DateFormatUtils.format(jobOffer.getStartDate(), DATE_FORMAT);
		this.setNumberOfApplications(Long.valueOf(jobOffer.getJobApplications().size()));
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(final String startDate) {
		this.startDate = startDate;
	}
	
	public Long getNumberOfApplications() {
		return numberOfApplications;
	}

	public void setNumberOfApplications(final Long numberOfApplications) {
		this.numberOfApplications = numberOfApplications;
	}

	public JobOffer toEntity() throws ValidationException {
		final JobOffer jobOffer = new JobOffer();
		jobOffer.setJobTitle(this.jobTitle);
		try {
			jobOffer.setStartDate(DateUtils.parseDate(this.startDate, DATE_FORMAT));
		} catch (final ParseException e) {
			throw new ValidationException(ApplicationConstant.JOB_OFFER_INVALID_START_DATE, "Please enter a valid date in dd-mm-yyyy format.",
					e.getCause());
		}
		return jobOffer;
	}

}
