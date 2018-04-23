/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test class for APILOINCCodeController
 *
 * @author Timothy Figgins
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APILOINCCodeControllerTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up tests
     * 
     * @throws Exception
     */
    @Before
    public void setup () throws Exception {
        for ( final LOINCCode l : LOINCCode.getAll() ) {
            mvc.perform( delete( "/api/v1/loinccode/" + l.getId() ).contentType( MediaType.APPLICATION_JSON ) );
        }
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void testLOINCAPI () throws Exception {

        // Creates admin if does not already exists
        final UserForm admin = new UserForm( "admin", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( admin ) ) );

        mvc.perform( formLogin( "/login" ).user( "admin" ).password( "pass" ) );

        final LOINCCodeForm form = new LOINCCodeForm();
        form.setCode( "82345-7" );
        form.setComponent( "test" );
        form.setLongCommonName( "thisisatest" );
        form.setProp( "prop" );
        form.setSpecialUsage( "testusuage" );

        String content = mvc.perform( post( "/api/v1/loinccodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        final Gson gson = new GsonBuilder().create();
        LOINCCodeForm code = gson.fromJson( content, LOINCCodeForm.class );
        form.setId( code.getId() ); // fill in the id of the code we just
                                    // created
        assertEquals( form.getComponent(), code.getComponent() );

        content = mvc.perform( get( "/api/v1/loinccode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();
        code = gson.fromJson( content, LOINCCodeForm.class );
        assertEquals( form.getComponent(), code.getComponent() );

        mvc.perform( get( "/api/v1/loinccodes" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // edit it
        form.setCode( "12345-7" );
        content = mvc.perform( put( "/api/v1/loinccode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andReturn().getResponse().getContentAsString();
        code = gson.fromJson( content, LOINCCodeForm.class );
        assertEquals( form.getComponent(), code.getComponent() );
        content = mvc.perform( get( "/api/v1/loinccode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andReturn().getResponse().getContentAsString();
        code = gson.fromJson( content, LOINCCodeForm.class );
        assertEquals( form.getComponent(), code.getComponent() );

        // then delete it and check that its gone.
        mvc.perform( delete( "/api/v1/loinccode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertNull( LOINCCode.getById( form.getId() ) );
        mvc.perform( get( "/api/v1/loinccode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        // trying to delete something that doesn't exist shouldn't work.
        mvc.perform( delete( "/api/v1/loinccode/" + form.getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );

        // Adding a bad/incomplete form should not work.
        final LOINCCodeForm bad = new LOINCCodeForm();
        mvc.perform( post( "/api/v1/loinccodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( bad ) ) ).andExpect( status().isBadRequest() );
    }

}
