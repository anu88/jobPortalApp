package com.heavenhr.jobportal.async.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.heavenhr.jobportal.async.event.JobApplicationStatusChangeEvent;

@Component
public class JobApplicationStatusChangeEventListener implements ApplicationListener<JobApplicationStatusChangeEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationStatusChangeEventListener.class);

	@Override
	public void onApplicationEvent(final JobApplicationStatusChangeEvent event) {
		final JobApplicationStatusChangeEvent statusChangeEvent = (JobApplicationStatusChangeEvent) event;
		LOGGER.info("*************** Job Application Status of {} updated to {} *******************", statusChangeEvent.getCandidateEmail(),
				statusChangeEvent.getApplicationStatus().name());
	}

}
