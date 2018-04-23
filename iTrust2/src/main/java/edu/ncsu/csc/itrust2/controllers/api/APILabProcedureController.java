/**
 *
 */
package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.LabProcedure;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints for managing the Lab Procedures. Functionality
 * includes updating a lab, reassigning a procedure, and viewing a lab.
 *
 * @author Timothy Figgins
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APILabProcedureController extends APIController {

    /**
     * Sets PUT request to update the lab procedure with new comments and/or
     * status.
     *
     * @param labForm
     *            form with new edits
     * @return ResponseEntity new lab or error
     */
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    @PutMapping ( BASE_PATH + "/labProcedures" )
    public ResponseEntity updateLabProcedure ( @RequestBody final LabProcedureForm labForm ) {
        boolean comment = false; // boolean check to see if comments were
                                 // updated
        boolean status = false; // boolean check to see if status was updated
        boolean both = false; // boolean check to see if both were updated
        try {
            final LabProcedure lab = new LabProcedure( labForm );
            final LabProcedure saved = LabProcedure.getById( labForm.getID() );
            if ( saved == null ) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "No lab procedure found with id " + labForm.getID() );
                return new ResponseEntity( errorResponse( "No lab found with id " + labForm.getID() ),
                        HttpStatus.NOT_FOUND );
            }

            final CompletionStatus oldStatus = saved.getCompletionStatus(); // get
                                                                            // old
                                                                            // one
                                                                            // for
                                                                            // logging
                                                                            // purposes

            if ( !saved.getComments().equals( lab.getComments() ) ) {
                comment = true;
            }
            if ( !saved.getCompletionStatus().equals( lab.getCompletionStatus() ) ) {
                status = true;
            }
            if ( comment && status ) {
                both = true;
            }

            lab.save(); /* Overwrite existing */

            if ( both ) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "Edited comments and status of lab procedure found with id " + labForm.getID()
                                + ".  Status updated to " + labForm.getCompletionStatus() );
            }
            else if ( status ) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "Edited status of lab procedure found with id " + labForm.getID() + " from " + oldStatus
                                + " to " + labForm.getCompletionStatus() );
            }
            else if ( comment ) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "Edited comments of lab procedure found with id " + labForm.getID() );
            }
            return new ResponseEntity( lab, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(), "Failed to edit lab" );
            return new ResponseEntity( errorResponse( "Failed to update lab: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Sets PUT request to update the lab procedure with new comments and/or
     * status.
     *
     * @param labForm
     *            form with newly assigned LT
     * @return ResponseEntity new lab or error
     */
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    @PutMapping ( BASE_PATH + "/labProcedures/reassign" )
    public ResponseEntity reassignProcedure ( @RequestBody final LabProcedureForm labForm ) {
        try {
            final LabProcedure lab = new LabProcedure( labForm );
            final LabProcedure saved = LabProcedure.getById( labForm.getID() );
            if ( saved == null ) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_REASSIGN, LoggerUtil.currentUser(),
                        "No lab procedure found with id " + labForm.getID() );
                return new ResponseEntity( errorResponse( "No lab found with id " + labForm.getID() ),
                        HttpStatus.NOT_FOUND );
            }

            lab.save(); /* Overwrite existing */
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_REASSIGN, LoggerUtil.currentUser(),
                    "Reassigned lab procedure found with id " + labForm.getID() + " to user " + labForm.getLabTech() );
            return new ResponseEntity( lab, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_REASSIGN, LoggerUtil.currentUser(),
                    "Failed to reassign lab" );
            return new ResponseEntity( errorResponse( "Failed to reassign lab: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns the LabProcedure with the specified ID.
     *
     * @param id
     *            The id of the lab procedure to be retrieved
     * @return Response Entity containing the lab procedure if it exists
     */
    @GetMapping ( BASE_PATH + "/labProcedure/{id}" )
    public ResponseEntity getLabProcedure ( @PathVariable ( "id" ) final Long id ) {
        final LabProcedure lab = LabProcedure.getById( id );
        LoggerUtil.log( TransactionType.LAB_PROCEDURE_VIEW, LoggerUtil.currentUser(),
                "Retrieved diagnosis with id " + id );
        return null == lab
                ? new ResponseEntity( errorResponse( "No LabProcedure found for id " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( lab, HttpStatus.OK );
    }

    /**
     * Returns a list of lab procedures for a specified office visit
     *
     * @param id
     *            The ID of the office visit to get lab procedures for
     * @return List of LabProcedure objects for the given visit
     */
    @GetMapping ( BASE_PATH + "/labsforvisit/{id}" )
    public List<LabProcedure> getLabsForVisit ( @PathVariable ( "id" ) final Long id ) {
        // Check if office visit exists
        if ( OfficeVisit.getById( id ) == null ) {
            return null;
        }
        LoggerUtil.log( TransactionType.LAB_PROCEDURE_VIEW, LoggerUtil.currentUser(),
                OfficeVisit.getById( id ).getPatient().getUsername(),
                "Retrieved lab procedures for office visit with id " + id );
        return LabProcedure.getByVisit( id );
    }

    /**
     * Returns a list of lab procedures for the logged in lab tech.
     *
     * @return List of lab procedures for the lab tech.
     */
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    @GetMapping ( BASE_PATH + "/labProcedures" )
    public List<LabProcedure> getLabProcedures () {
        final User self = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
        if ( self == null ) {
            return null;
        }
        LoggerUtil.log( TransactionType.LAB_PROCEDURE_VIEW, self.getUsername(),
                self.getUsername() + " viewed their lab procedures" );

        // return LabProcedure.getForLT( self );
        return LabProcedure.getForLT( self );

    }

    /**
     * Returns a list of lab procedures for the logged in lab tech.
     *
     * @return List of lab procedures for the lab tech.
     */
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    @GetMapping ( BASE_PATH + "/status" )
    public List<CompletionStatus> getStatus () {
        return LabProcedure.getStatus();
    }

    /**
     * Retrieves and returns a list of all lab techs stored in the system.
     *
     * @return list of lab techs.
     */
    @GetMapping ( BASE_PATH + "/labtechs" )
    public List<User> getLabTechs () {
        return User.getByRole( Role.ROLE_LT );
    }

}
