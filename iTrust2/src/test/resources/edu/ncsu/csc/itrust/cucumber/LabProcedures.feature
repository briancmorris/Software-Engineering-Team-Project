Feature: Creating and using Lab Procedures
	As an Admin
	I want to be able to create, edit, and delete LOINC codes
	As an HCP
	I want to be able to add lab procedures to my OfficeVisits
	As an LT
	I want to be able to view my procedures, edit them, and reassign them
	So that patients can receive lab treatment to help with diagnosis
	
Scenario Outline: Managing LOINC codes
	Given I am logged in as an admin
	When I navigate to view the Manage LOINC code page
	And I add a new code with <code>, <long_name>, <special_usage>, <component>, and <property>
	Then I can see the code added to the page
	And I can also delete the code
	Then see that the code was successfully removed
	
Examples:
	| code | long_name | special_usage | component | property |
	| 12345-7 | thisisatest | specialtest | componenttest | proptest |