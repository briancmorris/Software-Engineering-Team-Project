/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.PriorityLevel;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;
import edu.ncsu.csc.itrust2.models.persistent.LabProcedure;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests functionality of LabProcedureForm class
 *
 * @author Anuraag
 *
 */
public class LabProcedureFormTest {

    @Test
    public void createLabProcedure () {
        final LabProcedure pro = new LabProcedure();
        final Calendar cal = Calendar.getInstance();
        pro.setDate( cal );
        final LOINCCode code = new LOINCCode();
        code.setCode( "code" );
        pro.setCode( code );
        pro.setComments( "comments" );
        pro.setId( (long) 12 );
        final long id = 12;
        final User lt = new User();
        lt.setRole( Role.ROLE_LT );
        pro.setLabTech( lt );
        final OfficeVisit ov = new OfficeVisit();
        ov.setId( (long) 11 );
        final long id2 = 11;
        pro.setOfficeVisit( ov );
        final PriorityLevel pl = PriorityLevel.ONE;
        pro.setPriorityLevel( pl );
        final CompletionStatus cs = CompletionStatus.COMPLETED;
        pro.setCompletionStatus( cs );

        final LabProcedureForm lpForm = new LabProcedureForm( pro );
        assertEquals( "code", lpForm.getCode() );
        assertEquals( "comments", lpForm.getComments() );
        assertTrue( lpForm.getID() == id );
        assertNull( lpForm.getLabTech() );
        assertTrue( lpForm.getOfficeVisitId() == id2 );
        assertEquals( "1", lpForm.getPriorityLevel() );
        assertEquals( "Completed", lpForm.getCompletionStatus() );

    }
}
