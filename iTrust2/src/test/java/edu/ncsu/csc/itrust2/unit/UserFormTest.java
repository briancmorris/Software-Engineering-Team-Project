/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests functionality of UserForm class
 *
 * @author Anuraag
 *
 */
public class UserFormTest {

    @Test
    public void createUserForm () {
        final User u = new User();
        u.setEnabled( 1 );
        u.setPassword( "password" );
        final Role role = Role.ROLE_PATIENT;
        u.setRole( role );
        u.setUsername( "username" );

        final UserForm uForm = new UserForm();
        uForm.setEnabled( "enabled" );
        uForm.setPassword( "PASSWORD" );
        uForm.setPassword2( "PASSWORD2" );
        uForm.setRole( "role" );
        uForm.setUsername( "USERNAME" );
        final UserForm uForm2 = new UserForm( "user", "pass", "role", "enab" );
        final UserForm uForm3 = new UserForm( u );
        final UserForm uForm4 = new UserForm( "user1", "pass1", role, 1 );

        assertEquals( "enabled", uForm.getEnabled() );
        assertEquals( "PASSWORD", uForm.getPassword() );
        assertEquals( "PASSWORD2", uForm.getPassword2() );
        assertEquals( "role", uForm.getRole() );
        assertEquals( "USERNAME", uForm.getUsername() );

        assertEquals( "enab", uForm2.getEnabled() );
        assertEquals( "pass", uForm2.getPassword() );
        assertEquals( "pass", uForm2.getPassword2() );
        assertEquals( "role", uForm2.getRole() );
        assertEquals( "user", uForm2.getUsername() );

        assertEquals( "1", uForm3.getEnabled() );
        assertEquals( "ROLE_PATIENT", uForm3.getRole() );
        assertEquals( "username", uForm3.getUsername() );

        assertEquals( "true", uForm4.getEnabled() );
        assertEquals( "pass1", uForm4.getPassword() );
        assertEquals( "ROLE_PATIENT", uForm4.getRole() );
        assertEquals( "user1", uForm4.getUsername() );

    }
}
