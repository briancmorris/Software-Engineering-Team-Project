/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.Drug;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests functionality of PrescriptionForm class
 *
 * @author Anuraag
 *
 */
public class PrescriptionFormTest {

    @Test
    public void createPrescriptionForm () {
        final Prescription pres = new Prescription();
        pres.setDosage( 2 );
        final Drug drug = new Drug();
        drug.setCode( "code" );
        pres.setDrug( drug );
        final Calendar cal = Calendar.getInstance();
        pres.setEndDate( cal );
        pres.setId( (long) ( 12 ) );
        final long id = 12;
        final User u = new User();
        u.setUsername( "username" );
        pres.setPatient( u );
        pres.setRenewals( 1 );
        pres.setStartDate( cal );

        final PrescriptionForm pForm = new PrescriptionForm( pres );
        assertEquals( 2, pForm.getDosage() );
        assertEquals( "code", pForm.getDrug() );
        assertTrue( pForm.getId() == id );
        assertEquals( "username", pForm.getPatient() );
        assertEquals( 1, pForm.getRenewals() );

        final PrescriptionForm pFormNew = new PrescriptionForm();
        pFormNew.setDosage( 5 );
        pFormNew.setDrug( "drug" );
        pFormNew.setEndDate( "02/01/2019" );
        pFormNew.setId( (long) 12 );
        pFormNew.setPatient( "user" );
        pFormNew.setRenewals( 3 );
        pFormNew.setStartDate( "04/30/2018" );

        assertEquals( 5, pFormNew.getDosage() );
        assertEquals( "drug", pFormNew.getDrug() );
        assertEquals( "04/30/2018", pFormNew.getStartDate() );
        assertEquals( "02/01/2019", pFormNew.getEndDate() );
        assertTrue( pFormNew.getId() == id );
        assertEquals( "user", pFormNew.getPatient() );
        assertEquals( 3, pFormNew.getRenewals() );

    }

}
