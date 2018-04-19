package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.er.EmergencyForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides the REST endpoints for handling EHR information. All
 * three endpoints will be called upon requesting EHRs, however they are
 * separated for easy diagnosis and prescription parsing.
 *
 * @author Garrett Watts
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIEmergencyController extends APIController {

    /**
     * Returns the demographics for a patients EHR
     *
     * @param id
     *            The patient ID to search for
     * @return The patient's demographics
     */
    @GetMapping ( BASE_PATH + "/emergencyHealthRecords/demo/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP') or hasRole('ROLE_ER')" )
    public ResponseEntity getEmergencyDemo ( @PathVariable ( "id" ) final String id ) {
        // Save id as username, may be updated in loop. Used for logging.
        final String username = id;

        final EmergencyForm form = new EmergencyForm( id );

        // Log that the current user viewed the patients EHR
        LoggerUtil.log( TransactionType.VIEW_EHR, LoggerUtil.currentUser(), username, "EHR viewed for " + username );
        return new ResponseEntity( form, HttpStatus.OK );

    }

    /**
     * Returns the diagnoses for a patients EHR
     *
     * @param id
     *            The patient ID to search for
     * @return The patient's diagnoses
     */
    @GetMapping ( BASE_PATH + "/emergencyHealthRecords/diag/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP') or hasRole('ROLE_ER')" )
    public List<Diagnosis> getEmergencyDiag ( @PathVariable ( "id" ) final String id ) {
        final User user = User.getByName( id );
        return Diagnosis.getForPatient( user );
    }

    /**
     * Returns the prescriptions for a patients EHR
     *
     * @param id
     *            The patient ID to search for
     * @return The patient's prescriptions
     */
    @GetMapping ( BASE_PATH + "/emergencyHealthRecords/pres/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP') or hasRole('ROLE_ER')" )
    public List<Prescription> getEmergencyPres ( @PathVariable ( "id" ) final String id ) {
        final User user = User.getByName( id );
        return Prescription.getForPatient( user.getId() );
    }

}
