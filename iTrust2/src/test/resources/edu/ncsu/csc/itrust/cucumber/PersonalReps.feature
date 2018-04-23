#Author aagarw10

Feature: Personal Representatives
	As a Patient
	I want to edit and view my representatives
	So that they can view records and make health decisions for me
	

Scenario Outline: Declare Representatives
	Given A patient exists with name: <first> <last> and DOB: <dob>
	When I log into iTrust2 as sample Patient
	When I navigate to the Edit Personal Representatives page
	When I add a patient by username: <username> as a representative of mine
	Then The representative: <username> is successfully added
	
Examples:
	| first   | last     | dob      | username      |
	| Alice   | Thirteen |04/24/1993| AliceThirteen |


Scenario Outline: HCP View Representatives
	Given A rep exists with name: <first> <last> and DOB: <dob>
	When I log into iTrust2 again as sample HCP
	When I view the HCP Edit Personal Representatives page
	When I view representatives for: <username>
	Then I am presented with <rep> in the list of representatives 
	When I add another representative: <anotherRep>
	Then I am presented with another <anotherRep> added to the list of representatives

Examples:
	| first | last | dob      | username | rep          | anotherRep       |
	| Bob   | Four |02/22/1995| patient  | AliceThirteen| BobTheFourYearOld|


Scenario Outline: Undeclare Representatives
	When I log into iTrust2 again as sample Patient
	When I view Personal Representatives page
	When I undeclare previously added patient to not be a representative
	Then I am presented with an empty list of representatives
	
Examples:
	| first    | last     | dob      | username      |
	| Nellie   | Sanderson|04/24/1993| AliceThirteen |
	