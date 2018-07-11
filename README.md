## Contents

 1. About
    1. Create Job Offer
    2. Get all job offers
    3. Get Job offer by Job title
    4. Create Job Application OR Apply for a job offer
    5. Get all applications for a job offer
    6. Get an individual job application for a job offer
    7. Update a job application status:
 2. How to build the application
 3. How to run the application
 4. Areas of improvement
 5. Estimates


## 1. About

```
Job Portal App is a web application for creating new Job Offers and job applications. A detailed description of API's available is as follow:
```
### 1. Create Job Offer

```
- This API will allow recruiter to create a new job offer.
- API URL & sample request JSON:
	http://localhost:8080/joboffers
	{
	"jobTitle": "Software Engineer",
	"startDate": "20- 09 - 2018"
	}
```
```
Validations:
- A job offer with same name (Case insensitive) can't be created.
- Accepted Date format is “dd-mm-yyyy”.
```
```
Scope of Improvements :
- Validations that the start date before current date is not accepted.
```
### 2. Get all job offers

```
- This API will list all the created job offers along with number of job applications.
- API URL & Sample response JSON:
	http://localhost:8080/joboffers
	[
	{
	"jobTitle": "Software Engineer",
	"startDate": "20- 09 - 2018",
	"numberOfApplications": 0
	},
	{
	"jobTitle": "Senior Software Engineer",
	"startDate": "21- 09 - 2018",
	"numberOfApplications": 0
	}
	]
```
### 3. Get Job offer by Job title

```
- This API will retrieve a job offer by its title.
```

```
- API URL & sample response JSON:
	http://localhost:8080/joboffers/SoftwareEngineer
	{
	"jobTitle": "SoftwareEngineer",
	"startDate": "20- 09 - 2018",
	"numberOfApplications": 0
	}
```
### 4. Create Job Application OR Apply for a job offer

```
- This API will allow Job seeker to apply for a job offer. Any new application has a status – APPLIED.
- API URL & sample Request JSON:
	http://localhost:8080/joboffers/SoftwareEngineer/jobapplications
	{
	"candidateEmail": "xyz@def.com",
	"resumeText": "Sample resume"
	}
```
```
Validations :
- Title of Job offer, candidate email and resume (as a String) is mandatory.
```
```
Scope of improvements:
- Resume should be persisted as byte array instead of text.
```
### 5. Get all applications for a job offer

```
- This API will allow recruiter to list all the applications for a job offer.
- API URL & sample response JSON:
	http://localhost:8080/joboffers/SoftwareEngineer/jobapplications
	[
		{
		"applicationStatus": "APPLIED",
		"resumeText": "Sample resume",
		"candidateEmail": "abc@def.com"
		},
		{
		"applicationStatus": "APPLIED",
		"resumeText": "Sample resume",
		"candidateEmail": "xyz@def.com"
		}
	]
```

```
Validations :
- Title of Job offer is mandatory.
```
```
Scope of improvements:
- Resume should not be part of response.
```
### 6. Get an individual job application for a job offer

```
- This API will allow recruiter to retrieve an individual application of a candidate by email Id for a job offer.
- API URL & sample response JSON:
	http://localhost:8080/joboffers/SoftwareEngineer/jobapplications/abc%40def.com
	{
	"applicationStatus": "APPLIED",
	"resumeText": "Sample resume",
	"candidateEmail": "abc@def.com"
	}
```
```
Validations :
- Title of Job offer and email Id is mandatory.
```
### 7. Update a job application status:

```
- This API will allow recruiter to update the application status of a candidate by his/her email Id for a job offer.
- Successful update of status will trigger a new event. For now, A logger entry will appear similar to:
“*************** Job Application Status of anuragsharma2088@gmail.com updated to HIRED *******************”
- API URL & sample request:
	http://localhost:8080/joboffers/SoftwareEngineer/jobapplications/abc%40def.com
	{
	INVITED
	}
```
```
Validations:
- Allowed application status are - APPLIED, INVITED, REJECTED, HIRED
- Email and Job offer title is mandatory.
```
```
Scope of improvements:
- Application status transition can have a validation. For e.g. An application status should not be allowed to change from REJECTED to HIRED.
```

## 2. How to build the application

```
JDK 8 and maven should be installed on your machine. For installing JDK 8 and maven please follow the hyperlinks. For building the application please follow the instructions as below:
```
```
- Download or clone the repository and Navigate to folder ‘...\JobPortalApp’ in command prompt.
- Run ‘mvn clean install’ for building the application
- After successful installation, it will create a target folder.
```
```
Please follow the instructions in next section to run the application.
```
## 3. How to run the application

```
- Navigate to the folder ‘...\ JobPortalApp\target\’ in command prompt.
- Run the command "java -jar JobPortalApp-0.0.1-SNAPSHOT.jar" which will start the application.
- Open the URL: http://localhost:8080/swagger-ui.html#/
- Navigate to the ‘createJobOffer’ API in ‘job-offer-controller’. Paste the request object as mentioned in ‘Create Job Offer’ and click on 'Try it out! '
- Above request will create a new job offer. Similarly, please try out the other API’s.
- For accessing the DB while application is running please open the URL and check the JDBC URL as ‘jdbc:h2:mem:test’ and username as  'sa' and click on ‘Connect’. Password is not required to access the DB.
```
```
Please note as this application runs on ‘in memory’ database, application restart will recreate the complete database and any persisted state of Job offer and job applications will cease to exist.
```
## 4. Areas of improvement

```
Though I’ve mentioned the improvement scope with individual API’s but below are some of the improvement area across the application.
```
- DB test and Integration tests should be added.
- Exception handling can be improved further by creating more custom exception classes.
- Project can be made modularized.
- Configuration file - _application.yml_ can be made environment specific using externalized config server.
- External messaging queue can be used for asynchronous events if consumer is a separate microservice.
- New API's can be exposed for candidates to access the job application status.

## 5. Estimates

```
I estimated 22-26 hours for the application. However, I was able to spend only 14 - 16 hours including design and coding.
```

