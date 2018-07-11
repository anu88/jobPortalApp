package com.heavenhr.jobportal.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heavenhr.jobportal.dao.JobOffer;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

	JobOffer findByJobTitleIgnoreCase(final String jobTitle);

}
