package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class PersonalRepsStepDefs {

    private WebDriver    driver;
    private final String baseUrl = "http://localhost:8080/iTrust2";

    WebDriverWait        wait;

    @Before
    public void setup () {

        driver = new HtmlUnitDriver( true );
        wait = new WebDriverWait( driver, 5 );
    }

    @After
    public void tearDown () {
        driver.close();
    }

    @Given ( "A patient exists with the name: (.+) and DOB: (.+)" )
    public void patientExists ( final String name, final String birthday ) throws ParseException {

        /* Create patient record */

        final Patient patient = new Patient();
        patient.setSelf( User.getByName( "AliceThirteen" ) );

        patient.setFirstName( name.split( " " )[0] );
        patient.setLastName( name.split( " " )[1] );
        patient.setEmail( "email@mail.com" );
        patient.setAddress1( "847 address place" );
        patient.setCity( "citytown" );
        patient.setState( State.CA );
        patient.setZip( "91505" );
        patient.setPhone( "123-456-7890" );
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/DD/YYYY", Locale.ENGLISH );

        final Calendar time = Calendar.getInstance();
        time.setTime( sdf.parse( birthday ) );

        patient.setDateOfBirth( time );

        patient.save();

    }

    @When ( "I log into iTrust2 as a Patient" )
    public void patientLogin () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "I navigate to the Edit Personal Representatives page" )
    public void editRepresentatives () {
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('editpersonalrepresentatives-patient').click();" );
    }

    @When ( "I add a patient by username: (.+) as a representative of mine" )
    public void fillRepresentative ( final String username ) {
        final WebElement firstName = driver.findElement( By.name( "addRepForm" ) );
        firstName.clear();
        firstName.sendKeys( username );

        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @Then ( "The representative: (.+) is successfully added" )
    public void viewRepresentative ( final String username ) {
        final WebElement rep = driver.findElement( By.name( "idCell" ) );
        assertEquals( rep.getAttribute( "value" ), username );
    }

    @When ( "Added representative logs into into iTrust2 as Patient" )
    public void patientLogin2 () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "AliceThirteen" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "Representative navigates to the Edit Personal Representatives page" )
    public void viewAddedRepresentatives () {
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('editpersonalrepresentatives-patient').click();" );
    }

    @Then ( "Representative is presented with a patient in the list of patients he represents" )
    public void viewRepresentative2 () {
        final WebElement rep = driver.findElement( By.name( "idCell" ) );
        assertEquals( rep.getAttribute( "value" ), "patient" );
    }

}
