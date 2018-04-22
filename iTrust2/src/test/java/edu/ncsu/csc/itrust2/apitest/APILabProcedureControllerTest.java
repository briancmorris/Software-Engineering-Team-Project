package edu.ncsu.csc.itrust2.apitest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
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
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class APILabProcedureControllerTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up tests
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @WithMockUser ( username = "lt1", roles = { "USER", "LT" } )
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

        /* Create a Hospital to use too */
        final Hospital hospital = new Hospital( "iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC" );
        mvc.perform( post( "/api/v1/hospitals" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hospital ) ) );

        // mvc.perform( delete( "/api/v1/officevisits" ) );
        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate( "4/16/2048" );
        visit.setTime( "9:50 AM" );
        visit.setHcp( "hcp" );
        visit.setPatient( "patient" );
        visit.setNotes( "Test office visit" );
        visit.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        visit.setHospital( "iTrust Test Hospital 2" );

        /* Create the Office Visit */

        final OfficeVisit ofv = new OfficeVisit( visit );
        ofv.save();
        // for future use

        // Create Procedure
        // LabProcedure lab = new LabProcedure(labForm);

        final LOINCCodeForm lf = new LOINCCodeForm();
        lf.setCode( "82345-7" );
        lf.setComponent( "test" );
        // lf.setId( 1L );
        lf.setLongCommonName( "thisisatest" );
        lf.setProp( "prop" );
        lf.setSpecialUsage( "testusuage" );
        final LOINCCode l = new LOINCCode( lf );
        l.save();

        final LabProcedureForm labForm = new LabProcedureForm();
        labForm.setCode( l.getCode() );
        labForm.setComments( "test" );
        labForm.setCompletionStatus( CompletionStatus.NOT_STARTED.getName() );
        // labForm.setID( 1L );
        labForm.setDate( "12/03/2018" );
        labForm.setTime( "12:09 PM" );
        labForm.setLabTech( "lt1" );
        labForm.setOfficeVisitId( ofv.getId() );
        final LabProcedure lab = new LabProcedure( labForm );
        lab.save();
        labForm.setID( lab.getId() );

        mvc.perform( formLogin( "/login" ).user( "lt1" ).password( "pass" ) );

        // Update comments
        labForm.setComments( "New comments" );
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() );

        // Make sure lab has new comments
        mvc.perform( get( "/api/v1/labProcedure/" + lab.getId() ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.comments", is( "New comments" ) ) );

        // Update status
        labForm.setCompletionStatus( CompletionStatus.IN_PROGRESS.getName() );
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() );

        // Make sure lab has new status and *same* comments
        mvc.perform( get( "/api/v1/labProcedure/" + lab.getId() ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.completionStatus", is( CompletionStatus.IN_PROGRESS.name() ) ) )
                .andExpect( jsonPath( "$.comments", is( "New comments" ) ) );

        // Update comments and status
        labForm.setComments( "new comments again" );
        labForm.setCompletionStatus( CompletionStatus.COMPLETED.getName() );
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() );

        // Make sure lab as new status and new comments
        mvc.perform( get( "/api/v1/labProcedure/" + lab.getId() ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.completionStatus", is( CompletionStatus.COMPLETED.name() ) ) )
                .andExpect( jsonPath( "$.comments", is( "new comments again" ) ) );

        // Try to update a lab that doesn't exists
        final LabProcedureForm fake = new LabProcedureForm();
        mvc.perform( put( "/api/v1/labProcedures" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( fake ) ) ).andExpect( status().isBadRequest() );

        // Assign to a new labtech
        labForm.setLabTech( lt2.getUsername() );
        mvc.perform( put( "/api/v1/labProcedures/reassign" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( labForm ) ) ).andExpect( status().isOk() );

        // Make sure current labTech no longer has labProcedure
        mvc.perform( get( "/api/v1/labProcedure/" + lab.getId() ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.labTech.username", is( "lt2" ) ) );

        mvc.perform( get( "/api/v1/labProcedures" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$", hasSize( 0 ) ) );

        // Assign a missing lab procedure should result in failure
        mvc.perform( put( "/api/v1/labProcedures/reassign" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( fake ) ) ).andExpect( status().isBadRequest() );
    }

    /**
     * @Test public void testzGetByOfficeVisit () throws Exception {
     * 
     *       final LOINCCodeForm lf = new LOINCCodeForm(); lf.setCode( "82345-7"
     *       ); lf.setComponent( "test" ); // lf.setId( 1L );
     *       lf.setLongCommonName( "thisisatest" ); lf.setProp( "prop" );
     *       lf.setSpecialUsage( "testusuage" ); final LOINCCode l = new
     *       LOINCCode( lf ); l.save();
     * 
     *       final LabProcedureForm labForm = new LabProcedureForm();
     *       labForm.setCode( l.getCode() ); labForm.setComments( "test" );
     *       labForm.setCompletionStatus( CompletionStatus.NOT_STARTED.getName()
     *       ); // labForm.setID( 1L ); labForm.setDate( "12/03/2018" );
     *       labForm.setTime( "12:09 PM" ); labForm.setLabTech( "lt1" ); final
     *       List<LabProcedureForm> labprocedures = new
     *       ArrayList<LabProcedureForm>(); labprocedures.add( labForm );
     * 
     *       mvc.perform( delete( "/api/v1/appointmentrequests" ) );
     * 
     *       final AppointmentRequestForm appointmentForm = new
     *       AppointmentRequestForm(); appointmentForm.setDate( "11/19/2030" );
     *       appointmentForm.setTime( "4:50 AM" ); appointmentForm.setType(
     *       AppointmentType.GENERAL_CHECKUP.toString() );
     *       appointmentForm.setStatus( Status.APPROVED.toString() );
     *       appointmentForm.setHcp( "hcp" ); appointmentForm.setPatient(
     *       "patient" ); appointmentForm.setComments( "Test appointment please
     *       ignore" ); mvc.perform( post( "/api/v1/appointmentrequests"
     *       ).contentType( MediaType.APPLICATION_JSON ) .content(
     *       TestUtils.asJsonString( appointmentForm ) ) ).andExpect(
     *       status().isOk() );
     * 
     *       final OfficeVisitForm visit = new OfficeVisitForm();
     *       visit.setPreScheduled( "yes" ); visit.setDate( "11/19/2030" );
     *       visit.setTime( "4:50 AM" ); visit.setHcp( "hcp" );
     *       visit.setPatient( "patient" ); visit.setNotes( "Test office visit"
     *       ); visit.setType( AppointmentType.GENERAL_CHECKUP.toString() );
     *       visit.setHospital( "iTrust Test Hospital 2" );
     *       visit.setLabProcedures( labprocedures );
     * 
     *       final OfficeVisit office = new OfficeVisit( visit ); office.save();
     *       labForm.setOfficeVisitId( office.getId() );
     * 
     *       final LabProcedure lab = new LabProcedure( labForm );
     *       lab.setOfficeVisit( office ); lab.save(); final List<LabProcedure>
     *       lp = new ArrayList<LabProcedure>(); lp.add( lab );
     *       office.setLabProcedures( lp ); office.save();
     * 
     *       mvc.perform( get( "/api/v1/labsforvisit/" + office.getId() )
     *       ).andExpect( status().isOk() );
     * 
     *       }
     */
}
