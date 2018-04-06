package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;

/**
 * Tests the LOINCCode class for completion.
 *
 * @author Brian Morris
 *
 */
public class LOINCCodeTest {

    /**
     * Tests the constructor that uses a LOINCCodeForm.
     */
    @Test
    public void testFormConstructor () {
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
        assertEquals( 5, test.getId().longValue() );
        assertTrue( "12345-6".equals( test.getCode() ) );
        assertTrue( "This is a test lcn.".equals( test.getLongCommonName() ) );
        assertTrue( "This is a test component.".equals( test.getComponent() ) );
        assertTrue( "This is a test prop.".equals( test.getProp() ) );
        assertTrue( "This is a test su.".equals( test.getSpecialUsage() ) );
    }

    /**
     * Tests the ID getter and setter methods.
     */
    @Test
    public void testId () {

        // Test code.
        final LOINCCode test = new LOINCCode();

        // Assert the the initial ID is null.
        assertNull( test.getId() );
        test.setId( new Long( 5 ) );
        // Assert that set/get work as intended.
        assertEquals( 5, test.getId().longValue() );

    }

    /**
     * Tests the code getter and setter methods.
     */
    @Test
    public void testCode () {

        // Test code.
        final LOINCCode test = new LOINCCode();

        // Assert the the initial code is null.
        assertNull( test.getCode() );
        // Assert that set/get work as intended.
        test.setCode( "12345-6" );
        assertTrue( "12345-6".equals( test.getCode() ) );

    }

    /**
     * Tests the long common name getter and setter methods.
     */
    @Test
    public void testLongCommonName () {

        // Test code.
        final LOINCCode test = new LOINCCode();

        // Assert the the initial long common name is null.
        assertNull( test.getLongCommonName() );
        // Assert that set/get work as intended.
        test.setLongCommonName( "This is a test." );
        assertTrue( "This is a test.".equals( test.getLongCommonName() ) );

    }

    /**
     * Tests the special usage getter and setter methods.
     */
    @Test
    public void testSpecialUsage () {

        // Test code.
        final LOINCCode test = new LOINCCode();

        // Assert the the initial special usage is null.
        assertNull( test.getSpecialUsage() );
        // Assert that set/get work as intended.
        test.setSpecialUsage( "This is a test." );
        assertTrue( "This is a test.".equals( test.getSpecialUsage() ) );

    }

    /**
     * Tests the component getter and setter methods.
     */
    @Test
    public void testComponent () {

        // Test code.
        final LOINCCode test = new LOINCCode();

        // Assert the the initial component is null.
        assertNull( test.getComponent() );
        // Assert that set/get work as intended.
        test.setComponent( "This is a test." );
        assertTrue( "This is a test.".equals( test.getComponent() ) );

    }

    /**
     * Tests the prop getter and setter methods.
     */
    @Test
    public void testProp () {

        // Test code.
        final LOINCCode test = new LOINCCode();

        // Assert the the initial prop is null.
        assertNull( test.getProp() );
        // Assert that set/get work as intended.
        test.setProp( "This is a test." );
        assertTrue( "This is a test.".equals( test.getProp() ) );

    }
}
