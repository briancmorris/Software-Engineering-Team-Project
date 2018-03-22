#Author aagarw10

Feature: Lab Procedures
	As an Admin	
	I want to search for patients
	So that I can quickly retrieve and print a report showing the most relevant information for a patient
	As an HCP
	I want to include a lab procedure within an office visit documentation
	So that I can diagnose patients with a specific lab procedure
	As a Patient
	I want to view lab procedures within an office visit documentation
	So that I am aware of what procedures are recommended for me
	As a Lab Tech
	I want to view assigned lab procedures
	So that I can administer these procedures to the patients
	

Scenario Outline: Admin Create Lab Procedure
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to Manage Lab Procedures page
	And I fill in all necessary fields for the new lab procedure with long common name: <LC name>, component: <component>, property: <property>, and LOINC code: <LOINC>
	And I submit the change
	Then a success message is displayed
	And I am greeted with a view of the created lab procedure added to the Existing Lab Procedures view
	
Examples:
	| user   | pass   | name   | role     | LC name   | component       | property  | LOINC    | type   |
	| aagarw | 123456 | Anu    | Admin    | LP1       | Known Exposure  | Prid      | 88570-7  | create |
	
	
Scenario Outline: HCP Add Lab Procedure
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to Document Office Visit page
	And I fill in default information fields for an office visit
	And I enter lab procedure long common name: <LC name>, LOINC code; <LOINC>, priority: <priority>, and comment: <comment> to add to office visit
	And I submit the change
	Then a success message is displayed
	
Examples:
	| user   | pass   | name   | role     | LC name  | LOINC   | priority | comment   |
	| aagarw | 123456 | Anu    | HCP      | LP1      | 88570-7 | 4        | test      |
	
	
Scenario Outline: Lab Tech View Lab Procedure
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to View Lab Procedures page
	Then I am greeted with a view of the list of all lab procedures assigned to me
	
Examples:
	| user   | pass   | name   | role     | type   |
	| aagarw | 123456 | Anu    | Lab Tech | view   |
	
	
Scenario Outline: Patient View Lab Procedure
	Given I have logged in with username: <user> and password: <pass>
	When I navigate to Document Office Visit page
	Then I am greeted with a view of all documented lab procedures included within scheduled office visits
	
Examples:
	| user   | pass   | name   | role     | type   |
	| aagarw | 123456 | Anu    | Patient  | view   |