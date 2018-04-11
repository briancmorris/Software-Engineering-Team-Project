package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;

/**
 * Tests the LOINCCode and LOINCCodeForm classes for completion.
 *
 * @author Brian Morris
 *
 */
public class LOINCCodeTest {

    /**
     * Tests the LOINCCode constructor that uses a LOINCCodeForm.
     */
    @Test
    public void testLOINCCodeConstructor () {
        // Test form.
        final LOINCCodeForm testForm = new LOINCCodeForm();
        testForm.setId( new Long( 5 ) );
        testForm.setCode( "12345-6" );
        testForm.setLongCommonName( "This is a test lcn." );
        testForm.setComponent( "This is a test component." );
        testForm.setProp( "This is a test prop." );
        testForm.setSpecialUsage( "This is a test su." );

        // Test code.
        final LOINCCode test = new LOINCCode( testForm );

        // Test assertions.
        assertEquals( testForm.getId().longValue(), test.getId().longValue() );
        assertTrue( testForm.getCode().equals( test.getCode() ) );
        assertTrue( testForm.getLongCommonName().equals( test.getLongCommonName() ) );
        assertTrue( testForm.getComponent().equals( test.getComponent() ) );
        assertTrue( testForm.getProp().equals( test.getProp() ) );
        assertTrue( testForm.getSpecialUsage().equals( test.getSpecialUsage() ) );
    }

    /**
     * Tests the LOINCCodeForm constructor that uses a LOINCCode.
     */
    @Test
    public void testLOINCCodeFormConstructor () {
        // Test code.
        final LOINCCode testCode = new LOINCCode();
        testCode.setId( new Long( 5 ) );
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );

        // Test form.
        final LOINCCodeForm test = new LOINCCodeForm( testCode );

        // Test assertions.
        assertEquals( testCode.getId().longValue(), test.getId().longValue() );
        assertTrue( testCode.getCode().equals( test.getCode() ) );
        assertTrue( testCode.getLongCommonName().equals( test.getLongCommonName() ) );
        assertTrue( testCode.getComponent().equals( test.getComponent() ) );
        assertTrue( testCode.getProp().equals( test.getProp() ) );
        assertTrue( testCode.getSpecialUsage().equals( test.getSpecialUsage() ) );
    }

    /**
     * Tests the ID getter and setter methods.
     */
    @Test
    public void testId () {

        // Test code and form.
        final LOINCCode testCode = new LOINCCode();
        final LOINCCodeForm testForm = new LOINCCodeForm();
        final Long testId = new Long( 5 );

        // Assert the the initial ID is null.
        assertNull( testCode.getId() );
        assertNull( testForm.getId() );
        testCode.setId( testId );
        testForm.setId( testId );
        // Assert that set/get work as intended.
        assertEquals( testId.longValue(), testCode.getId().longValue() );
        assertEquals( testId.longValue(), testForm.getId().longValue() );

    }

    /**
     * Tests the code getter and setter methods.
     */
    @Test
    public void testCode () {

        // Test code and form.
        final LOINCCode testCode = new LOINCCode();
        final LOINCCodeForm testForm = new LOINCCodeForm();
        final String testCodeStr = "12345-6";

        // Assert the the initial code is null.
        assertNull( testCode.getCode() );
        assertNull( testForm.getCode() );
        // Assert that set/get work as intended.
        testCode.setCode( testCodeStr );
        testForm.setCode( testCodeStr );
        assertTrue( testCodeStr.equals( testCode.getCode() ) );
        assertTrue( testCodeStr.equalsIgnoreCase( testForm.getCode() ) );

    }

    /**
     * Tests the long common name getter and setter methods.
     */
    @Test
    public void testLongCommonName () {

        // Test code and form.
        final LOINCCode testCode = new LOINCCode();
        final LOINCCodeForm testForm = new LOINCCodeForm();
        final String testLongCommonName = "This is a test.";

        // Assert the the initial long common name is null.
        assertNull( testCode.getLongCommonName() );
        assertNull( testForm.getLongCommonName() );
        // Assert that set/get work as intended.
        testCode.setLongCommonName( testLongCommonName );
        testForm.setLongCommonName( testLongCommonName );
        assertTrue( testLongCommonName.equals( testCode.getLongCommonName() ) );
        assertTrue( testLongCommonName.equals( testForm.getLongCommonName() ) );

    }

    /**
     * Tests the special usage getter and setter methods.
     */
    @Test
    public void testSpecialUsage () {

        // Test code and form.
        final LOINCCode testCode = new LOINCCode();
        final LOINCCodeForm testForm = new LOINCCodeForm();
        final String testSpecialUsage = "This is a test.";

        // Assert the the initial long common name is null.
        assertNull( testCode.getSpecialUsage() );
        assertNull( testForm.getSpecialUsage() );
        // Assert that set/get work as intended.
        testCode.setSpecialUsage( testSpecialUsage );
        testForm.setSpecialUsage( testSpecialUsage );
        assertTrue( testSpecialUsage.equals( testCode.getSpecialUsage() ) );
        assertTrue( testSpecialUsage.equals( testForm.getSpecialUsage() ) );

    }

    /**
     * Tests the component getter and setter methods.
     */
    @Test
    public void testComponent () {

        // Test code and form.
        final LOINCCode testCode = new LOINCCode();
        final LOINCCodeForm testForm = new LOINCCodeForm();
        final String testComponent = "This is a test.";

        // Assert the the initial long common name is null.
        assertNull( testCode.getComponent() );
        assertNull( testForm.getComponent() );
        // Assert that set/get work as intended.
        testCode.setComponent( testComponent );
        testForm.setComponent( testComponent );
        assertTrue( testComponent.equals( testCode.getComponent() ) );
        assertTrue( testComponent.equals( testForm.getComponent() ) );

    }

    /**
     * Tests the prop getter and setter methods.
     */
    @Test
    public void testProp () {

        // Test code and form.
        final LOINCCode testCode = new LOINCCode();
        final LOINCCodeForm testForm = new LOINCCodeForm();
        final String testProp = "This is a test.";

        // Assert the the initial long common name is null.
        assertNull( testCode.getProp() );
        assertNull( testForm.getProp() );
        // Assert that set/get work as intended.
        testCode.setProp( testProp );
        testForm.setProp( testProp );
        assertTrue( testProp.equals( testCode.getProp() ) );
        assertTrue( testProp.equals( testForm.getProp() ) );

    }

    /**
     * Tests the getById method in LOINCCode.
     */
    @Test
    public void testGetById () {
        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();

        // Roundabout way to get the auto-generated ID.
        final List<LOINCCode> list = LOINCCode.getAll();
        assertTrue( list.size() >= 1 );
        Long toRetrieveID = null;
        LOINCCode code = null;
        for ( int i = 0; i < list.size(); i++ ) {
            code = list.get( i );
            if ( testCode.getLongCommonName().equals( code.getLongCommonName() ) ) {
                toRetrieveID = code.getId();
                break;
            }
        }
        assertNotNull( toRetrieveID );

        // Retrieve the code by ID.
        final LOINCCode retrieved = LOINCCode.getById( toRetrieveID );
        assertNotNull( retrieved );
        assertTrue( "12345-6".equals( retrieved.getCode() ) );

        // Remove from the database.
        testCode.delete();
        assertNull( LOINCCode.getById( toRetrieveID ) );
    }

    /**
     * Tests the getAll method in LOINCCode.
     */
    @Test
    public void testGetAll () {
        // Test codes.
        final LOINCCode testCode1 = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode1.setCode( "12345-6" );
        testCode1.setLongCommonName( "This is a test lcn 1." );
        testCode1.setComponent( "This is a test component 1." );
        testCode1.setProp( "This is a test prop 1." );
        testCode1.setSpecialUsage( "This is a test su 1." );
        testCode1.save();

        final LOINCCode testCode2 = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode2.setCode( "65432-1" );
        testCode2.setLongCommonName( "This is a test lcn 2." );
        testCode2.setComponent( "This is a test component 2." );
        testCode2.setProp( "This is a test prop 2." );
        testCode2.setSpecialUsage( "This is a test su 2." );
        testCode2.save();

        // Codes list.
        final List<LOINCCode> testList = LOINCCode.getAll();

        // Must be at least 2 for the 2 test codes.
        assertTrue( testList.size() >= 2 );

        // Find both of the codes in the list.
        LOINCCode retrievedCode1 = null;
        LOINCCode retrievedCode2 = null;
        for ( final LOINCCode code : testList ) {
            if ( testCode1.getLongCommonName().equals( code.getLongCommonName() ) ) {
                retrievedCode1 = code;
            }
            else if ( testCode2.getLongCommonName().equals( code.getLongCommonName() ) ) {
                retrievedCode2 = code;
            }
        }
        assertNotNull( retrievedCode1 );
        assertNotNull( retrievedCode2 );

        // Verified by above, but double check codes here.
        assertTrue( testCode1.getCode().equals( retrievedCode1.getCode() ) );
        assertTrue( testCode2.getCode().equals( retrievedCode2.getCode() ) );

        // Delete from DB and verify deletion.
        final Long retrievedCode1ID = retrievedCode1.getId();
        final Long retrievedCode2ID = retrievedCode2.getId();
        testCode1.delete();
        testCode2.delete();
        assertNull( LOINCCode.getById( retrievedCode1ID ) );
        assertNull( LOINCCode.getById( retrievedCode2ID ) );
    }

    /**
     * Tests the getByCode method in LOINCCode.
     */
    @Test
    public void testGetByCode () {
        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();

        // Get the code from the DB.
        final LOINCCode retrieved = LOINCCode.getByCode( "12345-6" );
        assertNotNull( retrieved );
        assertTrue( "This is a test lcn.".equals( retrieved.getLongCommonName() ) );

        // Remove the code from the DB and verify deletion.
        final Long retrievedID = retrieved.getId();
        testCode.delete();
        assertNull( LOINCCode.getById( retrievedID ) );

        // Check for a code not stored in DB.
        assertNull( LOINCCode.getByCode( "65432-1" ) );
    }
}
