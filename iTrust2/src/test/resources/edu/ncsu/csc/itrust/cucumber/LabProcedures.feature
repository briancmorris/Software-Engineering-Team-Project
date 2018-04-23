Feature: Creating and using Lab Procedures
	As an Admin
	I want to be able to create, edit, and delete LOINC codes
	As an HCP
	I want to be able to add lab procedures to my OfficeVisits
	
Scenario Outline: Managing LOINC codes and adding LabProcedure
	Given I am logged in as an admin
	When I navigate to view the Manage LOINC code page
	And I add a new code with <code>, <long_name>, <special_usage>, <component>, and <property>
	Then I can see the code added to the page
	Given The required hospital and patient exist for me
	When I am logged in as an HCP
	When I go to the Office Visit page to create a new lab
	When I fill in information with the LabProcedure
	Then The office visit is created successfully with the added labProcedure
	
	
Examples:
	| code | long_name | special_usage | component | property |
	| 12345-7 | thisisatest | specialtest | componenttest | proptest |