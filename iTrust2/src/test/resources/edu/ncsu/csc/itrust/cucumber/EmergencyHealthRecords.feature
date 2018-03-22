#Author aagarw10

Feature: Emergency Health Records
	As an HCP or ER
	I want to search for patients
	So that I can quickly retrieve and print a report showing the most relevant information for a patient
	

Scenario Outline: Search Patient Emergency Health Records By Name
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Emergency Records page for all patients in iTrust2 system
	And I search for patient name: Shelly to view her emergency records
	Then I am greeted with a printable page present with all relevant info related to emergency records for "Shelly"
	
Examples:
	| user   | pass   | name   | role     | patient          |
	| aagarw | 123456 | Anu    | Patient  | svang (Shelly)   |
	
	
Scenario Outline: Search Patient Emergency Health Records By MID
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Emergency Records page for all patients in iTrust2 system
	And I search for patient MID: svang to view her emergency record
	Then I am greeted with a printable page present with all relevant info related to emergency records for "svang"
    
Examples:
	| user   | pass   | name   | role     | patient          |
	| aagarw | 123456 | Anu    | Patient  | svang (Shelly)   |