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
     * Tests the getByID method in LOINCCode.
     */
    @Test
    public void testGetByID () {
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
        LOINCCode retrieved = LOINCCode.getById( toRetrieveID );
        assertNotNull( retrieved );
        assertTrue( "12345-6".equals( retrieved.getCode() ) );

        // Remove from the database.
        testCode.delete();
        retrieved = LOINCCode.getById( toRetrieveID );
        assertNull( retrieved );
    }
}
