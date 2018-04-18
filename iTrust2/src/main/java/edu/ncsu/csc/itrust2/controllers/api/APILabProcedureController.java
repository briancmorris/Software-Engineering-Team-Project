/**
 * 
 */
package edu.ncsu.csc.itrust2.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.LabProcedure;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints for managing the Lab Procedures.  Functionality
 * includes updating a lab, reassigning a procedure, and viewing a lab.
 * 
 * @author Timothy Figgins
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APILabProcedureController extends APIController {
    
    /**
     * Sets PUT request to update the lab procedure with new comments and/or status.
     * 
     * @param labForm form with new edits
     * @return ResponseEntity new lab or error
     */
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    @PutMapping ( BASE_PATH + "/labProcedures" )
    public ResponseEntity updateLabProcedure(@RequestBody final LabProcedureForm labForm) {
        boolean comment = false; // boolean check to see if comments were updated
        boolean status = false; // boolean check to see if status was updated
        boolean both = false; // boolean check to see if both were updated
        try {
            final LabProcedure lab = new LabProcedure(labForm);
            final LabProcedure saved = LabProcedure.getById( labForm.getID() );
            if (saved == null) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "No lab procedure found with id " + labForm.getID() );
                return new ResponseEntity( errorResponse( "No lab found with id " + labForm.getID() ),
                        HttpStatus.NOT_FOUND );
            }
            
            CompletionStatus oldStatus = saved.getCompletionStatus(); // get old one for logging purposes
            
            if (!saved.getComments().equals( lab.getComments() )) {
                comment = true;
            }
            if (!(saved.getCompletionStatus() == lab.getCompletionStatus())) {
                status = true;
            }
            if (comment && status) {
                both = true;
            }
            
            lab.save(); /* Overwrite existing */
            
            if (both) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "Edited comments and status of lab procedure found with id " + labForm.getID() + 
                        ".  Status updated to " + labForm.getCompletionStatus());
            } else if (status) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "Edited status of lab procedure found with id " + labForm.getID() + " from " + oldStatus +
                         " to " + labForm.getCompletionStatus() );
            } else if (comment) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                        "Edited comments of lab procedure found with id " + labForm.getID());
            }
            return new ResponseEntity( lab, HttpStatus.OK );
        } catch (Exception e) {
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_EDIT, LoggerUtil.currentUser(),
                    "Failed to edit lab" );
            return new ResponseEntity( errorResponse( "Failed to update lab: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
    
    /**
     * Sets PUT request to update the lab procedure with new comments and/or status.
     * 
     * @param labForm form with newly assigned LT
     * @return ResponseEntity new lab or error
     */
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    @PutMapping ( BASE_PATH + "/labProcedures/reassign" )
    public ResponseEntity reassignProcedure(@RequestBody final LabProcedureForm labForm) {
        try {
            final LabProcedure lab = new LabProcedure(labForm);
            final LabProcedure saved = LabProcedure.getById( labForm.getID() );
            if (saved == null) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_REASSIGN, LoggerUtil.currentUser(),
                        "No lab procedure found with id " + labForm.getID() );
                return new ResponseEntity( errorResponse( "No lab found with id " + labForm.getID() ),
                        HttpStatus.NOT_FOUND );
            }
            
            lab.save(); /* Overwrite existing */
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_REASSIGN, LoggerUtil.currentUser(),
                    "Reassigned lab procedure found with id " + labForm.getID() + " to user " + labForm.getLabTech());
            return new ResponseEntity( lab, HttpStatus.OK );
            } catch (Exception e) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_REASSIGN, LoggerUtil.currentUser(),
                        "Failed to reassign lab" );
                return new ResponseEntity( errorResponse( "Failed to reassign lab: " + e.getMessage() ),
                        HttpStatus.BAD_REQUEST );
            }
    }
    
    /**
     * Sets PUT mapping request to add a new lab procedure to an existing office visit. 
     * 
     * @param officeVisitID id of OfficeVisit to add lab to
     * @param labForm id of Lab Procedure to create
     * @return ResponseEntity updated OfficeVisit or error
     */
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    @PutMapping ( BASE_PATH + "/labProcedures/{officeVisitID}" )
    public ResponseEntity addLabProcedure(@PathVariable Long officeVisitID, @RequestBody final LabProcedureForm labForm) {
        try {
            final LabProcedure lab = new LabProcedure(labForm);
            OfficeVisit ov = OfficeVisit.getById( officeVisitID );
            if (ov == null) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_ADD, LoggerUtil.currentUser(),
                        "No office visit found with id " + officeVisitID );
                return new ResponseEntity( errorResponse( "No Office Visit found with id " + officeVisitID ),
                        HttpStatus.NOT_FOUND );
            }
            
            ov.addLabProcedure( lab );
            ov.save();
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_ADD, LoggerUtil.currentUser(),
                    "Added lab procedures to  office visit found with id " + officeVisitID );
            return new ResponseEntity(ov, HttpStatus.OK);
        } catch (Exception e) {
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_ADD, LoggerUtil.currentUser(),
                    "Failed to add lab procedure" );
            return new ResponseEntity( errorResponse( "Failed to add lab: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
    
    /**
     * Sets DELETE mapping request to remove a lab procedure from an office visit.
     * 
     * @param officeVisitID id of office visit to update
     * @param labID id of lab procedure to remove
     * @return ResponseEntity updated office visit or error. 
     */
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    @DeleteMapping ( BASE_PATH + "/labProcedures/{officeVisitID}" )
    public ResponseEntity deleteLabProcedure(@PathVariable Long officeVisitID, @RequestBody final Long labID) {
        try {
            final LabProcedure lab = LabProcedure.getById( labID );
            OfficeVisit ov = OfficeVisit.getById( officeVisitID );
            if (ov == null) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_REMOVE, LoggerUtil.currentUser(),
                        "No office visit found with id " + officeVisitID );
                return new ResponseEntity( errorResponse( "No Office Visit found with id " + officeVisitID ),
                        HttpStatus.NOT_FOUND );
            }
            if (lab == null) {
                LoggerUtil.log( TransactionType.LAB_PROCEDURE_REMOVE, LoggerUtil.currentUser(),
                        "No lab procedure found with id " + labID );
                return new ResponseEntity( errorResponse( "No lab found with id " + labID ),
                        HttpStatus.NOT_FOUND );
            }
            
            ov.removeLabProcedure( lab );
            ov.save();
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_REMOVE, LoggerUtil.currentUser(),
                    "Removed lab procedure with id " + labID + " from office visit found with id " + officeVisitID );
            return new ResponseEntity(ov, HttpStatus.OK);
        } catch (Exception e) {
            LoggerUtil.log( TransactionType.LAB_PROCEDURE_REMOVE, LoggerUtil.currentUser(),
                    "Failed to remove lab procedure" );
            return new ResponseEntity( errorResponse( "Failed to remove lab: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }
}
