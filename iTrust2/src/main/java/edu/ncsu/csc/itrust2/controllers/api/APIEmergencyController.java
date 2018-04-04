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
import edu.ncsu.csc.itrust2.models.persistent.ICDCode;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides the REST endpoints for handling ICD Codes. They can be
 * retrieved individually based on id, or all in a list. An Admin can add,
 * remove, or edit them.
 *
 * @author Thomas
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIEmergencyController extends APIController {

    /**
     * Returns a list of Codes in the system
     *
     * @return All the codes in the system
     */
    @GetMapping ( BASE_PATH + "/icdcodesx" )
    public List<ICDCode> getCodes () {
        LoggerUtil.log( TransactionType.ICD_VIEW_ALL, LoggerUtil.currentUser(), "Fetched icd codes" );
        return ICDCode.getAll();
    }

    /**
     * Returns the code with the given ID
     *
     * @param id
     *            The ID of the code to retrieve
     * @return The requested Code
     */
    @GetMapping ( BASE_PATH + "/emergencyHealthRecords/demo/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP') or hasRole('ROLE_ER')" )
    public ResponseEntity getEmergencyDemo ( @PathVariable ( "id" ) final String id ) {
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
                        if ( form.getId().equals( "" ) ) {
                            return new ResponseEntity( errorResponse( "No patient with name " + id ),
                                    HttpStatus.NOT_FOUND );
                        }
                    }

                }
            }
            LoggerUtil.log( TransactionType.ICD_VIEW, LoggerUtil.currentUser(), "Fetched icd code with id " + id );
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
     * Returns the code with the given ID
     *
     * @param id
     *            The ID of the code to retrieve
     * @return The requested Code
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
        LoggerUtil.log( TransactionType.DIAGNOSIS_PATIENT_VIEW_ALL, user.getUsername(),
                user.getUsername() + " viewed their diagnoses" );

        return Diagnosis.getForPatient( user );
    }

    /**
     * Returns the code with the given ID
     *
     * @param id
     *            The ID of the code to retrieve
     * @return The requested Code
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
        LoggerUtil.log( TransactionType.DIAGNOSIS_PATIENT_VIEW_ALL, user.getUsername(),
                user.getUsername() + " viewed their diagnoses" );

        return Prescription.getForPatient( user.getId() );
    }

}
