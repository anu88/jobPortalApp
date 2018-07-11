package com.heavenhr.jobportal.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heavenhr.jobportal.constant.ApplicationConstant;
import com.heavenhr.jobportal.dao.ApplicationStatusEnum;
import com.heavenhr.jobportal.dto.JobApplicationDto;
import com.heavenhr.jobportal.exception.BaseException;
import com.heavenhr.jobportal.exception.ValidationException;
import com.heavenhr.jobportal.service.JobApplicationService;

@RestController
@RequestMapping(value = "joboffers/{jobTitle}/jobapplications")
public class JobApplicationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationController.class);

	@Autowired
	private JobApplicationService jobApplicationService;

	@PostMapping
	public ResponseEntity<Void> createJobApplication(@PathVariable @Valid @NotBlank final String jobTitle,
			@RequestBody @Valid final JobApplicationDto jobApplicationDto) throws BaseException {
		jobApplicationService.createJobApplication(jobTitle, jobApplicationDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<JobApplicationDto>> getAllJobApplications(
			@PathVariable @Valid @NotBlank final String jobTitle) throws BaseException {
		return new ResponseEntity<>(jobApplicationService.getAllJobApplicationsByJobTitle(jobTitle), HttpStatus.OK);
	}

	@GetMapping("/{email}")
	public ResponseEntity<JobApplicationDto> getJobApplicationByEmailId(
			@PathVariable @Valid @NotBlank final String jobTitle, @PathVariable @Valid @Email final String email)
			throws BaseException {
		return new ResponseEntity<>(jobApplicationService.getJobApplicationByJobTitleAndEmailId(jobTitle, email), HttpStatus.OK);
	}

	@PutMapping("/{email}")
	public ResponseEntity<Void> updateJobApplicationStatus(@PathVariable @Valid @NotBlank final String jobTitle,
			@PathVariable @Valid @Email final String email, @RequestBody final String applicationStatus)
			throws BaseException {
		jobApplicationService.updateJobApplicationStatus(email, jobTitle, getApplicationStatusEnum(applicationStatus));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private ApplicationStatusEnum getApplicationStatusEnum(final String applicationStatus) throws ValidationException {
		ApplicationStatusEnum applicationStatusEnum = EnumUtils.getEnum(ApplicationStatusEnum.class, applicationStatus);
		if (null != applicationStatusEnum) {
			return applicationStatusEnum;
		}
		LOGGER.error("Invalid Job application Status : {}", applicationStatus);
		throw new ValidationException(ApplicationConstant.JOB_APPLICATION_STATUS_INVALID, "Invalid Application Status");
	}

}
