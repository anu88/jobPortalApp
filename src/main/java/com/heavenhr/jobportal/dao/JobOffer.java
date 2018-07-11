package com.heavenhr.jobportal.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "job_offer", uniqueConstraints = @UniqueConstraint(columnNames = "job_title"))
public class JobOffer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "job_title")
	private String jobTitle;

	@Column(name = "start_date")
	private Date startDate;

	@OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<JobApplication> jobApplications = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(final String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Set<JobApplication> getJobApplications() {
		return new HashSet<>(jobApplications);
	}

	public void addJobApplication(final JobApplication jobApplication) {
		this.jobApplications.add(jobApplication);
	}

	public void addJobApplications(final Collection<JobApplication> jobApplications) {
		this.jobApplications.addAll(jobApplications);
	}
}
