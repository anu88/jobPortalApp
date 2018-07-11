package com.heavenhr.jobportal.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.heavenhr.jobportal.async.event.JobApplicationStatusChangeEvent;
import com.heavenhr.jobportal.dao.ApplicationStatusEnum;
import com.heavenhr.jobportal.dao.JobApplication;
import com.heavenhr.jobportal.dao.JobOffer;
import com.heavenhr.jobportal.dao.repository.JobApplicationRepository;
import com.heavenhr.jobportal.dao.repository.JobOfferRepository;
import com.heavenhr.jobportal.dto.JobApplicationDto;
import com.heavenhr.jobportal.exception.ValidationException;
import com.heavenhr.jobportal.service.impl.JobApplicationServiceImpl;

public class JobApplicationServiceImplTest {

	private static final String JOB_TITLE_SDE1 = "SDE1";

	@InjectMocks
	private JobApplicationServiceImpl jobApplicationService;

	@Mock
	private JobApplicationRepository jobApplicationRepository;

	@Mock
	private JobOfferRepository jobOfferRepository;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAllJobApplicationsByJobTitle_withOneJobApplication_returnNonEmptyList() throws Exception {

		final JobOffer jobOffer = getJobOffer(JOB_TITLE_SDE1);
		final JobApplication jobApplication = getJobApplication(jobOffer);
		jobOffer.addJobApplication(jobApplication);
		Mockito.when(jobOfferRepository.findByJobTitleIgnoreCase(JOB_TITLE_SDE1)).thenReturn(jobOffer);

		List<JobApplicationDto> jobApplicationDtos = jobApplicationService
				.getAllJobApplicationsByJobTitle(JOB_TITLE_SDE1);

		Mockito.verify(jobOfferRepository, Mockito.times(1)).findByJobTitleIgnoreCase(JOB_TITLE_SDE1);
		Assert.assertEquals(1, jobApplicationDtos.size());
		assertJobApplicationEquals(jobApplication, jobApplicationDtos.get(0));
	}

	@Test
	public void getAllJobApplicationsByJobTitle_withZeroJobApplication_returnEmptyList() throws Exception {
		// Pending implementation
		assertTrue(true);
	}

	@Test(expected = ValidationException.class)
	public void getAllJobApplicationsByJobTitle_withNoJobOffer_exceptionThrown() throws Exception {
		// Pending implementation
		throw new ValidationException();
	}

	@Test
	public void getAllApplicationsByJobTitleAndEmailId_validData_returnJobApplication() throws Exception {
		final String emailId = "email@email.com";
		final JobOffer jobOffer = getJobOffer(JOB_TITLE_SDE1);
		final JobApplication jobApplication = getJobApplication(jobOffer);
		jobOffer.addJobApplication(jobApplication);
		Mockito.when(jobOfferRepository.findByJobTitleIgnoreCase(JOB_TITLE_SDE1)).thenReturn(jobOffer);

		final JobApplicationDto jobApplicationDto = jobApplicationService
				.getJobApplicationByJobTitleAndEmailId(JOB_TITLE_SDE1, emailId);

		Mockito.verify(jobOfferRepository, Mockito.times(1)).findByJobTitleIgnoreCase(JOB_TITLE_SDE1);
		assertJobApplicationEquals(jobApplication, jobApplicationDto);
	}

	@Test(expected = ValidationException.class)
	public void getAllApplicationsByJobTitleAndEmailId_jobOfferNotExist_exceptionThrown() throws Exception {
		// Pending implementation
		throw new ValidationException();
	}

	@Test(expected = ValidationException.class)
	public void getAllApplicationsByJobTitleAndEmailId_applicationNotExistForJobOffer_exceptionThrown()
			throws Exception {
		// Pending implementation
		throw new ValidationException();
	}

	@Test
	public void createJobApplication_validRequest_executeSuccessfully() throws Exception {
		final JobOffer jobOffer = getJobOffer(JOB_TITLE_SDE1);
		Mockito.when(jobOfferRepository.findByJobTitleIgnoreCase(JOB_TITLE_SDE1)).thenReturn(jobOffer);
		final JobApplicationDto jobApplicationDto = getJobApplicationDto();
		ArgumentCaptor<JobOffer> captor = ArgumentCaptor.forClass(JobOffer.class);
		jobApplicationService.createJobApplication(JOB_TITLE_SDE1, jobApplicationDto);
		Mockito.verify(jobOfferRepository).save(captor.capture());

		final JobOffer actualJobOffer = captor.getValue();
		Assert.assertEquals(1, actualJobOffer.getJobApplications().size());
		assertJobApplicationEquals(actualJobOffer.getJobApplications().stream().findFirst().get(), jobApplicationDto);
	}

	@Test(expected = ValidationException.class)
	public void createJobApplication_applicationAlreadyExist_exceptionThrown() throws Exception {
		// Pending implementation
		throw new ValidationException();
	}

	@Test(expected = ValidationException.class)
	public void createJobApplication_jobOfferNotExist_exceptionThrown() throws Exception {
		// Pending implementation
		throw new ValidationException();
	}

	@Test
	public void updateJobApplicationStatus_validJobOfferAndApplication_statusUpdatedAndEventPublished()
			throws Exception {
		final String emailId = "email@email.com";
		final JobOffer jobOffer = getJobOffer(JOB_TITLE_SDE1);
		final JobApplication jobApplication = getJobApplication(jobOffer);
		final List<JobApplication> jobApplications = Lists.newArrayList(getJobApplication(jobOffer));
		Mockito.when(jobApplicationRepository.findByEmailIdIgnoreCase(emailId)).thenReturn(jobApplications);

		ArgumentCaptor<JobApplication> jobApplicationCaptor = ArgumentCaptor.forClass(JobApplication.class);
		ArgumentCaptor<JobApplicationStatusChangeEvent> jobApplicationStatusChangeEventCaptor = ArgumentCaptor
				.forClass(JobApplicationStatusChangeEvent.class);

		jobApplicationService.updateJobApplicationStatus(emailId, JOB_TITLE_SDE1, ApplicationStatusEnum.INVITED);

		Mockito.verify(jobApplicationRepository).save(jobApplicationCaptor.capture());
		final JobApplication actualJobApplication = jobApplicationCaptor.getValue();
		Assert.assertEquals(ApplicationStatusEnum.INVITED, actualJobApplication.getApplicationStatus());
		Assert.assertEquals(jobApplication.getEmailId(), actualJobApplication.getEmailId());
		Assert.assertEquals(jobApplication.getResumeText(), actualJobApplication.getResumeText());
		Assert.assertEquals(jobApplication.getId(), actualJobApplication.getId());
		Assert.assertEquals(jobApplication.getJobOffer(), actualJobApplication.getJobOffer());

		Mockito.verify(eventPublisher).publishEvent((jobApplicationStatusChangeEventCaptor.capture()));
		final JobApplicationStatusChangeEvent statusChangeEvent = jobApplicationStatusChangeEventCaptor.getValue();
		Assert.assertEquals(ApplicationStatusEnum.INVITED, statusChangeEvent.getApplicationStatus());
		Assert.assertEquals(emailId, statusChangeEvent.getCandidateEmail());
	}

	@Test(expected = ValidationException.class)
	public void updateJobApplication_jobApplicationNotExist_exceptionThrown() throws Exception {
		// Pending implementation
		throw new ValidationException();

	}

	private JobApplication getJobApplication(final JobOffer jobOffer) {
		JobApplication jobApplication = new JobApplication();
		jobApplication.setJobOffer(jobOffer);
		jobApplication.setApplicationStatus(ApplicationStatusEnum.APPLIED);
		jobApplication.setResumeText("Resume");
		jobApplication.setEmailId("email@email.com");
		return jobApplication;
	}

	private JobApplicationDto getJobApplicationDto() {
		JobApplicationDto jobApplicationDto = new JobApplicationDto();
		jobApplicationDto.setResumeText("Resume");
		jobApplicationDto.setCandidateEmail("email@email.com");
		return jobApplicationDto;
	}

	private JobOffer getJobOffer(final String jobTitle) {
		JobOffer jobOffer = new JobOffer();
		jobOffer.setId(1L);
		jobOffer.setJobTitle(jobTitle);
		jobOffer.setStartDate(new Date(2018 / 11 / 21));
		return jobOffer;
	}

	private void assertJobApplicationEquals(final JobApplication jobApplication,
			final JobApplicationDto jobApplicationDto) {
		Assert.assertEquals(jobApplication.getEmailId(), jobApplicationDto.getCandidateEmail());
		Assert.assertEquals(jobApplication.getResumeText(), jobApplicationDto.getResumeText());
	}
}
