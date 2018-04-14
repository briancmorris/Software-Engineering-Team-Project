package edu.ncsu.csc.itrust2.controllers.api;

import java.util.ArrayList;
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
import edu.ncsu.csc.itrust2.models.persistent.Patient;
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
        String username = id;
        try {
            EmergencyForm form = new EmergencyForm( id );
            // If the patient was not found
            if ( form.getId().equals( "" ) ) {
                // Get all the patients
                final List<Patient> patients = Patient.getPatients();
                // Loop over all the patients
                for ( final Patient p : patients ) {
                    // Create a full name string for the patient
                    final String name = p.getFirstName() + " " + p.getLastName();
                    // Compare the full name to the string
                    if ( name.equals( id ) ) {
                        form = new EmergencyForm( p.getSelf().getId() );
                        username = p.getSelf().getId();

                    }
                }
                // No patient found by id or full name
                if ( form.getId().equals( "" ) ) {
                    return new ResponseEntity( errorResponse( "No patient with name " + id ), HttpStatus.NOT_FOUND );
                }
            }
            // Log that the current user viewed the patients EHR
            LoggerUtil.log( TransactionType.VIEW_EHR, LoggerUtil.currentUser(), username,
                    "EHR viewed for " + username );
            return new ResponseEntity( form, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not retrieve Emergency Health Record for  " + id + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
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

        User user = User.getByName( id );
        if ( null == user ) {
            final List<Patient> patients = Patient.getPatients();
            // Loop over all the patients
            for ( final Patient p : patients ) {
                // Create a full name string for the patient
                final String name = p.getFirstName() + " " + p.getLastName();
                // Compare the full name to the string
                if ( name.equals( id ) ) {
                    user = p.getSelf();
                }
            }
            // If the user still was not found
            if ( null == user ) {
                return null;
            }
        }

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
        User user = User.getByName( id );
        if ( null == user ) {
            final List<Patient> patients = Patient.getPatients();
            // Loop over all the patients
            for ( final Patient p : patients ) {
                // Create a full name string for the patient
                final String name = p.getFirstName() + " " + p.getLastName();
                // Compare the full name to the string
                if ( name.equals( id ) ) {
                    user = p.getSelf();
                }
            }
            // If the user still was not found
            if ( null == user ) {
                return null;
            }
        }
        return Prescription.getForPatient( user.getId() );
    }

    /**
     * Returns a list of patients that match the given ID
     *
     * @param id
     *            The patient ID to search for
     * @return The list of patients
     */
    @GetMapping ( BASE_PATH + "/emergencyHealthRecords/patients" )
    @PreAuthorize ( "hasRole('ROLE_HCP') or hasRole('ROLE_ER')" )
    public List<Patient> getPatients ( @PathVariable ( "id" ) final String id ) {
        final List<Patient> patients = new ArrayList<Patient>();
        final List<Patient> allpatients = Patient.getPatients();
        for ( final Patient p : allpatients ) {
            if ( p.getSelf().getId().equals( id ) ) {
                patients.add( p );
            }
        }
        return patients;
    }

}
