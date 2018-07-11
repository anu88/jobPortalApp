package com.heavenhr.jobportal.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "job_application")
public class JobApplication implements Serializable {

	private static final long serialVersionUID = -3903862846479954379L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ApplicationStatusEnum status;

	@Column(name = "resume_text")
	private String resumeText;

	@Column(name = "email")
	private String emailId;

	@ManyToOne
	private JobOffer jobOffer;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public ApplicationStatusEnum getApplicationStatus() {
		return status;
	}

	public void setApplicationStatus(final ApplicationStatusEnum applicationStatus) {
		this.status = applicationStatus;
	}

	public String getResumeText() {
		return resumeText;
	}

	public void setResumeText(final String resumeText) {
		this.resumeText = resumeText;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(final String emailId) {
		this.emailId = emailId;
	}

	public JobOffer getJobOffer() {
		return jobOffer;
	}

	public void setJobOffer(final JobOffer jobOffer) {
		this.jobOffer = jobOffer;
	}
	
	@Override
    public boolean equals(Object o) {
      return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
