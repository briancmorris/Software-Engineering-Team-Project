package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides the REST endpoints for handling LOINC Codes. They can be
 * retrieved individually based on id, or all in a list. An Admin can add,
 * remove, or edit them.
 *
 * @author Brian Morris
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APILOINCCodeController extends APIController {

    /**
     * Returns a list of Codes in the system
     *
     * @return All the codes in the system
     */
    @GetMapping ( BASE_PATH + "/loinccodes" )
    public List<LOINCCode> getCodes () {
        LoggerUtil.log( TransactionType.LOINC_VIEW_ALL, LoggerUtil.currentUser(), "Viewed LOINC codes." );
        return LOINCCode.getAll();
    }

    /**
     * Returns the LOINC code with the given ID
     *
     * @param id
     *            The ID of the LOINC code to retrieve
     * @return The requested LOINC code.
     */
    @GetMapping ( BASE_PATH + "/loinccode/{id}" )
    public ResponseEntity getCode ( @PathVariable ( "id" ) final Long id ) {
        try {
            final LOINCCode code = LOINCCode.getById( id );
            if ( code == null ) {
                return new ResponseEntity( errorResponse( "No LOINC code with id " + id ), HttpStatus.NOT_FOUND );
            }
            LoggerUtil.log( TransactionType.LOINC_VIEW, LoggerUtil.currentUser(), "Viewed LOINC code with id " + id );
            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not retrieve LOINC code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates the LOINC code with the specified ID to the value supplied.
     *
     * @param id
     *            The ID of the LOINC code to edit.
     * @param form
     *            The new values for the LOINC code.
     * @return The Response of the action.
     */
    @PutMapping ( BASE_PATH + "/loinccode/{id}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity updateCode ( @PathVariable ( "id" ) final Long id, @RequestBody final LOINCCodeForm form ) {
        try {
            final LOINCCode code = LOINCCode.getById( id );
            if ( code == null ) {
                return new ResponseEntity( "No code with id " + id, HttpStatus.NOT_FOUND );
            }
            form.setId( id );
            final LOINCCode updatedCode = new LOINCCode( form );
            updatedCode.save();
            User user = null;
            try {
                user = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
            }
            catch ( final Exception e ) {
                // ignore, it was a test that wasn't authenticated properly.
            }
            LoggerUtil.log( TransactionType.LOINC_EDIT, user.getUsername(),
                    user.getUsername() + " edited a LOINC code." );

            return new ResponseEntity( updatedCode, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update LOINC code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Adds a new LOINC code to the system.
     *
     * @param form
     *            The data for the new code.
     * @return The result of the action.
     */
    @PostMapping ( BASE_PATH + "/loinccodes" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity addCode ( @RequestBody final LOINCCodeForm form ) {
        try {
            final LOINCCode code = new LOINCCode( form );
            code.save();
            User user = null;
            try {
                user = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
            }
            catch ( final Exception e ) {
                // ignore, it was a test that wasn't authenticated properly.
            }
            LoggerUtil.log( TransactionType.LOINC_CREATE, user.getUsername(),
                    user.getUsername() + " created a LOINC Code" );

            return new ResponseEntity( code, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not create LOINC Code " + form.getCode() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes a LOINC code from the system.
     *
     * @param id
     *            The ID of the LOINC code to delete.
     * @return The result of the action.
     */
    @DeleteMapping ( BASE_PATH + "/loinccode/{id}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity deleteCode ( @PathVariable ( "id" ) final Long id ) {
        try {
            final LOINCCode code = LOINCCode.getById( id );
            code.delete();
            User user = null;
            try {
                user = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
            }
            catch ( final Exception e ) {
                // ignore, it was a test that wasn't authenticated properly.
            }
            LoggerUtil.log( TransactionType.LOINC_DELETE, LoggerUtil.currentUser(),
                    user.getUsername() + " deleted a LOINC code." );

            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not delete LOINC code " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
