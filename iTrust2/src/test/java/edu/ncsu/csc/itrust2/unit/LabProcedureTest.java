package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PriorityLevel;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;
import edu.ncsu.csc.itrust2.models.persistent.LabProcedure;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests LabProcedure and LabProcedure form for correct and complete
 * implementation.
 *
 * @author Brian Morris
 *
 */
public class LabProcedureTest {

    /**
     * Tests the constructor of LabProcedure that requires a LabProcedureForm.
     * Simultaneously tests the getters of LabProcedure and setters of
     * LabProcedureForm.
     *
     * @throws ParseException
     *             If there is an error parsing the date.
     */
    @SuppressWarnings ( "static-access" )
    @Test
    public void testLabProcedureConstructor () throws ParseException {
        // Test form.
        final LabProcedureForm form = new LabProcedureForm();

        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();

        form.setID( new Long( 5 ) );
        form.setCode( "12345-6" );
        form.setPriorityLevel( "2" );
        form.setDate( "03/11/2018" );
        form.setTime( "12:30 PM" );
        form.setComments( "test comments" );
        form.setCompletionStatus( "Completed" );
        form.setLabTech( "lt" );
        form.setOfficeVisitId( new Long( 5 ) );

        // Test procedure.
        final LabProcedure procedure = new LabProcedure( form );

        // Assert equals.
        assertTrue( new Long( 5 ).equals( procedure.getId() ) );
        assertTrue( "12345-6".equals( procedure.getCode().getCode() ) );
        testCode.delete();
        assertTrue( PriorityLevel.TWO.equals( procedure.getPriorityLevel() ) );
        final Calendar testCal = Calendar.getInstance();
        testCal.set( 2018, 3, 11 );
        // Test individual attributes, since hour/minute timestamp will differ.
        assertEquals( testCal.DAY_OF_MONTH, procedure.getDate().DAY_OF_MONTH );
        assertEquals( testCal.MONTH, procedure.getDate().MONTH );
        assertEquals( testCal.YEAR, procedure.getDate().YEAR );
        assertTrue( "test comments".equals( procedure.getComments() ) );
        assertTrue( CompletionStatus.COMPLETED.equals( procedure.getCompletionStatus() ) );
        assertTrue( User.getByName( "lt" ).equals( procedure.getLabTech() ) );
        assertNull( procedure.getOfficeVisit() );
    }

    /**
     * Tests the constructor of LabProcedureForm that requires a LabProcedure.
     * Simultaneously tests the getters of LabProcedureForm and the setters of
     * LabProcedure.
     *
     * @throws ParseException
     *             If there is an error parsing the date.
     */
    @Test
    public void testLabProcedureFormConstructor () throws ParseException {
        // Test procedure.
        final LabProcedure procedure = new LabProcedure();
        procedure.setId( new Long( 5 ) );

        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();
        procedure.setCode( testCode );

        procedure.setLabTech( User.getByName( "lt" ) );
        procedure.setOfficeVisit( new OfficeVisit() );
        procedure.setComments( "test comments" );
        procedure.setCompletionStatus( CompletionStatus.IN_PROGRESS );
        procedure.setPriorityLevel( PriorityLevel.TWO );

        // Test Calendar.
        final Calendar testCal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm aaa", Locale.ENGLISH );
        final Date parsedDate = sdf.parse( "03/11/2018 12:30 PM" );
        testCal.setTime( parsedDate );
        procedure.setDate( testCal );

        // Test form.
        final LabProcedureForm form = new LabProcedureForm( procedure );

        // Assert Equals.
        assertTrue( new Long( 5 ).equals( form.getID() ) );
        assertTrue( "12345-6".equals( form.getCode() ) );
        assertTrue( "test comments".equals( form.getComments() ) );
        assertTrue( "In-progress".equals( form.getCompletionStatus() ) );
        assertTrue( "2".equals( form.getPriorityLevel() ) );
        assertTrue( "lt".equals( form.getLabTech() ) );
        System.out.println( form.getDate() );
        assertTrue( "03/11/2018".equals( form.getDate() ) );
        assertTrue( "12:30 PM".equals( form.getTime() ) );
        assertNull( form.getOfficeVisitId() );

        testCode.delete();

    }

    /**
     * Tests the getById method in LabProcedure. Simultaneously tests the getAll
     * method for LabProcedure.
     *
     * @throws ParseException
     *             if there is an error when parsing the date.
     */
    @Test
    public void testGetById () throws ParseException {
        // Test procedure.
        final LabProcedure procedure = new LabProcedure();
        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();

        procedure.setCode( testCode );
        procedure.setLabTech( User.getByName( "lt" ) );
        procedure.setComments( "test comments" );
        procedure.setCompletionStatus( CompletionStatus.IN_PROGRESS );
        procedure.setPriorityLevel( PriorityLevel.TWO );

        // Test Calendar.
        final Calendar testCal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm aaa", Locale.ENGLISH );
        final Date parsedDate = sdf.parse( "03/11/2018 12:30 PM" );
        testCal.setTime( parsedDate );
        procedure.setDate( testCal );

        // Test OfficeVisit setup.
        final OfficeVisit visit = new OfficeVisit();

        final Hospital hosp = new Hospital( "Brian's Test Clinic", "5545 NCSU Blvd", "27603", "NC" );
        hosp.save();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setDiastolic( 100 );
        bhm.setHcp( User.getByName( "hcp" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setHeight( 75f );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.GENERAL_CHECKUP );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "hcp" ) );
        visit.setDate( Calendar.getInstance() );
        visit.save();

        procedure.setOfficeVisit( visit );
        procedure.save();

        final List<LabProcedure> list = LabProcedure.getAll();
        LabProcedure retrieved = null;
        assertTrue( list.size() >= 1 );
        for ( int i = 0; i < list.size(); i++ ) {
            if ( testCode.getCode().equals( list.get( i ).getCode().getCode() ) ) {
                retrieved = list.get( i );
                break;
            }
        }
        assertNotNull( retrieved );

        final Long id = retrieved.getId();
        final LabProcedure toCompare = LabProcedure.getById( id );
        assertNotNull( toCompare );

        assertTrue( retrieved.getComments().equals( toCompare.getComments() ) );

        procedure.delete();
        testCode.delete();
        visit.delete();
        // hosp.delete();
        bhm.delete();

        final LabProcedure bad = LabProcedure.getById( new Long( -55 ) );
        assertNull( bad );

    }

    /**
     * Tests the delete method of LabProcedure.
     *
     * @throws ParseException
     *             for invalid date
     */
    @Test
    public void testDelete () throws ParseException {
        // Test procedure.
        final LabProcedure procedure = new LabProcedure();
        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();

        procedure.setCode( testCode );
        procedure.setLabTech( User.getByName( "lt" ) );
        procedure.setComments( "test comments" );
        procedure.setCompletionStatus( CompletionStatus.IN_PROGRESS );
        procedure.setPriorityLevel( PriorityLevel.TWO );

        // Test Calendar.
        final Calendar testCal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm aaa", Locale.ENGLISH );
        final Date parsedDate = sdf.parse( "03/11/2018 12:30 PM" );
        testCal.setTime( parsedDate );
        procedure.setDate( testCal );

        // Test OfficeVisit setup.
        final OfficeVisit visit = new OfficeVisit();

        final Hospital hosp = new Hospital( "Brian's Test Clinic", "5545 NCSU Blvd", "27603", "NC" );
        hosp.save();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setDiastolic( 100 );
        bhm.setHcp( User.getByName( "hcp" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setHeight( 75f );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.GENERAL_CHECKUP );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "hcp" ) );
        visit.setDate( Calendar.getInstance() );
        visit.save();

        procedure.setOfficeVisit( visit );
        procedure.save();

        final List<LabProcedure> list = LabProcedure.getAll();
        LabProcedure retrieved = null;
        assertTrue( list.size() >= 1 );
        for ( int i = 0; i < list.size(); i++ ) {
            if ( testCode.getCode().equals( list.get( i ).getCode().getCode() ) ) {
                retrieved = list.get( i );
                break;
            }
        }
        assertNotNull( retrieved );

        retrieved.delete();
        final List<LabProcedure> newList = LabProcedure.getAll();
        assertFalse( newList.contains( retrieved ) );
    }

    /**
     * Tests the getForLT method of LabProcedure.
     *
     * @throws ParseException
     *             for invalid date
     */
    @Test
    public void testGetForLT () throws ParseException {
        // Test procedure.
        final LabProcedure procedure = new LabProcedure();
        // Test code.
        final LOINCCode testCode = new LOINCCode();
        // NOTE: DO NOT set ID for an auto-generated value.
        testCode.setCode( "12345-6" );
        testCode.setLongCommonName( "This is a test lcn." );
        testCode.setComponent( "This is a test component." );
        testCode.setProp( "This is a test prop." );
        testCode.setSpecialUsage( "This is a test su." );
        testCode.save();

        procedure.setCode( testCode );
        procedure.setLabTech( User.getByName( "lt" ) );
        procedure.setComments( "test comments" );
        procedure.setCompletionStatus( CompletionStatus.IN_PROGRESS );
        procedure.setPriorityLevel( PriorityLevel.TWO );

        // Test Calendar.
        final Calendar testCal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm aaa", Locale.ENGLISH );
        final Date parsedDate = sdf.parse( "03/11/2018 12:30 PM" );
        testCal.setTime( parsedDate );
        procedure.setDate( testCal );

        // Test OfficeVisit setup.
        final OfficeVisit visit = new OfficeVisit();

        final Hospital hosp = new Hospital( "Brian's Test Clinic", "5545 NCSU Blvd", "27603", "NC" );
        hosp.save();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setDiastolic( 100 );
        bhm.setHcp( User.getByName( "hcp" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setHeight( 75f );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.GENERAL_CHECKUP );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "hcp" ) );
        visit.setDate( Calendar.getInstance() );
        visit.save();

        procedure.setOfficeVisit( visit );
        procedure.save();

        final List<LabProcedure> list = LabProcedure.getAll();
        LabProcedure retrieved = null;
        assertTrue( list.size() >= 1 );
        for ( int i = 0; i < list.size(); i++ ) {
            if ( testCode.getCode().equals( list.get( i ).getCode().getCode() ) ) {
                retrieved = list.get( i );
                break;
            }
        }
        assertNotNull( retrieved );

        final List<LabProcedure> ltList = LabProcedure.getForLT( User.getByName( "lt" ) );
        assertTrue( ltList.size() >= 1 );

        retrieved.delete();
    }

    /**
     * Tests the getStatus method of LabProcedure.
     */
    @Test
    public void testGetStatus () {
        final List<CompletionStatus> list = LabProcedure.getStatus();

        assertEquals( 4, list.size() );
    }

}
