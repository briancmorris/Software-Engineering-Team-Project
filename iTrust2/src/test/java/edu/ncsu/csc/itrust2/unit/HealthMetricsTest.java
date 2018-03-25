package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class HealthMetricsTest {

    @Test
    public void healthEqualsTest () {
        // BasicHealthMetrics fields
        final Float height = new Float( 10 );
        final Float weight = new Float( 10 );
        final Float headCircumference = new Float( 10 );
        final Integer systolic = new Integer( 10 );
        final Integer diastolic = new Integer( 10 );
        final Integer hdl = new Integer( 10 );
        final Integer ldl = new Integer( 10 );
        final Integer tri = new Integer( 100 );
        final HouseholdSmokingStatus houseSmokingStatus = HouseholdSmokingStatus.NONSMOKING;
        final PatientSmokingStatus patientSmokingStatus = PatientSmokingStatus.NEVER;
        final User hcp = new User( "anttihcp", "12345", Role.ROLE_HCP, new Integer( 1 ) );
        final User p = new User( "anttiu", "12345", Role.ROLE_PATIENT, new Integer( 1 ) );

        final BasicHealthMetrics metrics = new BasicHealthMetrics();
        metrics.setHeight( height );
        metrics.setWeight( weight );
        metrics.setHeadCircumference( headCircumference );
        metrics.setSystolic( systolic );
        metrics.setDiastolic( diastolic );
        metrics.setHdl( hdl );
        metrics.setLdl( ldl );
        metrics.setTri( tri );
        metrics.setHouseSmokingStatus( houseSmokingStatus );
        metrics.setPatientSmokingStatus( patientSmokingStatus );
        metrics.setHcp( hcp );
        metrics.setPatient( p );

        final Float height2 = new Float( 5 );
        final Float weight2 = new Float( 5 );
        final Float headCircumference2 = new Float( 5 );
        final Integer systolic2 = new Integer( 5 );
        final Integer diastolic2 = new Integer( 5 );
        final Integer hdl2 = new Integer( 5 );
        final Integer ldl2 = new Integer( 5 );
        final Integer tri2 = new Integer( 150 );
        final HouseholdSmokingStatus houseSmokingStatus2 = HouseholdSmokingStatus.INDOOR;
        final PatientSmokingStatus patientSmokingStatus2 = PatientSmokingStatus.EVERYDAY;
        final User hcp2 = new User( "anttiuhcp2", "12345", Role.ROLE_HCP, new Integer( 1 ) );
        final User p2 = new User( "anttiu2", "12345", Role.ROLE_PATIENT, new Integer( 1 ) );

        final BasicHealthMetrics metrics2 = new BasicHealthMetrics();
        metrics2.setHeight( height2 );
        metrics2.setWeight( weight2 );
        metrics2.setHeadCircumference( headCircumference2 );
        metrics2.setSystolic( systolic2 );
        metrics2.setDiastolic( diastolic2 );
        metrics2.setHdl( hdl2 );
        metrics2.setLdl( ldl2 );
        metrics2.setTri( tri2 );
        metrics2.setHouseSmokingStatus( houseSmokingStatus2 );
        metrics2.setPatientSmokingStatus( patientSmokingStatus2 );
        metrics2.setHcp( hcp2 );
        metrics2.setPatient( p2 );

        final BasicHealthMetrics metrics3 = new BasicHealthMetrics();
        metrics3.setHeight( height );
        metrics3.setWeight( weight );
        metrics3.setHeadCircumference( headCircumference );
        metrics3.setSystolic( systolic );
        metrics3.setDiastolic( diastolic );
        metrics3.setHdl( hdl );
        metrics3.setLdl( ldl );
        metrics3.setTri( tri );
        metrics3.setHouseSmokingStatus( houseSmokingStatus );
        metrics3.setPatientSmokingStatus( patientSmokingStatus );
        metrics3.setHcp( hcp );
        metrics3.setPatient( p );

        final BasicHealthMetrics metrics4 = new BasicHealthMetrics();
        metrics4.setHeight( height2 );
        metrics4.setWeight( weight2 );
        metrics4.setHeadCircumference( headCircumference2 );
        metrics4.setSystolic( systolic2 );
        metrics4.setDiastolic( diastolic2 );
        metrics4.setHdl( hdl2 );
        metrics4.setLdl( ldl2 );
        metrics4.setTri( tri2 );
        metrics4.setHouseSmokingStatus( houseSmokingStatus2 );
        metrics4.setPatientSmokingStatus( patientSmokingStatus2 );
        metrics4.setHcp( hcp2 );
        metrics4.setPatient( p2 );

        // 1 = 3
        // 2 = 4
        assertTrue( metrics.equals( metrics3 ) );
        assertTrue( metrics2.equals( metrics4 ) );
        assertTrue( metrics.equals( metrics ) );
        assertFalse( metrics.equals( metrics2 ) );
        assertFalse( metrics.equals( metrics4 ) );
        assertFalse( metrics2.equals( metrics3 ) );
        assertFalse( metrics.equals( null ) );

        // Testing with 1 and 3 which start the same
        metrics3.setWeight( weight2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setTri( tri2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setSystolic( systolic2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setPatientSmokingStatus( patientSmokingStatus2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setPatient( p2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setLdl( ldl2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHouseSmokingStatus( houseSmokingStatus2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHeight( height2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHeadCircumference( headCircumference2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHdl( hdl2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHcp( hcp2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setDiastolic( diastolic2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHdl( hdl2 );
        assertFalse( metrics.equals( metrics3 ) );
        metrics3.setHdl( hdl2 );
        assertFalse( metrics.equals( metrics3 ) );

        // now for null tests
        final BasicHealthMetrics metrics5 = new BasicHealthMetrics();
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setDiastolic( diastolic );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setHcp( hcp );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setHdl( hdl );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setHeadCircumference( headCircumference );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setHeight( height );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setHouseSmokingStatus( houseSmokingStatus );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setLdl( ldl );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setPatient( p );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setPatientSmokingStatus( patientSmokingStatus );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setSystolic( systolic );
        assertFalse( metrics5.equals( metrics ) );
        metrics5.setTri( tri );
        assertFalse( metrics5.equals( metrics ) );
    }

    @Test
    public void healthGetMethodTest () {
        final Float height = new Float( 10 );
        final Float weight = new Float( 10 );
        final Float headCircumference = new Float( 10 );
        final Integer systolic = new Integer( 10 );
        final Integer diastolic = new Integer( 10 );
        final Integer hdl = new Integer( 10 );
        final Integer ldl = new Integer( 10 );
        final Integer tri = new Integer( 100 );
        final HouseholdSmokingStatus houseSmokingStatus = HouseholdSmokingStatus.NONSMOKING;
        final PatientSmokingStatus patientSmokingStatus = PatientSmokingStatus.NEVER;
        final User hcp = new User( "anttihcp", "12345", Role.ROLE_HCP, new Integer( 1 ) );
        final User p = new User( "anttiu", "12345", Role.ROLE_PATIENT, new Integer( 1 ) );

        final User hcp2 = new User( "anttihcp2", "12345", Role.ROLE_HCP, new Integer( 1 ) );
        final User p2 = new User( "anttiu2", "12345", Role.ROLE_PATIENT, new Integer( 1 ) );

        final BasicHealthMetrics metrics = new BasicHealthMetrics();
        metrics.setHeight( height );
        metrics.setWeight( weight );
        metrics.setHeadCircumference( headCircumference );
        metrics.setSystolic( systolic );
        metrics.setDiastolic( diastolic );
        metrics.setHdl( hdl );
        metrics.setLdl( ldl );
        metrics.setTri( tri );
        metrics.setHouseSmokingStatus( houseSmokingStatus );
        metrics.setPatientSmokingStatus( patientSmokingStatus );
        metrics.setHcp( hcp );
        metrics.setPatient( p );

        final BasicHealthMetrics metrics2 = new BasicHealthMetrics();
        metrics2.setHeight( height );
        metrics2.setWeight( weight );
        metrics2.setHeadCircumference( headCircumference );
        metrics2.setSystolic( systolic );
        metrics2.setDiastolic( diastolic );
        metrics2.setHdl( hdl );
        metrics2.setLdl( ldl );
        metrics2.setTri( tri );
        metrics2.setHouseSmokingStatus( houseSmokingStatus );
        metrics2.setPatientSmokingStatus( patientSmokingStatus );
        metrics2.setHcp( hcp );
        metrics2.setPatient( p );

        assertEquals( metrics.getDiastolic(), metrics2.getDiastolic() );

        assertEquals( BasicHealthMetrics.getBasicHealthMetricsForHCPAndPatient( hcp.getId(), p.getId() ),
                BasicHealthMetrics.getBasicHealthMetricsForHCPAndPatient( hcp.getId(), p.getId() ) );

        assertEquals( BasicHealthMetrics.getBasicHealthMetrics(), BasicHealthMetrics.getBasicHealthMetrics() );

        assertEquals( BasicHealthMetrics.getBasicHealthMetricsForHCP( hcp.getId() ),
                BasicHealthMetrics.getBasicHealthMetricsForHCP( hcp2.getId() ) );

        assertEquals( BasicHealthMetrics.getBasicHealthMetricsForPatient( p.getId() ),
                BasicHealthMetrics.getBasicHealthMetricsForPatient( p2.getId() ) );

        assertEquals( BasicHealthMetrics.getById( metrics.getId() ), BasicHealthMetrics.getById( metrics2.getId() ) );

        assertEquals( metrics.getId(), metrics2.getId() );
        assertEquals( metrics.getPatient(), metrics2.getPatient() );
        assertEquals( metrics.getHcp(), metrics2.getHcp() );
        metrics.setHeight( null );
        assertEquals( metrics.getHeight(), metrics2.getHeight() );

        try {
            metrics.setHeight( new Float( .01 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getHeight(), metrics2.getHeight() );
        }
        metrics.setWeight( null );
        assertEquals( metrics.getWeight(), metrics2.getWeight() );
        try {
            metrics.setWeight( new Float( .01 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getWeight(), metrics2.getWeight() );
        }

        metrics.setHeadCircumference( null );
        assertEquals( metrics.getHeadCircumference(), metrics2.getHeadCircumference() );
        try {
            metrics.setHeadCircumference( new Float( .01 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getHeadCircumference(), metrics2.getHeadCircumference() );
        }

        metrics.setDiastolic( null );
        assertEquals( metrics.getDiastolic(), metrics2.getDiastolic() );
        try {
            metrics.setDiastolic( new Integer( 1000 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getDiastolic(), metrics2.getDiastolic() );
        }

        metrics.setSystolic( null );
        assertEquals( metrics.getSystolic(), metrics2.getSystolic() );
        try {
            metrics.setSystolic( new Integer( 1000 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getSystolic(), metrics2.getSystolic() );
        }

        metrics.setSystolic( null );
        assertEquals( metrics.getSystolic(), metrics2.getSystolic() );
        try {
            metrics.setSystolic( new Integer( 1000 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getSystolic(), metrics2.getSystolic() );
        }

        metrics.setHdl( null );
        assertEquals( metrics.getHdl(), metrics2.getHdl() );
        try {
            metrics.setHdl( new Integer( 1000 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getHdl(), metrics2.getHdl() );
        }

        metrics.setLdl( null );
        assertEquals( metrics.getLdl(), metrics2.getLdl() );
        try {
            metrics.setLdl( new Integer( 1000 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getLdl(), metrics2.getLdl() );
        }

        metrics.setTri( null );
        assertEquals( metrics.getTri(), metrics2.getTri() );
        try {
            metrics.setTri( new Integer( 1000 ) );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( metrics.getTri(), metrics2.getTri() );
        }

        assertEquals( metrics.getHouseSmokingStatus(), metrics2.getHouseSmokingStatus() );
        assertEquals( metrics.getPatientSmokingStatus(), metrics2.getPatientSmokingStatus() );

        final OfficeVisitForm f = new OfficeVisitForm();
        try {
            final BasicHealthMetrics metrics3 = new BasicHealthMetrics( f );
        }
        catch ( final ParseException e ) {
            // shouldn't have an error here anyway
            assertEquals( metrics.getHouseSmokingStatus(), metrics2.getHouseSmokingStatus() );
        }

    }

    @Test
    public void healthHashTest () {
        final Float height = new Float( 10 );
        final Float weight = new Float( 10 );
        final Float headCircumference = new Float( 10 );
        final Integer systolic = new Integer( 10 );
        final Integer diastolic = new Integer( 10 );
        final Integer hdl = new Integer( 10 );
        final Integer ldl = new Integer( 10 );
        final Integer tri = new Integer( 100 );
        final HouseholdSmokingStatus houseSmokingStatus = HouseholdSmokingStatus.NONSMOKING;
        final PatientSmokingStatus patientSmokingStatus = PatientSmokingStatus.NEVER;
        final User hcp = new User( "anttihcp", "12345", Role.ROLE_HCP, new Integer( 1 ) );
        final User p = new User( "anttiu", "12345", Role.ROLE_PATIENT, new Integer( 1 ) );
        final BasicHealthMetrics metrics = new BasicHealthMetrics();
        final BasicHealthMetrics metrics2 = new BasicHealthMetrics();

        assertEquals( metrics.hashCode(), metrics2.hashCode() );
        metrics.setHeight( height );
        metrics.setWeight( weight );
        metrics.setHeadCircumference( headCircumference );
        metrics.setSystolic( systolic );
        metrics.setDiastolic( diastolic );
        metrics.setHdl( hdl );
        metrics.setLdl( ldl );
        metrics.setTri( tri );
        metrics.setHouseSmokingStatus( houseSmokingStatus );
        metrics.setPatientSmokingStatus( patientSmokingStatus );
        metrics.setHcp( hcp );
        metrics.setPatient( p );

        metrics2.setHeight( height );
        metrics2.setWeight( weight );
        metrics2.setHeadCircumference( headCircumference );
        metrics2.setSystolic( systolic );
        metrics2.setDiastolic( diastolic );
        metrics2.setHdl( hdl );
        metrics2.setLdl( ldl );
        metrics2.setTri( tri );
        metrics2.setHouseSmokingStatus( houseSmokingStatus );
        metrics2.setPatientSmokingStatus( patientSmokingStatus );
        metrics2.setHcp( hcp );
        metrics2.setPatient( p );

        assertEquals( metrics.hashCode(), metrics2.hashCode() );
    }
}
