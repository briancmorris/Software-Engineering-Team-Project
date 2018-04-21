Feature: New Roles
	As an ER and Lab Tech
	I want the same basic functionality as other Personnel roles
	

Scenario Outline: ER Role User
	Given The ER role user is a registered user of the iTrust2 Medical Records system
	When I enter username: <user> and password: <pass> to log in
	Then I am greeted as an Emergency Responder user
	
Examples:
	| user   | pass   |
	| er     | 123456 |


Scenario Outline: Lab Tech Role User
	Given A Lab Tech role user is a registered user of the iTrust2 Medical Records system
	When I enter username: <user> and password: <pass> to log in
	Then I am greeted as a Lab Tech user
	
Examples:
	| user   | pass   |
	| lt     | 123456 |