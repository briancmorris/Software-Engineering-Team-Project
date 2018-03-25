package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test for API functionality for interacting with Patients
 *
 * @author Kai Presler-Marshall
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class APIPatientTest {

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
     * Tests that getting a patient that doesn't exist returns the proper
     * status.
     *
     * @throws Exception
     */
    @Test
    public void testGetNonExistentPatient () throws Exception {
        mvc.perform( get( "/api/v1/patients/-1" ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests PatientAPI
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testPatientAPI () throws Exception {
        // Clear out all patients before running these tests.
        DomainObject.deleteAll( Patient.class );

        final UserForm p = new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( p ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "6/15/1977" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        // Editing the patient before they exist should fail
        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isNotFound() );

        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        // Creating the same patient twice should fail.
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().is4xxClientError() );

        mvc.perform( get( "/api/v1/patients" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        mvc.perform( get( "/api/v1/patients/antti" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        patient.setPreferredName( "Antti Walhelm" );

        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // Editing with the wrong username should fail.
        mvc.perform( put( "/api/v1/patients/badusername" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isConflict() );

        mvc.perform( delete( "/api/v1/patients" ) );
    }

    /**
     * Test accessing the patient PUT request unauthenticated
     *
     * @throws Exception
     */
    @Test
    public void testPatientUnauthenticated () throws Exception {
        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "6/15/1977" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isUnauthorized() );
    }

    /**
     * Test accessing the patient PUT request as a patient
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "antti", roles = { "PATIENT" } )
    public void testPatientAsPatient () throws Exception {
        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "6/15/1977" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Antti" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );

        final Patient antti = new Patient( patient );
        antti.save(); // create the patient if they don't exist already

        // a patient can edit their own info
        mvc.perform( put( "/api/v1/patients/antti" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        // but they can't edit someone else's
        patient.setSelf( "patient" );
        mvc.perform( put( "/api/v1/patients/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isUnauthorized() );
    }

    /**
     * Testing the end point for editDemographics in PatientController
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "antti", roles = { "PATIENT" } )
    public void testPatientController () throws Exception {

        // Adding an extra test here to test APIPatientController's get method
        mvc.perform( get( "/api/v1/patient" ).contentType( MediaType.APPLICATION_JSON ).content( "" ) )
                .andExpect( status().isOk() );

        // Test the Get end point for viewOfficeVisits
        mvc.perform( get( "/patient/viewOfficeVisits" ).contentType( MediaType.APPLICATION_JSON ).content( "" ) )
                .andExpect( status().isOk() );

        // Test the Get end point for viewPrescriptions
        mvc.perform( get( "/patient/viewPrescriptions" ).contentType( MediaType.APPLICATION_JSON ).content( "" ) )
                .andExpect( status().isOk() );

        // Test the Get end point for editDemographics
        mvc.perform( get( "/patient/editDemographics" ).contentType( MediaType.APPLICATION_JSON ).content( "" ) )
                .andExpect( status().isOk() );

        // Test the Post end point for editDemographics
        RequestBuilder request = post( "/patient/editDemographics" ).param( "firstName", "Antti" )
                .param( "lastName", "Walhelm" ).param( "email", "antti@itrust.fi" ).param( "address1", "1 Test Street" )
                .param( "address2", "Some Location" ).param( "city", "Raleigh" ).param( "state", "AL" )
                .param( "zip", "55555" ).param( "phone", "555-555-5555" ).param( "dateOfBirth", "06/19/1994" )
                .param( "bloodType", "A+" ).param( "ethnicity", "Caucasian" ).param( "gender", "Male" );

        mvc.perform( request ).andExpect( status().isOk() );

        final Patient p = Patient.getByName( "antti" );
        assertEquals( "Antti", p.getFirstName() );
        assertEquals( "555-555-5555", p.getPhone() );
        assertEquals( "55555", p.getZip() );
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        String parsedDate = sdf.format( p.getDateOfBirth().getTime() );
        assertEquals( "06/19/1994", parsedDate );
        assertEquals( "Raleigh", p.getCity() );

        // Bad phone number
        request = post( "/patient/editDemographics" ).param( "firstName", "Antti" ).param( "lastName", "Walhelm" )
                .param( "email", "antti@itrust.fi" ).param( "address1", "1 Test Street" )
                .param( "address2", "Some Location" ).param( "city", "Raleigh" ).param( "state", "AL" )
                .param( "zip", "55555" ).param( "phone", "555655565555" ).param( "dateOfBirth", "06/19/1994" )
                .param( "bloodType", "A+" ).param( "ethnicity", "Caucasian" ).param( "gender", "Male" );
        mvc.perform( request ).andExpect( status().isOk() );
        assertEquals( "555-555-5555", p.getPhone() );

        // Bad city
        request = post( "/patient/editDemographics" ).param( "firstName", "Antti" ).param( "lastName", "Walhelm" )
                .param( "email", "antti@itrust.fi" ).param( "address1", "1 Test Street" )
                .param( "address2", "Some Location" ).param( "city", "Raleigh 45" ).param( "state", "AL" )
                .param( "zip", "55555" ).param( "phone", "555-555-5555" ).param( "dateOfBirth", "06/19/1994" )
                .param( "bloodType", "A+" ).param( "ethnicity", "Caucasian" ).param( "gender", "Male" );
        mvc.perform( request ).andExpect( status().isOk() );
        assertEquals( "Raleigh", p.getCity() );

        // bad zip
        request = post( "/patient/editDemographics" ).param( "firstName", "Antti" ).param( "lastName", "Walhelm" )
                .param( "email", "antti@itrust.fi" ).param( "address1", "1 Test Street" )
                .param( "address2", "Some Location" ).param( "city", "Raleigh" ).param( "state", "AL" )
                .param( "zip", "5555555" ).param( "phone", "555-555-5555" ).param( "dateOfBirth", "06/19/1994" )
                .param( "bloodType", "A+" ).param( "ethnicity", "Caucasian" ).param( "gender", "Male" );

        mvc.perform( request ).andExpect( status().isOk() );
        assertEquals( "55555", p.getZip() );

        // bad dateOfBirth
        request = post( "/patient/editDemographics" ).param( "firstName", "Antti" ).param( "lastName", "Walhelm" )
                .param( "email", "antti@itrust.fi" ).param( "address1", "1 Test Street" )
                .param( "address2", "Some Location" ).param( "city", "Raleigh" ).param( "state", "AL" )
                .param( "zip", "55555" ).param( "phone", "555-555-5555" ).param( "dateOfBirth", "0671971994" )
                .param( "bloodType", "A+" ).param( "ethnicity", "Caucasian" ).param( "gender", "Male" );

        mvc.perform( request ).andExpect( status().isOk() );
        parsedDate = sdf.format( p.getDateOfBirth().getTime() );
        assertEquals( "06/19/1994", parsedDate );

        // Resetting antti back to normal just in case
        request = post( "/patient/editDemographics" ).param( "firstName", "Antti" ).param( "lastName", "Walhelm" )
                .param( "email", "antti@itrust.fi" ).param( "address1", "1 Test Street" )
                .param( "address2", "Some Location" ).param( "city", "Viipuri" ).param( "state", "AL" )
                .param( "zip", "27514" ).param( "phone", "123-456-7890" ).param( "dateOfBirth", "06/15/1977" )
                .param( "bloodType", "A+" ).param( "ethnicity", "Caucasian" ).param( "gender", "Male" );

        mvc.perform( request ).andExpect( status().isOk() );
    }

}
