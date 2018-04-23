/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.DeleteHospitalForm;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;

/**
 * Tests functionality of DeleteHospitalForm class
 *
 * @author Anuraag
 *
 */
public class DeleteHospitalFormTest {

    @Test
    public void createDeleteHospitalFormTest () {
        final Hospital hp = new Hospital();
        hp.setAddress( "happy lane" );
        assertEquals( hp.getAddress(), "happy lane" );
        hp.setName( "Hope" );
        assertEquals( hp.getName(), "Hope" );
        hp.setState( State.NC );
        assertEquals( hp.getState(), State.NC );
        hp.setZip( "27510" );
        assertEquals( hp.getZip(), "27510" );

        final DeleteHospitalForm hpForm = new DeleteHospitalForm();
        hpForm.setName( "hospital" );
        hpForm.setConfirm( "confirm" );

        assertEquals( "hospital", hpForm.getName() );
        assertEquals( "confirm", hpForm.getConfirm() );

    }

}
