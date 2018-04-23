/**
 *
 */
package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.DrugForm;
import edu.ncsu.csc.itrust2.models.persistent.Drug;

/**
 * Tests functionality of DrugForm class
 *
 * @author Anuraag
 *
 */
public class DrugFormTest {

    @Test
    public void createDrugForm () {
        final Drug drug = new Drug();
        drug.setCode( "abc" );
        drug.setDescription( "test" );
        drug.setId( (long) 12 );
        final long id = drug.getId();
        drug.setName( "drug" );

        final DrugForm drugFormNew = new DrugForm( drug );
        assertEquals( "abc", drugFormNew.getCode() );
        assertEquals( "test", drugFormNew.getDescription() );
        assertEquals( "drug", drugFormNew.getName() );
        assertTrue( drugFormNew.getId() == id );
    }

}
