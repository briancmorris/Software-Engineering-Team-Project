package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.er.EmergencyForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Tests EmergencyForm for correct and complete implementation.
 *
 * @author Garrett Watts
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class EmergencyRecordTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests the constructor of EmergencyForm
     *
     * @throws ParseException
     */
    @Test
    public void testEmergencyForm () throws ParseException {
        final User newu = new User( "gcwatts", "123456", Role.ROLE_PATIENT, 1 );
        newu.save();

        // Create a patient with demographics
        final PatientForm patient = new PatientForm();
        // Other required demographics for a patient, may not be needed
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setCity( "Viipuri" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "gcwatts" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        // Demographics I car about
        patient.setFirstName( "Garrett" );
        patient.setLastName( "Watts" );
        patient.setDateOfBirth( "06/19/1994" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setGender( Gender.Male.toString() );

        final Patient gcwatts = new Patient( patient );
        gcwatts.save(); // create the patient if they don't exist already

        // Test form.
        EmergencyForm form = new EmergencyForm( "gcwatts" );

        assertTrue( form.getFirstName().equals( "Garrett" ) );
        assertTrue( form.getLastName().equals( "Watts" ) );
        assertTrue( form.getName().equals( "Garrett Watts" ) );
        assertTrue( form.getAge().equals( "23" ) );
        assertTrue( form.getDateOfBirth().equals( "06/19/1994" ) );
        assertTrue( form.getBloodType().equals( "A+" ) );
        assertTrue( form.getGender().equals( "Male" ) );
        assertTrue( form.getId().equals( "gcwatts" ) );

        // Extra line coverage by setting dob to this month + some days.
        patient.setDateOfBirth( "04/30/1994" );
        final Patient gcwatts2 = new Patient( patient );
        gcwatts2.save();
        form = new EmergencyForm( "gcwatts" );

        final Date today = Calendar.getInstance().getTime();
        if ( today.getMonth() <= 4 && today.getDate() < 30 ) {
            assertTrue( form.getAge().equals( "23" ) );
        }
        else {
            assertTrue( form.getAge().equals( "23" ) );
        }
    }

    /**
     * Tests the API endpoints for APIEmergencyController
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testHealthRecordAPI () throws Exception {

        mvc.perform( get( "/api/v1/emergencyHealthRecords/demo/gcwatts" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        mvc.perform( get( "/api/v1/emergencyHealthRecords/diag/gcwatts" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        mvc.perform( get( "/api/v1/emergencyHealthRecords/pres/gcwatts" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
    }

}
