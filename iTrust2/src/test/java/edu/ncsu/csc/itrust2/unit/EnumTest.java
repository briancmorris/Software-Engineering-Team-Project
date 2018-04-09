package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PriorityLevel;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;

/**
 * Tests enumeration classes functionality.
 *
 * @author Brian Morris
 *
 */
public class EnumTest {

    /**
     * Tests TransactionType enum.
     */
    @Test
    public void testTransactionType () {
        // Get a transaction type.
        final TransactionType test = TransactionType.VIEW_USER_LOG;
        // Check its code.
        final int testCode = test.getCode();
        assertEquals( testCode, 1301 );
        // Check its description.
        final String testDescription = test.getDescription();
        assertTrue( testDescription.equals( "Log events viewed" ) );
    }

    /**
     * Tests AppointmentType enum.
     */
    @Test
    public void testAppointmentType () {
        // Get the appointment type.
        final AppointmentType test = AppointmentType.GENERAL_CHECKUP;
        // Get the code.
        final int testCode = test.getCode();
        assertEquals( testCode, 1 );
    }

    /**
     * Tests BloodType enum.
     */
    @Test
    public void testBloodType () {
        final BloodType test = BloodType.parse( "test" );
        final String testString = test.getName();
        assertTrue( testString.equals( "Not Specified" ) );
    }

    /**
     * Tests Ethnicity enum.
     */
    @Test
    public void testEthnicity () {
        final Ethnicity test = Ethnicity.parse( "test" );
        final String testString = test.getName();
        assertTrue( testString.equals( "Not Specified" ) );
    }

    /**
     * Tests Gender enum.
     */
    @Test
    public void testGender () {
        final Gender test = Gender.parse( "test" );
        final String testString = test.getName();
        assertTrue( testString.equals( "Not Specified" ) );
    }

    /**
     * Tests PatientSmokingStatus enum.
     */
    @Test
    public void testPatientSmokingStatus () {
        final PatientSmokingStatus test = PatientSmokingStatus.parseValue( 99 );
        final int testCode = test.getCode();
        assertEquals( testCode, 0 );
    }

    /**
     * Tests Role enum.
     */
    @Test
    public void testRole () {
        final Role test = Role.ROLE_ADMIN;
        final int testCode = test.getCode();
        assertEquals( testCode, 3 );
        final String testLanding = test.getLanding();
        assertTrue( testLanding.equals( "admin/index" ) );
    }

    /**
     * Tests State enum.
     */
    @Test
    public void testState () {
        final State test = State.parse( "test" );
        final String testString = test.getName();
        assertTrue( testString.equals( "North Carolina" ) );
    }

    /**
     * Tests Status enum.
     */
    @Test
    public void testStatus () {
        final Status test = Status.APPROVED;
        final int testCode = test.getCode();
        assertEquals( testCode, 3 );
    }

    /**
     * Tests PriorityLevel enum.
     */
    @Test
    public void testPriorityLevel () {
        // Test get method.
        final PriorityLevel test = PriorityLevel.ONE;
        final int testCode = test.getCode();
        assertEquals( testCode, 1 );

        // Test parse method.
        assertTrue( PriorityLevel.THREE.equals( PriorityLevel.parse( "3" ) ) );
        assertTrue( PriorityLevel.INVALID.equals( PriorityLevel.parse( "7" ) ) );
        assertTrue( PriorityLevel.INVALID.equals( PriorityLevel.parse( "-1" ) ) );
    }

    /**
     * Tests the CompletionStatus enum.
     */
    @Test
    public void testCompletionStatus () {
        // Test the get method.
        final CompletionStatus test = CompletionStatus.COMPLETED;
        final String testString = test.getName();
        assertTrue( "Completed".equals( testString ) );

        // Test parse method.
        assertTrue( CompletionStatus.IN_PROGRESS.equals( CompletionStatus.parse( "In-progress" ) ) );
        assertTrue( CompletionStatus.NOT_SPECIFIED.equals( CompletionStatus.parse( "12345" ) ) );
    }
}
