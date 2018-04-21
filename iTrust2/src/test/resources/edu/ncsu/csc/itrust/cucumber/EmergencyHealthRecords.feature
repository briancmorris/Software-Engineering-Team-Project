Feature: Emergency Health Records
	As an HCP or ER
	I want to search for patients
	So that I can quickly retrieve and print a report showing the most relevant information for a patient
	

Scenario Outline: Search Patient Emergency Health Records By First Name
	Given A ER role user is a registered user
	And user <user> with first name: <firstname> and last name: <lastname>  is registered as a patient within the iTrust2 system
	When I have logged in as an er
	And I navigate to View Emergency Records page for all patients in iTrust2 system
	And I search for patient with the field: <firstname> to view their emergency records with id <user>
	Then I am greeted with a printable page present with all relevant info related to emergency records for <user>
	
Examples:
	| user    | firstname | lastname |
	| aagarw  | Anu       | Foo      |
	| gcwatts | Garrett   | Watts    |
	
Scenario Outline: Search Patient Emergency Health Records By Last Name
	Given A ER role user is a registered user
	And user <user> with first name: <firstname> and last name: <lastname>  is registered as a patient within the iTrust2 system
	When I have logged in as an er
	And I navigate to View Emergency Records page for all patients in iTrust2 system
	And I search for patient with the field: <lastname> to view their emergency records with id <user>
	Then I am greeted with a printable page present with all relevant info related to emergency records for <user>
	
Examples:
	| user    | firstname | lastname |
	| aagarw  | Anu       | Foo      |
	| gcwatts | Garrett   | Watts    |
	
	
Scenario Outline: Search Patient Emergency Health Records By ID
	Given A ER role user is a registered user
	And user <user> with first name: <firstname> and last name: <lastname>  is registered as a patient within the iTrust2 system
	When I have logged in as an er
	And I navigate to View Emergency Records page for all patients in iTrust2 system
	And I search by ID for patient with the field: <user> to view their records
	Then I am greeted with a printable page present with all relevant info related to emergency records for <user>
    
Examples:
	| user    | firstname | lastname |
	| aagarw  | Anu       | Foo      |
	| gcwatts | Garrett   | Watts    |

	
Scenario Outline: Invalid Search Patient Emergency Health Records
	Given A ER role user is a registered user
	And user <user> with first name: <firstname> and last name: <lastname>  is registered as a patient within the iTrust2 system
	When I have logged in as an er
	And I navigate to View Emergency Records page for all patients in iTrust2 system
	And I search by ID for patient with an invalid field
	Then I am greeted with no results because patient with id: <user> is not found
	
Examples:
	| user    | firstname | lastname |
	| aagarw  | Anu       | Foo      |