/**
 *
 */
package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Step Definitions for Creating Lab Procedures
 *
 * @author Timothy Figgins
 *
 */
public class LabProceduresStepDefs {

    private WebDriver    driver;
    private final String baseUrl   = "http://localhost:8080/iTrust2";

    WebDriverWait        wait;

    String               currentCode;
    private int          tableSize = 0;

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

    @After
    public void tearDown () {
        final WebElement codeTable = driver.findElement( By.className( "table" ) );
        final List<WebElement> lCodes = codeTable.findElements( By.xpath( "//tr[@name='codeRow']" ) );
        for ( final WebElement we : lCodes ) {
            we.findElement( By.xpath( "//input[@value='Remove']" ) ).click();
        }
        driver.quit();
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
                tableSize++;
            }
        }
        assertTrue( check );
    }

    @And ( "I can also delete the code" )
    public void deleteCode () {
        final WebElement codeTable = driver.findElement( By.className( "table" ) );
        final List<WebElement> lCodes = codeTable.findElements( By.xpath( "//tr[@name='codeRow']" ) );
        for ( final WebElement we : lCodes ) {
            if ( currentCode.equals( we.findElement( By.name( "codeCell" ) ).getText() ) ) {
                we.findElement( By.xpath( "//input[@value='Remove']" ) ).click();
            }
        }
    }

    @Then ( "see that the code was successfully removed" )
    public void confirmDelete () throws InterruptedException {
        wait.until( ExpectedConditions.invisibilityOfElementLocated( ( ( By.xpath( "//tr[@name='codeRow']" ) ) ) ) );
        int currentTableSize = 0;
        final WebElement codeTable = driver.findElement( By.className( "table" ) );
        final List<WebElement> lCodes = codeTable.findElements( By.xpath( "//tr[@name='codeRow']" ) );
        for ( @SuppressWarnings ( "unused" )
        final WebElement we : lCodes ) {
            currentTableSize++;
        }

        assertEquals( currentTableSize, tableSize - 1 );
    }
}
