
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
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
import org.junit.runners.MethodSorters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;



import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Class for testing Representative API
 * 
 * @author Timothy Figgins
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class APIRepresentativeControllerTest {

    private MockMvc               mvc;

    private final String REP1_USER = "test_rep1";
    
    private final String REP2_USER = "test_rep2";
    
    @Autowired
    private WebApplicationContext context;
    
    /**
     * Performs setup before test class
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUp () throws Exception {
        // For future testing.
        for (final Patient p : Patient.getPatients()) {
            p.getRepresentatives().clear();
            p.getRepresentees().clear();
            p.save();
        }
    }
    
    /**
     * Setup before every test
     * 
     * @throws Exception
     */
    @Before
    public void setUp2() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }
    
    /**
     * Clean up for test class
     */
    @AfterClass
    public static void tearDown() {
     // For future testing.
        for (final Patient p : Patient.getPatients()) {
            p.getRepresentatives().clear();
            p.getRepresentees().clear();
            p.save();
        }
    }

    /**
     * Tests adding representatives to patient
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "USER", "PATIENT" } )
    public void testAddRepAPI () throws Exception {
        /*
         * Create a HCP and a Patient to use. If they already exist, this
         * will do nothing
         */
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );

        // Create patient and two other patients to be representatives
        final UserForm patient = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );
        
        final UserForm rep1 = new UserForm( REP1_USER, "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( rep1 ) ) );
        
        final UserForm rep2 = new UserForm( REP2_USER, "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( rep2 ) ) );

        // Add rep1 as a patient
        mvc.perform( formLogin( "/login" ).user( "patient" ).password( "pass" ) );
        
        mvc
        .perform( post( "/api/v1/editPersonalRepresentatives" ).contentType( MediaType.APPLICATION_JSON )
                .content( REP1_USER ) )
        .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        
        mvc.perform( get( "/api/v1/editPersonalRepresentatives" ) ).andExpect( status().isOk() )
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].self.username", is(REP1_USER)));
        
        // Duplicate checks, should fail
        
        mvc
        .perform( post( "/api/v1/editPersonalRepresentatives" ).contentType( MediaType.APPLICATION_JSON )
                .content( REP1_USER ) )
        .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
        
        mvc
        .perform( post( "/api/v1/editPersonalRepresentatives" ).contentType( MediaType.APPLICATION_JSON )
                .content( "patient" ) )
        .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
    }  


    /**
     * Test making sure patient was added successfully as a represeentee of test_rep1
     * 
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "test_rep1", roles = { "USER", "PATIENT" } )
    public void testRepresenteeExists() throws Exception {
     // Login as rep to make sure patient added as representee
        mvc.perform( formLogin( "/login" ).user( REP1_USER ).password( "pass" ) );
        
        mvc.perform( get( "/api/v1/editPersonalRepresentatives/myPatients" ) ).andExpect( status().isOk() )
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].self.username", is("patient")));
    }
    
    /**
     * Tests HCP functionality
     * 
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testHCPAddRep() throws Exception {
     // Login as rep to make sure patient added as representee
    mvc.perform( formLogin( "/login" ).user( "hcp" ).password( "pass" ) );
    
    
    // Add rep2 to patient's list of representatives
    mvc
    .perform( post( "/api/v1/editPersonalRepresentatives/patient" ).contentType( MediaType.APPLICATION_JSON )
            .content( REP2_USER ) )
    .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
    
    
    mvc.perform( get( "/api/v1/editPersonalRepresentatives/patient" ) ).andExpect( status().isOk() )
    .andExpect(jsonPath("$", hasSize(2)));
    
    mvc.perform( get( "/api/v1/editPersonalRepresentatives/patientRepresentees/" + REP2_USER ) ).andExpect( status().isOk() )
    .andExpect(jsonPath("$", hasSize(1)))
    .andExpect(jsonPath("$[0].self.username", is("patient")));
    
    // Duplicate check, should fail
    mvc
    .perform( post( "/api/v1/editPersonalRepresentatives/patient" ).contentType( MediaType.APPLICATION_JSON )
            .content( REP2_USER ) )
    .andExpect( status().isBadRequest() ).andReturn().getResponse().getContentAsString();
    }
    
    /**
     * Test removing representee
     * 
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "test_rep1", roles = { "USER", "PATIENT" } )
    public void testUndeclareARepresentee() throws Exception {
        // Login as rep to make sure patient added as representee
        mvc.perform( formLogin( "/login" ).user( REP1_USER ).password( "pass" ) );
        
        // Try to remove someone who doesn't exist should fail
        mvc
        .perform( delete( "/api/v1/editPersonalRepresentatives/removeSelf/dummy" ))
        .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        
        // Valid test
        mvc
        .perform( delete( "/api/v1/editPersonalRepresentatives/removeSelf/patient" ))
        .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        
        mvc.perform( get( "/api/v1/editPersonalRepresentatives/myPatients" ) ).andExpect( status().isOk() )
        .andExpect(jsonPath("$", hasSize(0)));
    }
    
    /**
     * Test removing representatives
     * 
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "USER", "PATIENT" } )
    public void testUndeclareRepresentatives() throws Exception {
        // Add rep1 as a patient
        mvc.perform( formLogin( "/login" ).user( "patient" ).password( "pass" ) );
        
        // Should only have rep2 at this point, rep1 removed themselves
        mvc.perform( get( "/api/v1/editPersonalRepresentatives" ) ).andExpect( status().isOk() )
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].self.username", is(REP2_USER)));
        
        // Try to remove someone who doesn't exist should fail
        mvc
        .perform( delete( "/api/v1/editPersonalRepresentatives/dummy" ))
        .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        
        // Valid test
        mvc
        .perform( delete( "/api/v1/editPersonalRepresentatives/" + REP2_USER ))
        .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        
        mvc.perform( get( "/api/v1/editPersonalRepresentatives" ) ).andExpect( status().isOk() )
        .andExpect(jsonPath("$", hasSize(0)));
    }
    
    
    
}
