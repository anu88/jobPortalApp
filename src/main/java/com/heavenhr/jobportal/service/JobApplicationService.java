package com.heavenhr.jobportal.service;

import java.util.List;

import com.heavenhr.jobportal.dao.ApplicationStatusEnum;
import com.heavenhr.jobportal.dto.JobApplicationDto;
import com.heavenhr.jobportal.exception.BaseException;

public interface JobApplicationService {

	List<JobApplicationDto> getAllJobApplicationsByJobTitle(String jobTitle) throws BaseException;

	JobApplicationDto getJobApplicationByJobTitleAndEmailId(String jobTitle, String emailId) throws BaseException;

	void createJobApplication(String jobTitle, JobApplicationDto jobApplicationDto) throws BaseException;

	void updateJobApplicationStatus(String emailId, String jobTitle, ApplicationStatusEnum applicationStatusEnum)
			throws BaseException;
}
