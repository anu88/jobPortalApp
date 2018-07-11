package com.heavenhr.jobportal.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heavenhr.jobportal.dao.JobApplication;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

	List<JobApplication> findByEmailIdIgnoreCase(final String emailId);

}
