/**
 *
 */
package edu.ncsu.csc.itrust2.controllers.api;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * API class to handle requests for the patient and HCP representative pages
 *
 * @author Timothy Figgins
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIRepresentativeController extends APIController {

    /**
     * Handles GET request for pulling up the list of the currently logged in
     * patient's list of representatives.
     *
     * @return p.getRepresentatives() the list of patient representatives.
     */
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<Patient> getRepresentatives () {
        Patient p = Patient.getByName( LoggerUtil.currentUser() );
        // If Patient object does not exist create it.
        if ( p == null ) {
            p = new Patient( User.getByName( LoggerUtil.currentUser() ) );
        }
        // For loop to stop JSON recursion
        for ( final Patient represent : p.getRepresentatives() ) {
            represent.setPersonalRepresentatives( new HashSet<Patient>() );
            represent.setPersonalRepresentees( new HashSet<Patient>() );
        }
        LoggerUtil.log( TransactionType.VIEW_REP_LIST, p.getSelf() );
        return p.getRepresentatives();
    }

    /**
     * Handles GET request for currently logged in patient's list of
     * representees, aka the people that the current patient represents.
     *
     * @return p.getRepresentees() list of current patient's representees
     */
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/myPatients" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<Patient> getRepresentees () {
        Patient p = Patient.getByName( LoggerUtil.currentUser() );
        // If patient object does not exist create it.
        if ( p == null ) {
            p = new Patient( User.getByName( LoggerUtil.currentUser() ) );
        }
        // If patient is not a representative just return a blank set.
        if ( !p.isRep() ) {
            return new HashSet<Patient>();
        }
        // For loop for JSON recursion
        for ( final Patient patients : p.getRepresentees() ) {
            patients.setPersonalRepresentatives( new HashSet<Patient>() );
            patients.setPersonalRepresentees( new HashSet<Patient>() );
        }
        LoggerUtil.log( TransactionType.VIEW_REP_PATIENT_LIST, p.getSelf() );
        return p.getRepresentees();
    }

    /**
     * Handles POST request for the current user adding a personal
     * representative.
     *
     * @param username
     *            name of the rep being added.
     * @return ReponseEntity with an error or the current patient object.
     */
    @PostMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity declareRep ( @RequestBody final String username ) {
        Patient p = Patient.getByName( LoggerUtil.currentUser() );
        // If patient object does not exist create it.
        if ( p == null ) {
            p = new Patient( User.getByName( LoggerUtil.currentUser() ) );
        }
        Patient rep = Patient.getByName( username );
        // If patient object for rep does not exist create it.
        if ( rep == null ) {
            rep = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        // If rep cannot be created because the User could not be found, return
        // an error.
        if ( rep.getSelf() == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            
            // Check for duplicates
            if (p.equals(rep)) {
                return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                        HttpStatus.BAD_REQUEST );
            }
            for (final Patient representatives: p.getRepresentatives()) {
                if (representatives.equals( rep )) {
                    return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                            HttpStatus.BAD_REQUEST );
                }
            }
            
            // Create temp sets to hold info
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();

            // Add reps and representees to their respective temp sets
            tempReps.addAll( p.getRepresentatives() );
            tempReps.add( rep ); // add new rep
            tempPatients.addAll( rep.getRepresentees() );
            tempPatients.add( p ); // add patient to new rep list

            // Clear lists
            p.getRepresentatives().clear();
            rep.getRepresentees().clear();
            p.save();
            rep.save();

            // Re add everything
            for ( final Patient represent : tempReps ) {
                p.getRepresentatives().add( represent );
            }
            for ( final Patient patients : tempPatients ) {
                rep.getRepresentees().add( patients );
            }
            p.save();
            rep.declareSelfRep();
            rep.save();

            // For loop for JSON recursion
            for ( final Patient represent : p.getRepresentatives() ) {
                represent.setPersonalRepresentatives( new HashSet<Patient>() );
                represent.setPersonalRepresentees( new HashSet<Patient>() );
            }
            LoggerUtil.log( TransactionType.DEC_REP, LoggerUtil.currentUser(), username,
                    p.getSelf().getUsername() + " declared representative: " + username );
            return new ResponseEntity( p, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Handles DELETE request for the current logged in patient removing a
     * representative.
     *
     * @param username
     *            name of representative
     * @return ResponseEntity error or currently logged in patient object
     */
    @DeleteMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareRep ( @PathVariable ( "username" ) final String username ) {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final Patient rep = Patient.getByName( username );
        // Handles the case where the rep can't be found for whatever reason.
        // Hopefully should never occur.
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            // Create temp sets to hold info
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();

            // Add all representatives to temp set
            tempReps.addAll( p.getRepresentatives() );
            // Find the rep that needs to be removed
            for ( final Patient r : tempReps ) {
                if ( r.equals( rep ) ) {
                    tempReps.remove( r );
                    break;
                }
            }
            // add all representees to temp set
            tempPatients.addAll( rep.getRepresentees() );
            // Find the patient that needs to be removed
            for ( final Patient patient : tempPatients ) {
                if ( patient.equals( p ) ) {
                    tempPatients.remove( patient );
                    break;
                }
            }

            // Clear sets
            p.getRepresentatives().clear();
            rep.getRepresentees().clear();
            p.save();
            rep.save();

            // Add patients and reps back
            for ( final Patient represent : tempReps ) {
                p.getRepresentatives().add( represent );
            }
            for ( final Patient patients : tempPatients ) {
                rep.getRepresentees().add( patients );
            }
            p.save();
            if ( rep.getRepresentees().isEmpty() ) {
                rep.undeclareSelfRep(); // If no more representees, no longer a
                                        // representative
            }
            rep.save();
            for ( final Patient represent : p.getRepresentatives() ) {
                represent.setPersonalRepresentatives( new HashSet<Patient>() );
                represent.setPersonalRepresentees( new HashSet<Patient>() );
            }

            LoggerUtil.log( TransactionType.UNDEC_REP, LoggerUtil.currentUser(), username,
                    p.getSelf().getUsername() + " undeclared representative " + username );
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to undeclare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Handles DELETE request for removing oneself as a representative from a
     * patient.
     *
     * @param username
     *            of patient that is being represented
     * @return ResponseEntity error or patient object
     */
    @DeleteMapping ( BASE_PATH + "/editPersonalRepresentatives/removeSelf/{username}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareSelfRep ( @PathVariable ( "username" ) final String username ) {
        // The rep is now the user in this case
        final Patient rep = Patient.getByName( LoggerUtil.currentUser() );
        final Patient p = Patient.getByName( username );
        // Handles the patient not being found, hopefully never happens.
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            // Create temp sets for data
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();

            // Add all the reps to the temp sets
            tempReps.addAll( p.getRepresentatives() );
            // Find the rep that needs to be removed.
            for ( final Patient r : tempReps ) {
                if ( r.equals( rep ) ) {
                    tempReps.remove( r );
                    break;
                }
            }
            // Add all the patients to the temp sets
            tempPatients.addAll( rep.getRepresentees() );
            // Find the patient that needs to removed
            for ( final Patient patient : tempPatients ) {
                if ( patient.equals( p ) ) {
                    tempPatients.remove( patient );
                    break;
                }
            }

            // Clear sets
            p.getRepresentatives().clear();
            rep.getRepresentees().clear();
            p.save();
            rep.save();

            // Add patients and reps back
            for ( final Patient represent : tempReps ) {
                p.getRepresentatives().add( represent );
            }
            for ( final Patient patients : tempPatients ) {
                rep.getRepresentees().add( patients );
            }
            p.save();
            if ( rep.getRepresentees().isEmpty() ) {
                rep.undeclareSelfRep();
            }
            rep.save();

            // For loop for JSON recursion
            for ( final Patient represent : p.getRepresentatives() ) {
                represent.setPersonalRepresentatives( new HashSet<Patient>() );
                represent.setPersonalRepresentees( new HashSet<Patient>() );
            }
            LoggerUtil.log( TransactionType.UNDEC_SELF_REP, LoggerUtil.currentUser(), username,
                    rep.getSelf().getUsername() + " undeclared self as representative of " + username );
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to undeclare self as representative from " + username ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Handles GET request for an HCP to get the list of a patient's
     * representatives
     *
     * @param username
     *            of patient
     * @return ReponseEntity list of patient representatives or error
     */
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getPatientRepresentatives ( @PathVariable ( "username" ) final String username ) {
        Patient patient = Patient.getByName( username );
        if ( patient == null ) {
            patient = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        if ( patient.getSelf() == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            for ( final Patient represent : patient.getRepresentatives() ) {
                represent.setPersonalRepresentatives( new HashSet<Patient>() );
                represent.setPersonalRepresentees( new HashSet<Patient>() );
            }
            LoggerUtil.log( TransactionType.HCP_VIEW_PATIENT_REP_LIST, LoggerUtil.currentUser(), username,
                    "HCP retrieved representatives for patient with username " + username );
            return new ResponseEntity( patient.getRepresentatives(), HttpStatus.OK );
        }
    }

    /**
     * Handles GET request for an HCP to get the list of a patient's
     * representees
     *
     * @param username
     *            of patient
     * @return ReponseEntity list of patient representees or error
     */
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/patientRepresentees/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getPatientRepresentees ( @PathVariable ( "username" ) final String username ) {
        Patient patient = Patient.getByName( username );
        if ( patient == null ) {
            patient = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        if ( patient.getSelf() == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            for ( final Patient p : patient.getRepresentees() ) {
                p.setPersonalRepresentatives( new HashSet<Patient>() );
                p.setPersonalRepresentees( new HashSet<Patient>() );
            }
            LoggerUtil.log( TransactionType.HCP_VIEW_PATIENT_REP_LIST, LoggerUtil.currentUser(), username,
                    "HCP retrieved representees for patient with username " + username );
            return new ResponseEntity( patient.getRepresentees(), HttpStatus.OK );
        }
    }

    /**
     * Handles POST request for an HCP to add a representative for a patient.
     *
     * @param username
     *            name of patient
     * @param representative
     *            name of representative
     * @return ResponseEntity patient object or error
     */
    @PostMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity declareRepForPatient ( @PathVariable final String username,
            @RequestBody String representative ) {
        Patient patient = Patient.getByName( username );
        if ( patient == null ) {
            patient = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        Patient rep = Patient.getByName( representative );
        if ( rep == null ) {
            rep = new Patient( User.getByNameAndRole( representative, Role.ROLE_PATIENT ) );
        }
        if ( patient.getSelf() == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        if ( rep.getSelf() == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + representative ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            
            // Check for duplicates
            if (patient.equals(rep)) {
                return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                        HttpStatus.BAD_REQUEST );
            }
            for (final Patient representatives: patient.getRepresentatives()) {
                if (representatives.equals( rep )) {
                    return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                            HttpStatus.BAD_REQUEST );
                }
            }
            
            // Create temp sets
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();

            // Add all representatives including new rep to tempRep set
            tempReps.addAll( patient.getRepresentatives() );
            tempReps.add( rep );
            // Add all representees including patient to tempPatients set
            tempPatients.addAll( rep.getRepresentees() );
            tempPatients.add( patient );

            // Clear sets
            patient.getRepresentatives().clear();
            rep.getRepresentees().clear();
            patient.save();
            rep.save();

            // Add reps and patients back to respective sets
            for ( final Patient represent : tempReps ) {
                patient.getRepresentatives().add( represent );
            }
            for ( final Patient patients : tempPatients ) {
                rep.getRepresentees().add( patients );
            }
            patient.save();
            rep.declareSelfRep(); // set boolean for isRepresentative to true
            rep.save();
            
            for ( final Patient p : patient.getRepresentatives() ) {
                p.setPersonalRepresentatives( new HashSet<Patient>() );
                p.setPersonalRepresentees( new HashSet<Patient>() );
            }
            
            LoggerUtil.log( TransactionType.HCP_DEC_REP, LoggerUtil.currentUser(), username,
                    "HCP declared representative: " + rep.getSelf().getUsername() + " for patient with username "
                            + username );
            return new ResponseEntity( patient, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to declare representative for username " + username ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
