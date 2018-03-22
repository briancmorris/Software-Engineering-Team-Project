#Author aagarw10

Feature: Personal Representatives
	As a Patient
	I want to edit and view my representatives
	So that they can view records and make health decisions for me
	

Scenario Outline: Declare Representatives
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Representatives page
	And I add a patient username: <representative> as a representative of mine
	And I submit the change
	Then a success message is displayed
	And I am presented with an added representative to the list of all representatives of mine
	
Examples:
	| user   | pass   | name   | role     | representative            | type     |
	| aagarw | 123456 | Anu    | Patient  | svang (Shelly) - Patient  | declare  |


Scenario Outline: Undeclare Representatives
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Representatives page
	And I undeclare patient username: <representative> as a representative of mine
	And I submit the change
	Then a success message is displayed
	And I am presented with a representative removed from the list of all representatives of mine
	
Examples:
	| user   | pass   | name   | role     | representative            | type       |
	| aagarw | 123456 | Anu    | Patient  | svang (Shelly) - Patient  | undeclare  |


Scenario Outline: Patient View Representatives
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Representatives page
	Then I am presented with the lists of all representatives who I represent and who represents me

Examples:
	| user   | pass   | name   | role     | type  |
	| aagarw | 123456 | Anu    | Patient  | view  |