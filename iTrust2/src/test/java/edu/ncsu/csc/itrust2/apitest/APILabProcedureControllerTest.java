package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.forms.patient.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
//import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;
import edu.ncsu.csc.itrust2.models.persistent.LabProcedure;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test class for APILabProcedureController to test fuctionality
 * 
 * @author Timothy Fggins
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APILabProcedureControllerTest {

    private MockMvc               mvc;
    
    private static LabProcedureForm      labForm;

    @Autowired
    private WebApplicationContext context;
    
    @BeforeClass
    public static void formSetup() throws Exception {
        /**
        // Create a lab form
        LOINCCode l = new LOINCCode();
        l.setCode( "82345-7" );
        l.setId( 1L );
        l.setComponent( "test" );
        l.setLongCommonName( "this is a test" );
        l.setProp( "super test" );
        l.setSpecialUsage( "special" );
        //l.save();
        labForm = new LabProcedureForm();
        labForm.setCode( "82345-7" );
        labForm.setComments( "test" );
        labForm.setCompletionStatus( CompletionStatus.NOT_STARTED.name() );
        labForm.setID( 1L );
        labForm.setDate( "12/03/2018" );
        labForm.setTime( "12:09 PM" );
        labForm.setLabTech( "lt1" );
        labForm.setOfficeVisitId( 1L );
        */
    }
    /**
     * Sets up tests
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }


    @Test
    @WithMockUser(username = "lt1", roles = { "USER", "LT" })
    public void testLTFuctions () throws Exception {
        
        // Creates necessary users, does nothing if they already exists
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );
        
        final UserForm patient = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );
        
        // Creates 2 LT users, does nothing if they already exists
        final UserForm lt1 = new UserForm( "lt1", "123456", Role.ROLE_LT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( lt1 ) ) );
        
        final UserForm lt2 = new UserForm( "lt2", "123456", Role.ROLE_LT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( lt2 ) ) );
        
        //mvc.perform( formLogin( "/login" ).user( "lt1" ).password( "pass" ) );
        
        /* Create a Hospital to use too */
        final Hospital hospital = new Hospital( "iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC" );
        mvc.perform( post( "/api/v1/hospitals" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hospital ) ) );

        mvc.perform( delete( "/api/v1/officevisits" ) );
        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate( "4/16/2048" );
        visit.setTime( "9:50 AM" );
        visit.setHcp( "hcp" );
        visit.setPatient( "patient" );
        visit.setNotes( "Test office visit" );
        visit.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        visit.setHospital( "iTrust Test Hospital 2" );

        /* Create the Office Visit */
        /**
        mvc.perform( post( "/api/v1/officevisits" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( visit ) ) ).andExpect( status().isOk() );
                */
        OfficeVisit ofv = new OfficeVisit(visit);
        ofv.save();
        
        // Create Procedure
        //LabProcedure lab = new LabProcedure(labForm);
        
        LOINCCodeForm lf = new LOINCCodeForm();
        lf.setCode( "82345-7" );
        lf.setComponent( "test" );
        //lf.setId( 1L );
        lf.setLongCommonName( "thisisatest" );
        lf.setProp( "prop" );
        lf.setSpecialUsage( "testusuage" );
        LOINCCode l = new LOINCCode(lf);
        l.save();
        
        labForm = new LabProcedureForm();
        labForm.setCode( l.getCode() );
        labForm.setComments( "test" );
        labForm.setCompletionStatus( CompletionStatus.NOT_STARTED.name() );
        //labForm.setID( 1L );
        labForm.setDate( "12/03/2018" );
        labForm.setTime( "12:09 PM" );
        labForm.setLabTech( "lt1" );
        labForm.setOfficeVisitId(ofv.getId() );
        LabProcedure lab = new LabProcedure(labForm);
        lab.save();
        labForm.setID( lab.getId() );
        
        mvc.perform( formLogin( "/login" ).user( "lt1" ).password( "pass" ) );
        
        // Update comments
        labForm.setComments( "New comments" );
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() ); 
        
        
        // Update status 
        labForm.setCompletionStatus( CompletionStatus.IN_PROGRESS.name() );
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() ); 
        
        // Update comments and status
        labForm.setComments( "new comments again" );
        labForm.setCompletionStatus( CompletionStatus.COMPLETED.name() );
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() );
        
        // Try to update a lab that doesn't exists
        LabProcedureForm fake = new LabProcedureForm();
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( fake ) ) ).andExpect( status().isNotFound() );
        
        // Assign to a new labtech
        labForm.setLabTech( lt2.getUsername() );
        mvc.perform( put( "/api/v1/labProcedures/reassign" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() );
    }

}
