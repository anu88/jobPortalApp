package com.heavenhr.jobportal.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heavenhr.jobportal.dto.JobOfferDto;
import com.heavenhr.jobportal.exception.BaseException;
import com.heavenhr.jobportal.service.JobOfferService;

@RestController
@RequestMapping(value = "/joboffers")
public class JobOfferController {

	@Autowired
	private JobOfferService jobOfferService;

	@PostMapping
	public ResponseEntity<Void> createJobOffer(@RequestBody @Valid final JobOfferDto jobOfferDto) throws BaseException {
		jobOfferService.createJobOffer(jobOfferDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<JobOfferDto>> getAllJobOffers() throws BaseException {
		return new ResponseEntity<List<JobOfferDto>>(jobOfferService.getAllJobOffers(), HttpStatus.OK);
	}

	@GetMapping("/{jobTitle}")
	public ResponseEntity<JobOfferDto> getJobOfferByTitle(@PathVariable @NotBlank @Valid final String jobTitle)
			throws BaseException {
		return new ResponseEntity<JobOfferDto>(jobOfferService.getJobOfferByTitle(jobTitle), HttpStatus.OK);
	}
}
