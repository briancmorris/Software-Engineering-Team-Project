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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import io.github.bonigarcia.wdm.ChromeDriverManager;

public class PersonalRepsStepDefs {

    private WebDriver    driver;
    private final String baseUrl = "http://localhost:8080/iTrust2";

    WebDriverWait        wait;

    @Before
    public void setup () {

        // driver = new HtmlUnitDriver( true );
        // wait = new WebDriverWait( driver, 5 );
        ChromeDriverManager.getInstance().setup();
        final ChromeOptions options = new ChromeOptions();
        options.addArguments( "window-size=1200x600" );
        options.addArguments( "blink-settings=imagesEnabled=false" );
        driver = new ChromeDriver( options );
    }

    @After
    public void tearDown () {
        driver.close();
    }

    @Given ( "A patient exists with name: (.+) and DOB: (.+)" )
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

    @When ( "I log into iTrust2 as sample Patient" )
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
        final WebElement firstName = driver.findElement( By.name( "rep" ) );
        firstName.clear();
        firstName.sendKeys( username );

        driver.findElement( By.name( "submit" ) ).click();
    }

    @Then ( "The representative: (.+) is successfully added" )
    public void viewRepresentative ( final String username ) throws InterruptedException {
        Thread.sleep( 2000 );
        final WebElement message = driver.findElement( By.id( "succP" ) );
        assertEquals( "Successfully added personal representative", message.getText() );

        final WebElement name = driver.findElement(
                By.xpath( "//*[@id=\"wrap\"]/div[1]/div/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[1]" ) );
        assertEquals( username, name.getText() );
    }

    // @When ( "Added representative logs into into iTrust2 as Patient" )
    // public void repLogin () {
    // driver.get( baseUrl );
    // setTextField( By.name( "username" ), "AliceThirteen" );
    // setTextField( By.name( "password" ), "123456" );
    // driver.findElement( By.className( "btn" ) ).click();
    // }
    //
    // @When ( "Representative navigates to the Edit Personal Representatives
    // page" )
    // public void viewAddedRepresentatives () {
    // ( (JavascriptExecutor) driver )
    // .executeScript(
    // "document.getElementById('editpersonalrepresentatives-patient').click();"
    // );
    // }
    //
    // @Then ( "Representative is presented with a patient in the list of
    // patients he represents" )
    // public void viewRepresentative2 () {
    // final WebElement rep = driver.findElement( By.name( "idCell" ) );
    // assertEquals( rep.getAttribute( "value" ), "patient" );
    // }

    // new scenario

    @Given ( "A rep exists with name: (.+) and DOB: (.+)" )
    public void repExists ( final String name, final String birthday ) throws ParseException {

        /* Create patient record */

        final Patient patient = new Patient();
        patient.setSelf( User.getByName( "BobTheFourYearOld" ) );

        patient.setFirstName( name.split( " " )[0] );
        patient.setLastName( name.split( " " )[1] );
        patient.setEmail( "email2@mail.com" );
        patient.setAddress1( "846 address place" );
        patient.setCity( "citytowntwo" );
        patient.setState( State.CA );
        patient.setZip( "91506" );
        patient.setPhone( "123-456-7891" );
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/DD/YYYY", Locale.ENGLISH );

        final Calendar time = Calendar.getInstance();
        time.setTime( sdf.parse( birthday ) );

        patient.setDateOfBirth( time );

        patient.save();

    }

    @When ( "I log into iTrust2 again as sample HCP" )
    public void hcpLogin () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "hcp" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "I view the HCP Edit Personal Representatives page" )
    public void editRepresentativesHCP () {
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('editpersonalrepresentatives-hcp').click();" );
    }

    @When ( "I view representatives for: (.+)" )
    public void viewPatientRepresentatives ( final String username ) {
        final WebElement firstName = driver.findElement( By.name( "patient" ) );
        firstName.clear();
        firstName.sendKeys( username );

        driver.findElement( By.name( "submit" ) ).click();
    }

    @Then ( "I am presented with (.+) in the list of representatives" )
    public void viewReps ( final String rep ) throws InterruptedException {
        Thread.sleep( 2000 );
        final WebElement name = driver.findElement(
                By.xpath( "//*[@id=\"wrap\"]/div[1]/div/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td" ) );
        assertEquals( rep, name.getText() );
    }

    @When ( "I add another representative: (.+)" )
    public void addRep ( final String rep ) throws InterruptedException {
        final WebElement firstName = driver.findElement( By.name( "rep" ) );
        firstName.clear();
        firstName.sendKeys( rep );

        driver.findElement( By.name( "submitRep" ) ).click();
    }

    @Then ( "I am presented with another (.+) added to the list of representatives" )
    public void viewRepsAgain ( final String rep ) throws InterruptedException {
        Thread.sleep( 2000 );
        final WebElement message = driver.findElement( By.id( "succP" ) );
        assertEquals( "Successfully added personal representative", message.getText() );
    }

    // new scenario

    @When ( "I log into iTrust2 again as sample Patient" )
    public void patientLogin2 () {
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

    @When ( "I view Personal Representatives page" )
    public void viewPersonalRepresentatives () {
        ( (JavascriptExecutor) driver )
                .executeScript( "document.getElementById('editpersonalrepresentatives-patient').click();" );
    }

    @When ( "I undeclare previously added patient to not be a representative" )
    public void undeclareRep () throws InterruptedException {
        Thread.sleep( 5000 );
        final WebElement button = driver.findElement(
                By.xpath( "//*[@id=\"wrap\"]/div[1]/div/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/input" ) );
        button.click();
        Thread.sleep( 5000 );
        final WebElement button2 = driver.findElement(
                By.xpath( "//*[@id=\"wrap\"]/div[1]/div/div[1]/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/input" ) );
        button2.click();
    }

    @Then ( "I am presented with an empty list of representatives" )
    public void checkReps () throws InterruptedException {
        Thread.sleep( 2000 );
        final WebElement message = driver.findElement( By.id( "messageView" ) );
        assertEquals( "NO PERSONAL REPRESENTATIVES", message.getText() );
    }

}
