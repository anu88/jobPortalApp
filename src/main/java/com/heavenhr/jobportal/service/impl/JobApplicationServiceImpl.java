package com.heavenhr.jobportal.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.heavenhr.jobportal.async.event.JobApplicationStatusChangeEvent;
import com.heavenhr.jobportal.constant.ApplicationConstant;
import com.heavenhr.jobportal.dao.ApplicationStatusEnum;
import com.heavenhr.jobportal.dao.JobApplication;
import com.heavenhr.jobportal.dao.JobOffer;
import com.heavenhr.jobportal.dao.repository.JobApplicationRepository;
import com.heavenhr.jobportal.dao.repository.JobOfferRepository;
import com.heavenhr.jobportal.dto.JobApplicationDto;
import com.heavenhr.jobportal.exception.BaseException;
import com.heavenhr.jobportal.exception.ValidationException;
import com.heavenhr.jobportal.service.JobApplicationService;

@Service
public class JobApplicationServiceImpl implements JobApplicationService, ApplicationEventPublisherAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationServiceImpl.class);

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Autowired
	private JobOfferRepository jobOfferRepository;

	private ApplicationEventPublisher publisher;

	@Override
	public JobApplicationDto getJobApplicationByJobTitleAndEmailId(final String jobTitle, final String emailId)
			throws ValidationException {
		final JobOffer jobOffer = jobOfferRepository.findByJobTitleIgnoreCase(jobTitle);
		if (null != jobOffer) {
			final JobApplication jobApplication = jobOffer.getJobApplications().stream()
					.filter(jobApplicationEntity -> jobApplicationEntity.getEmailId().equalsIgnoreCase(emailId))
					.findFirst()
					.orElseThrow(() -> new ValidationException(ApplicationConstant.JOB_APPLICATION_NOT_FOUND,
							MessageFormat.format("Job Application with email id: {0} doesn't exist.", emailId)));
			return new JobApplicationDto(jobApplication);
		} else {
			LOGGER.error("Job Offer with title: {} doesn't exist.", jobTitle);
			throw new ValidationException(ApplicationConstant.JOB_OFFER_NOT_FOUND,
					MessageFormat.format("Job Offer with title: {0} doesn't exist.", jobTitle));
		}
	}

	@Override
	public List<JobApplicationDto> getAllJobApplicationsByJobTitle(final String jobTitle) throws ValidationException {
		final JobOffer jobOffer = jobOfferRepository.findByJobTitleIgnoreCase(jobTitle);
		if (null != jobOffer) {
			return jobOffer.getJobApplications().stream().map(jobApplication -> new JobApplicationDto(jobApplication))
					.collect(Collectors.toList());
		}
		LOGGER.error("Job Offer with title: {} doesn't exist.", jobTitle);
		throw new ValidationException(ApplicationConstant.JOB_OFFER_NOT_FOUND,
				MessageFormat.format("Job Offer with title: {0} doesn't exist.", jobTitle));
	}

	@Override
	@Transactional
	public void createJobApplication(final String jobTitle, final JobApplicationDto jobApplicationDto)
			throws BaseException {
		final JobOffer jobOffer = jobOfferRepository.findByJobTitleIgnoreCase(jobTitle);
		if (null != jobOffer) {
			validateJobApplication(jobOffer, jobApplicationDto.getCandidateEmail());
			final JobApplication jobApplication = jobApplicationDto.toEntity();
			jobApplication.setJobOffer(jobOffer);
			jobOffer.addJobApplication(jobApplication);
			jobOfferRepository.save(jobOffer);
			LOGGER.info("Job application for job offer: {} created successfully.", jobTitle);
		} else {
			LOGGER.error("Job Offer with title: {} doesn't exist.", jobTitle);
			throw new ValidationException(ApplicationConstant.JOB_OFFER_NOT_FOUND,
					MessageFormat.format("Job Offer with title: {0} doesn't exist.", jobTitle));
		}
	}

	@Override
	@Transactional
	public void updateJobApplicationStatus(final String emailId, final String jobTitle,
			final ApplicationStatusEnum applicationStatus) throws ValidationException {

		final JobApplication jobApplication = jobApplicationRepository.findByEmailIdIgnoreCase(emailId)
				.stream()
				.filter(jobApplicationEntity -> jobApplicationEntity.getJobOffer() != null
						&& jobApplicationEntity.getJobOffer().getJobTitle().equalsIgnoreCase(jobTitle))
				.findFirst()
				.orElseThrow(logAndThrowValidationException(emailId, jobTitle));

		jobApplication.setApplicationStatus(applicationStatus);
		jobApplicationRepository.save(jobApplication);
		LOGGER.info("Job application of {} updated successfully.", emailId);
		publishStatusChangeEvent(jobApplication.getEmailId(), applicationStatus);
	}

	@Override
	public void setApplicationEventPublisher(final ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	private void validateJobApplication(final JobOffer jobOffer, final String email) throws ValidationException {
		boolean isJobApplicationAlreadyExists = jobOffer.getJobApplications().stream()
				.anyMatch(jobApplication -> jobApplication.getEmailId().equalsIgnoreCase(email));
		if (isJobApplicationAlreadyExists) {
			throw new ValidationException(ApplicationConstant.JOB_APPLICATION_ALREADY_EXISTS,
					MessageFormat.format("Job Application for job title: {0} and with emaid: {1} already exists.",
							jobOffer.getJobTitle(), email));
		}
	}

	private void publishStatusChangeEvent(final String canndidateEmail, final ApplicationStatusEnum applicationStatus) {
		publisher.publishEvent(new JobApplicationStatusChangeEvent(this, canndidateEmail, applicationStatus));
	}
	
	private Supplier<? extends ValidationException> logAndThrowValidationException(final String emailId,
			final String jobTitle) {
		return () -> {
			LOGGER.error("Job Application of {} for job offer: {} doesn't exist.", emailId, jobTitle);
			return new ValidationException(ApplicationConstant.JOB_APPLICATION_NOT_FOUND, MessageFormat
					.format("Job Application of {0} for job offer: {1} doesn't exist.", emailId, jobTitle));
		};
	}
}
