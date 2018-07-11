package com.heavenhr.jobportal.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heavenhr.jobportal.constant.ApplicationConstant;
import com.heavenhr.jobportal.dao.JobOffer;
import com.heavenhr.jobportal.dao.repository.JobOfferRepository;
import com.heavenhr.jobportal.dto.JobOfferDto;
import com.heavenhr.jobportal.exception.ValidationException;
import com.heavenhr.jobportal.service.JobOfferService;

@Service
public class JobOfferServiceImpl implements JobOfferService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferServiceImpl.class);

	@Autowired
	private JobOfferRepository jobOfferRepository;

	@Override
	public List<JobOfferDto> getAllJobOffers() {
		return jobOfferRepository.findAll().stream().map(jobOffer -> new JobOfferDto(jobOffer))
				.collect(Collectors.toList());
	}

	@Override
	public JobOfferDto getJobOfferByTitle(final String jobTitle) throws ValidationException {
		final JobOffer jobOffer = jobOfferRepository.findByJobTitleIgnoreCase(jobTitle);
		if (jobOffer != null) {
			return new JobOfferDto(jobOffer);
		} else {
			LOGGER.error("Job Offer with title: {} doesn't exist.", jobTitle);
			throw new ValidationException(ApplicationConstant.JOB_OFFER_NOT_FOUND,
					MessageFormat.format("Job Offer with title: {0} doesn't exist.", jobTitle));
		}
	}

	@Override
	@Transactional
	public void createJobOffer(final JobOfferDto jobOfferDto) throws ValidationException {
		jobOfferRepository.save(jobOfferDto.toEntity());
	}

}
