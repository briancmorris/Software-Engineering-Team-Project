/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.DeleteUserForm;

/**
 * Tests functionality of DeleteUserForm class
 *
 * @author Anuraag
 *
 */
public class DeleteUserFormTest {

    @Test
    public void createDeleteUserForm () {
        final DeleteUserForm delUserForm = new DeleteUserForm();
        delUserForm.setName( "name" );
        delUserForm.setConfirm( "confirm" );
        assertEquals( "name", delUserForm.getName() );
        assertEquals( "confirm", delUserForm.getConfirm() );
    }
}
