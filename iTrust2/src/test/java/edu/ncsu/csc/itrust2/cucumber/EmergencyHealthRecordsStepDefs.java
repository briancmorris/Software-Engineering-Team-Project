package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

public class EmergencyHealthRecordsStepDefs {

    private final WebDriver driver  = new HtmlUnitDriver( true );
    private final String    baseUrl = "http://localhost:8080/iTrust2";
    WebDriverWait           wait    = new WebDriverWait( driver, 2 );

    @Before
    public void setup () {

        HibernateDataGenerator.generateUsers();

    }

    @After
    public void tearDown () {
        driver.close();
    }

    @Given ( "A ER role user is a registered user" )
    public void initERUser () {
        final User er = new User( "er", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_ER,
                1 );
        er.save();
    }

    @Given ( "user (.+) with first name: (.+) and last name: (.+)  is registered as a patient within the iTrust2 system" )
    public void initPatient ( final String username, final String name, final String last ) throws ParseException {
        // user "gcwatts"
        // name "Garrett"

        final User patient = new User( username, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        patient.save();

        // Add demographics for the user
        // To use this cucumber test with different users, each of these would
        // need to be a variable

        final PatientForm form = new PatientForm();
        form.setFirstName( name );
        form.setLastName( last );
        form.setEmail( "bademail@ncsu.edu" );
        form.setAddress1( "Some town" );
        form.setAddress2( "Somewhere" );
        form.setCity( "placecity" );
        form.setState( State.AL.getName() );
        form.setZip( "27606" );
        form.setPhone( "111-111-1111" );
        form.setDateOfBirth( "01/01/1901" );
        form.setBloodType( BloodType.ABPos.getName() );
        form.setEthnicity( Ethnicity.Asian.getName() );
        form.setGender( Gender.Male.getName() );
        form.setSelf( patient.getUsername() );

        final Patient aagarw = new Patient( form );
        aagarw.save();
    }

    @When ( "I have logged in as an er" )
    public void attemptLogin2 () {
        driver.get( baseUrl );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "username" ) ) );

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( "er" );
        final WebElement passwordField = driver.findElement( By.name( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

    }

    @When ( "I navigate to View Emergency Records page for all patients in iTrust2 system" )
    public void viewEHR () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('requesthealthrecordser').click();" );
    }

    @When ( "I search for patient with the field: (.+) to view their emergency records with id (.+)" )
    public void searchEHRField ( final String name, final String username ) {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "search" ) ) );
        final WebElement searchField = driver.findElement( By.name( "search" ) );
        searchField.clear();
        searchField.sendKeys( name );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//input[@value='" + username + "']" ) ) );
        driver.findElement( By.xpath( "//input[@value='" + username + "']" ) ).click();
    }

    @When ( "I search by ID for patient with the field: (.+) to view their records" )
    public void searchEHRID ( final String username ) {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "search" ) ) );
        final WebElement searchField = driver.findElement( By.name( "search" ) );
        searchField.clear();
        searchField.sendKeys( username );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//input[@value='" + username + "']" ) ) );
        driver.findElement( By.xpath( "//input[@value='" + username + "']" ) ).click();
    }

    @Then ( "I am greeted with a printable page present with all relevant info related to emergency records for (.+)" )
    public void checkEHR ( final String name ) throws InterruptedException {
        Thread.sleep( 5000 );

        // Check that the patient was made with correct demographics before
        // checking the page for the demographics
        final Patient p = Patient.getByName( name );
        assertTrue( p.getCity().equals( "placecity" ) );

        // Assert demographics used above
        assertTrue( driver.getPageSource().contains( "01/01/1901" ) );
        assertTrue( driver.getPageSource().contains( "Male" ) );
        assertTrue( driver.getPageSource().contains( "AB+" ) );

    }

}
