/**
 *
 */
package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Step Definitions for Creating Lab Procedures
 *
 * @author Timothy Figgins
 *
 */
public class LabProceduresStepDefs {

    private WebDriver    driver;
    private final String baseUrl      = "http://localhost:8080/iTrust2";
    private final String hospitalName = "Office Visit Hospital" + ( new Random() ).nextInt();

    WebDriverWait        wait;

    String               currentCode;

    @Before
    public void setup () {

        ChromeDriverManager.getInstance().setup();
        final ChromeOptions options = new ChromeOptions();
        options.addArguments( "headless" );
        options.addArguments( "window-size=1200x600" );
        options.addArguments( "blink-settings=imagesEnabled=false" );
        driver = new ChromeDriver( options );

        wait = new WebDriverWait( driver, 5 );

    }

    @Given ( "I am logged in as an admin" )
    public void adminLogin () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "I navigate to view the Manage LOINC code page" )
    public void moveToLOINCPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('manageLOINCCodes').click();" );

    }

    @And ( "I add a new code with (.+), (.+), (.+), (.+), and (.+)" )
    public void addCode ( final String code, final String longName, final String special, final String component,
            final String property ) {

        currentCode = code;
        final WebElement codeForm = driver.findElement( By.name( "code" ) );
        codeForm.clear();
        codeForm.sendKeys( code );
        final WebElement longNameForm = driver.findElement( By.name( "longCommonName" ) );
        longNameForm.clear();
        longNameForm.sendKeys( longName );
        final WebElement specialForm = driver.findElement( By.name( "specialUsage" ) );
        specialForm.clear();
        specialForm.sendKeys( special );
        final WebElement componentForm = wait.until( ExpectedConditions
                .presenceOfElementLocated( ( By.xpath( "//textarea[@ng-model='code.component']" ) ) ) );
        componentForm.clear();
        componentForm.sendKeys( component );
        final WebElement propertyForm = wait.until(
                ExpectedConditions.presenceOfElementLocated( ( By.xpath( "//textarea[@ng-model='code.prop']" ) ) ) );
        propertyForm.clear();
        propertyForm.sendKeys( property );

        driver.findElement( By.name( "submit" ) ).click();

    }

    @Then ( "I can see the code added to the page" )
    public void confirmAdd () throws InterruptedException {
        boolean check = false;
        wait.until( ExpectedConditions.presenceOfElementLocated( ( By.xpath( "//tr[@name='codeRow']" ) ) ) );
        final WebElement codeTable = driver.findElement( By.className( "table" ) );
        final List<WebElement> lCodes = codeTable.findElements( By.xpath( "//tr[@name='codeRow']" ) );
        for ( final WebElement we : lCodes ) {
            if ( currentCode.equals( we.findElement( By.name( "codeCell" ) ).getText() ) ) {
                check = true;
                break;
            }
        }
        assertTrue( check );

        // logout as admin
        driver.findElement( By.id( "logout" ) ).click();

    }

    @Given ( "The required hospital and patient exist for me" )
    public void personnelExists () throws Exception {
        OfficeVisit.deleteAll();
        // All tests can safely assume the existence of the 'hcp', 'admin', and
        // 'patient' users

        /* Make sure we create a Hospital and Patient record */

        final Hospital hospital = new Hospital( hospitalName, "Bialystok", "10101", State.NJ.toString() );
        hospital.save();

        /* Create patient record */

        final Patient patient = new Patient();
        patient.setSelf( User.getByName( "patient" ) );
        patient.setFirstName( "Karl" );
        patient.setLastName( "Liebknecht" );
        patient.setEmail( "karl_liebknecht@mail.de" );
        patient.setAddress1( "Karl Liebknecht Haus. Alexanderplatz" );
        patient.setCity( "Berlin" );
        patient.setState( State.DE );
        patient.setZip( "91505" );
        patient.setPhone( "123-456-7890" );
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/DD/YYYY", Locale.ENGLISH );

        final Calendar time = Calendar.getInstance();
        time.setTime( sdf.parse( "08/13/1871" ) );

        patient.setDateOfBirth( time );

        patient.save();

    }

    @When ( "I am logged in as an HCP" )
    public void loginAsHCP () {
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

    @When ( "I go to the Office Visit page to create a new lab" )
    public void navigateDocumentOV () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );
    }

    @When ( "^I fill in information with the LabProcedure$" )
    public void documentOV () {
        wait.until( ExpectedConditions.and( ExpectedConditions.visibilityOfElementLocated( By.name( "type" ) ),
                ExpectedConditions.visibilityOfElementLocated( By.name( "name" ) ),
                ExpectedConditions.visibilityOfElementLocated( By.name( "hospital" ) ) ) );

        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( "Patient appears pretty much alive" );

        final WebElement patient = driver.findElement( By.name( "name" ) );
        patient.click();
        final WebElement type = driver.findElement( By.name( "type" ) );
        type.click();

        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        final WebElement date = driver.findElement( By.name( "date" ) );
        date.clear();
        date.sendKeys( "12/19/2027" );

        final WebElement time = driver.findElement( By.name( "time" ) );
        time.clear();
        time.sendKeys( "9:30 AM" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "height" ) ) );
        final WebElement heightElement = driver.findElement( By.name( "height" ) );
        heightElement.clear();
        heightElement.sendKeys( "120" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "weight" ) ) );
        final WebElement weightElement = driver.findElement( By.name( "weight" ) );
        weightElement.clear();
        weightElement.sendKeys( "120" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "systolic" ) ) );
        final WebElement systolicElement = driver.findElement( By.name( "systolic" ) );
        systolicElement.clear();
        systolicElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "diastolic" ) ) );
        final WebElement diastolicElement = driver.findElement( By.name( "diastolic" ) );
        diastolicElement.clear();
        diastolicElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "hdl" ) ) );
        final WebElement hdlElement = driver.findElement( By.name( "hdl" ) );
        hdlElement.clear();
        hdlElement.sendKeys( "90" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "ldl" ) ) );
        final WebElement ldlElement = driver.findElement( By.name( "ldl" ) );
        ldlElement.clear();
        ldlElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "tri" ) ) );
        final WebElement triElement = driver.findElement( By.name( "tri" ) );
        triElement.clear();
        triElement.sendKeys( "100" );

        wait.until( ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) ) );
        final WebElement houseSmokeElement = driver.findElement(
                By.cssSelector( "input[value=\"" + HouseholdSmokingStatus.NONSMOKING.toString() + "\"]" ) );
        houseSmokeElement.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) ) );
        final WebElement patientSmokeElement = driver
                .findElement( By.cssSelector( "input[value=\"" + PatientSmokingStatus.NEVER.toString() + "\"]" ) );
        patientSmokeElement.click();

        // Adding the lab procedure
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//input[@ng-value='loinc.code']" ) ) );
        final WebElement loincCode = driver.findElement( By.xpath( "//input[@ng-value='loinc.code']" ) );
        loincCode.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( "//option[@value='1']" ) ) );
        final WebElement priorityLevel = driver.findElement( By.xpath( "//option[@value='1']" ) );
        priorityLevel.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "commentsEntry" ) ) );
        final WebElement labComments = driver.findElement( By.name( "commentsEntry" ) );
        labComments.clear();
        labComments.sendKeys( "testComments" );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "fillLabProcedures" ) ) );
        final WebElement labButton = driver.findElement( By.name( "fillLabProcedures" ) );
        labButton.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "submit" ) ) );
        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();

    }

    @Then ( "The office visit is created successfully with the added labProcedure" )
    public void documentedSuccessfully () {
        wait.until( ExpectedConditions.textToBePresentInElementLocated( By.name( "success" ),
                "Office visit created successfully" ) );
    }
}
