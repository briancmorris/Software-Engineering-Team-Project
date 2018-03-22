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
	Then I log out of iTrust2 account
	And Shelly has logged in with username: <rep MID> and password: <rep Name>
	When Shelly navigates to View Representatives page
	Then Shelly is presented with an added representative user: aagarw10 and name: Anu in the list of all patients she represents
	
Examples:
	| user   | pass   | name   | role     | rep MID         | rep Name          | type     |
	| aagarw | 123456 | Anu    | Patient  | svang (Patient) | Shelly (Patient)  | declare  |


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