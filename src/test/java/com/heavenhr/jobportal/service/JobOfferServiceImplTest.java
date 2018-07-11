package com.heavenhr.jobportal.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.heavenhr.jobportal.dao.JobOffer;
import com.heavenhr.jobportal.dao.repository.JobApplicationRepository;
import com.heavenhr.jobportal.dao.repository.JobOfferRepository;
import com.heavenhr.jobportal.dto.JobOfferDto;
import com.heavenhr.jobportal.exception.ValidationException;
import com.heavenhr.jobportal.service.impl.JobOfferServiceImpl;

public class JobOfferServiceImplTest {

	private static final String JOB_TITLE_SDE1 = "SDE1";

	@InjectMocks
	private JobOfferServiceImpl jobOfferService;

	@Mock
	private JobApplicationRepository jobApplicationRepository;

	@Mock
	private JobOfferRepository jobOfferRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAllJobOffers_offerExists_returnNonEmptyList() {
		final List<JobOffer> jobOffers = Lists.newArrayList(getJobOffer(JOB_TITLE_SDE1));
		Mockito.when(jobOfferRepository.findAll()).thenReturn(jobOffers);
		List<JobOfferDto> actualJobOffers = jobOfferService.getAllJobOffers();

		Mockito.verify(jobOfferRepository, Mockito.times(1)).findAll();
		Assert.assertEquals(1, actualJobOffers.size());
		assertEquals(jobOffers.get(0), actualJobOffers.get(0));
	}

	@Test
	public void getAllJobOffers_noOfferExists_returnEmptyList() {
		// pending implementation
		assertTrue(true);
	}

	@Test
	public void getJobOfferByTitle_offerExists_returnJobOffer() throws Exception {
		final JobOffer jobOffer = getJobOffer(JOB_TITLE_SDE1);
		Mockito.when(jobOfferRepository.findByJobTitleIgnoreCase(JOB_TITLE_SDE1)).thenReturn(jobOffer);
		final JobOfferDto jobOfferDto = jobOfferService.getJobOfferByTitle(JOB_TITLE_SDE1);

		Mockito.verify(jobOfferRepository, Mockito.times(1)).findByJobTitleIgnoreCase(JOB_TITLE_SDE1);
		assertEquals(jobOffer, jobOfferDto);
	}

	@Test(expected = ValidationException.class)
	public void getJobOfferByTitle_offerNotExists_exceptionThrown() throws Exception {
		Mockito.when(jobOfferRepository.findByJobTitleIgnoreCase(JOB_TITLE_SDE1)).thenReturn(null);
		jobOfferService.getJobOfferByTitle(JOB_TITLE_SDE1);
	}

	@Test
	public void createJobOffer_validRequest_offerCreated() throws Exception {
		final JobOfferDto jobOfferDto = getJobOfferDto();
		final ArgumentCaptor<JobOffer> captor = ArgumentCaptor.forClass(JobOffer.class);

		jobOfferService.createJobOffer(jobOfferDto);

		Mockito.verify(jobOfferRepository).save(captor.capture());
		final JobOffer jobOffer = captor.getValue();
		assertEquals(jobOffer, jobOfferDto);

	}

	private JobOffer getJobOffer(final String jobTitle) {
		JobOffer jobOffer = new JobOffer();
		jobOffer.setId(1L);
		jobOffer.setJobTitle(jobTitle);
		jobOffer.setStartDate(new Date(2018 / 11 / 21));
		return jobOffer;
	}

	private void assertEquals(final JobOffer jobOffer, final JobOfferDto jobOfferDto) {
		Assert.assertEquals(jobOffer.getJobTitle(), jobOfferDto.getJobTitle());
		Assert.assertEquals(DateFormatUtils.format(jobOffer.getStartDate(), "dd-mm-yyyy"), jobOfferDto.getStartDate());
	}

	private JobOfferDto getJobOfferDto() {
		JobOfferDto jobOfferDto = new JobOfferDto();
		jobOfferDto.setJobTitle(JOB_TITLE_SDE1);
		jobOfferDto.setStartDate("21-11-2018");
		return jobOfferDto;
	}

}
