#Author aagarw10

Feature: New Roles
	As an ER and Lab Tech
	I want the same basic functionality as other Personnel roles
	

Scenario Outline: ER Role User
	Given A ER role user is a registered user of the iTrust2 Medical Records system
	When I enter username: <user> and password: <pass> to log in as a user of the newly specified ER role
	Then I am greeted with a view of recent access logs of the iTrust2 system
	
Examples:
	| user   | pass   | name   | role   |
	| aagarw | 123456 | Anu    | ER     |


Scenario Outline: Lab Tech Role User
	Given A Lab Tech role user is a registered user of the iTrust2 Medical Records system
	When I enter username: <user> and password: <pass> to log in as a user of the newly specified Lab Tech role
	Then I am greeted with a view of recent access logs of the iTrust2 system
	
Examples:
	| user   | pass   | name   | role      |
	| aagarw | 123456 | Anu    | Lab Tech  |