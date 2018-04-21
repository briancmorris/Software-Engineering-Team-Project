package edu.ncsu.csc.itrust2.cucumber;

// Author Garrett Watts
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
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
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

public class NewRolesStepDefs {

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

    @Given ( "The ER role user is a registered user of the iTrust2 Medical Records system" )
    public void initER () {
        final User er = new User( "er", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_ER,
                1 );
        er.save();
    }

    @Given ( "A Lab Tech role user is a registered user of the iTrust2 Medical Records system" )
    public void initLT () {
        final User lt = new User( "lt", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_LT,
                1 );
        lt.save();
    }

    @When ( "I enter username: (.+) and password: (.+) to log in" )
    public void attemptLogin ( final String username, final String password ) {
        driver.get( baseUrl );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "username" ) ) );

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( username );
        final WebElement passwordField = driver.findElement( By.name( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( password );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

    }

    @Then ( "I am greeted as an Emergency Responder user" )
    public void checkLandingER () {
        // Check Greeting
        assertTrue( driver.getPageSource().contains( "Emergency Responder" ) );
        // Check that the correct dropdown menu is displayed
        assertTrue( driver.getPageSource().contains( "View Emergency Health Records" ) );
    }

    @Then ( "I am greeted as a Lab Tech user" )
    public void checkLandingLT () {
        // Check Greeting
        assertTrue( driver.getPageSource().contains( "Lab Tech" ) );
        // Check that the correct dropdown menu is displayed
        assertTrue( driver.getPageSource().contains( "Lab Procedures" ) );
    }

}
