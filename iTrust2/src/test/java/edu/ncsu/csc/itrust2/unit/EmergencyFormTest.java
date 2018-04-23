/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.er.EmergencyForm;

/**
 * Tests functionality of EmergencyForm class
 *
 * @author Anuraag
 *
 */
public class EmergencyFormTest {

    @Test
    public void createEmergencyForm () {
        final EmergencyForm eF = new EmergencyForm();
        eF.setAge( "1" );
        assertEquals( "1", eF.getAge() );
        eF.setDateOfBirth( "04/30/2000" );
        assertEquals( "04/30/2000", eF.getDateOfBirth() );

        final EmergencyForm eForm = new EmergencyForm( "AliceThirteen" );
        eForm.setAge( "12" );
        eForm.setBloodType( "A+" );
        eForm.setDateOfBirth( "02/01/2000" );
        eForm.setFirstName( "Alice" );
        eForm.setGender( "female" );
        eForm.setId( "1" );
        eForm.setLastName( "Thirteen" );
        eForm.setName( "Alice Thirteen" );
        eForm.setSelf( "self" );

        assertEquals( "12", eForm.getAge() );
        assertEquals( "A+", eForm.getBloodType() );
        assertEquals( "02/01/2000", eForm.getDateOfBirth() );
        assertEquals( "Alice", eForm.getFirstName() );
        assertEquals( "female", eForm.getGender() );
        assertEquals( "1", eForm.getId() );
        assertEquals( "Thirteen", eForm.getLastName() );
        assertEquals( "Alice Thirteen", eForm.getName() );
        assertEquals( "self", eForm.getSelf() );

    }
}
