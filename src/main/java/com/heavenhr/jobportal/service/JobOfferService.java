package com.heavenhr.jobportal.service;

import java.util.List;

import com.heavenhr.jobportal.exception.ValidationException;
import com.heavenhr.jobportal.dto.JobOfferDto;

public interface JobOfferService {
	
	List<JobOfferDto> getAllJobOffers();

	JobOfferDto getJobOfferByTitle(String jobTitle) throws ValidationException;

	void createJobOffer(JobOfferDto jobOfferDto) throws ValidationException;

}
