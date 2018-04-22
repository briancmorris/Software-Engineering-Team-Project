#Author aagarw10

Feature: Personal Representatives
	As a Patient
	I want to edit and view my representatives
	So that they can view records and make health decisions for me
	

Scenario Outline: Declare Representatives
	Given A patient exists with the name: <first> <last> and DOB: <dob>
	When I log into iTrust2 as a Patient
	When I navigate to the Edit Personal Representatives page
	When I add a patient by username: <username> as a representative of mine
	Then The representative: <username> is successfully added
	When Added representative logs into into iTrust2 as Patient
	When Representative navigates to the Edit Personal Representatives page
	Then Representative is presented with a patient in the list of patients he represents
	
Examples:
	| first    | last     | dob      | username      |
	| Nellie   | Sanderson|04/24/1993| AliceThirteen |


Scenario Outline: Undeclare Representatives
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Representatives page
	And I undeclare patient username: <representative> as a representative of mine
	And I submit the change
	Then a success message is displayed
	And I am presented with a representative removed from the list of all representatives of mine
	Then I log out of iTrust2 account
	And Shelly has logged in with username: <rep MID> and password: <rep Name>
	When Shelly navigates to View Representatives page
	Then Shelly is presented with an empty list of all patients she represents
	
Examples:
	| user   | pass   | name   | role     | rep MID         | rep Name          | type       |
	| aagarw | 123456 | Anu    | Patient  | svang (Patient) | Shelly (Patient)  | undeclare  |


Scenario Outline: Patient View Representatives
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Representatives page
	Then I am presented with the lists of all representatives who I represent and who represents me

Examples:
	| user   | pass   | name   | role     | type  |
	| aagarw | 123456 | Anu    | Patient  | view  |