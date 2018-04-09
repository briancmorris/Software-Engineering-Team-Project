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
 * @author Timothy Figgins
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIRepresentativeController extends APIController {

    /**
     * TODO
     *
     * @return NULL
     */
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<Patient> getRepresentatives () {
        Patient p = Patient.getByName( LoggerUtil.currentUser() );
        if ( p == null ) {
            p = new Patient( User.getByName( LoggerUtil.currentUser() ) );
        }
        for ( final Patient represent : p.getRepresentatives() ) {
            represent.setPersonalRepresentatives( new HashSet<Patient>() );
            represent.setPersonalRepresentees( new HashSet<Patient>() );
        }
        LoggerUtil.log( TransactionType.VIEW_REP_LIST, p.getSelf() );
        return p.getRepresentatives();
    }

    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/myPatients" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<Patient> getRepresentees () {
        Patient p = Patient.getByName( LoggerUtil.currentUser() );
        if ( p == null ) {
            p = new Patient( User.getByName( LoggerUtil.currentUser() ) );
        }
        if ( !p.isRep() ) {
            return new HashSet<Patient>();
        }
        for ( final Patient patients : p.getRepresentees() ) {
            patients.setPersonalRepresentatives( new HashSet<Patient>() );
            patients.setPersonalRepresentees( new HashSet<Patient>() );
        }
        LoggerUtil.log( TransactionType.VIEW_REP_PATIENT_LIST, p.getSelf() );
        return p.getRepresentees();
    }

    @SuppressWarnings ( "unused" )
    @PostMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity declareRep ( @RequestBody final String username ) {
        Patient p = Patient.getByName( LoggerUtil.currentUser() );
        if ( p == null ) {
            p = new Patient( User.getByName( LoggerUtil.currentUser() ) );
        }
        Patient rep = Patient.getByName( username );
        if ( rep == null ) {
            rep = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            // final Session sf = HibernateUtil.openSession();
            // sf.beginTransaction();
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();
            tempReps.addAll( p.getRepresentatives() );
            // tempReps = p.getRepresentatives();
            tempReps.add( rep );
            tempPatients.addAll( rep.getRepresentees() );
            tempPatients.add( p );
            p.getRepresentatives().clear();
            rep.getRepresentees().clear();
            // rep.declareSelfRep();
            p.save();
            rep.save();
            for ( final Patient represent : tempReps ) {
                p.getRepresentatives().add( represent );
            }
            for ( final Patient patients : tempPatients ) {
                rep.getRepresentees().add( patients );
            }
            p.save();
            rep.declareSelfRep();
            rep.save();
            // sf.save( p );
            // sf.save( rep );
            // sf.getTransaction().commit();
            // sf.close();
            for ( final Patient represent : p.getRepresentatives() ) {
                represent.setPersonalRepresentatives( new HashSet<Patient>() );
                represent.setPersonalRepresentees( new HashSet<Patient>() );
            }
            LoggerUtil.log( TransactionType.DEC_REP, LoggerUtil.currentUser(), username,
                    p.getSelf().getUsername() + " declared representative: " + username );
            return new ResponseEntity( p, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            System.out.println( "fail" );
            return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        /**
         * LoggerUtil.log( TransactionType.DEC_REP, LoggerUtil.currentUser(),
         * username, p.getUsername() + " declared representative: " + username
         * ); return new ResponseEntity( p, HttpStatus.OK );
         */
    }

    @SuppressWarnings ( "unused" )
    @DeleteMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareRep ( @PathVariable ( "username" ) final String username ) {
        System.out.println( username );
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        /**
         * if ( p == null ) { p = new Patient(User.getByName(
         * LoggerUtil.currentUser() )); }
         */
        final Patient rep = Patient.getByName( username );
        /**
         * if (rep == null) { rep = new Patient(User.getByNameAndRole( username,
         * Role.ROLE_PATIENT )); }
         */
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            // final Session sf = HibernateUtil.openSession();
            // sf.beginTransaction();
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();
            tempReps.addAll( p.getRepresentatives() );
            // tempReps = p.getRepresentatives();
            for ( final Patient r : tempReps ) {
                if ( r.equals( rep ) ) {
                    tempReps.remove( r );
                }
            }
            // tempReps.remove( rep );
            tempPatients.addAll( rep.getRepresentees() );
            for ( final Patient patient : tempPatients ) {
                if ( patient.equals( p ) ) {
                    tempPatients.remove( patient );
                }
            }
            p.getRepresentatives().clear();
            rep.getRepresentees().clear();
            // rep.declareSelfRep();
            p.save();
            rep.save();
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
            // sf.save( p );
            // sf.save( rep );
            // sf.getTransaction().commit();
            // sf.close();
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
        /**
         * LoggerUtil.log( TransactionType.UNDEC_REP, LoggerUtil.currentUser(),
         * username, p.getSelf().getUsername() + " undeclared representative " +
         * username ); return new ResponseEntity( p, HttpStatus.OK );
         */
    }

    @SuppressWarnings ( "unused" )
    @DeleteMapping ( BASE_PATH + "/editPersonalRepresentatives/removeSelf/{username}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareSelfRep ( @PathVariable ( "username" ) final String username ) {
        final Patient rep = Patient.getByName( LoggerUtil.currentUser() );
        /**
         * if ( rep == null ) { rep = new Patient(User.getByName(
         * LoggerUtil.currentUser() )); }
         */
        final Patient p = Patient.getByName( username );
        /**
         * if ( p == null ) { p = new Patient(User.getByNameAndRole( username,
         * Role.ROLE_PATIENT)); }
         */
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            // final Session sf = HibernateUtil.openSession();
            // sf.beginTransaction();
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();
            tempReps.addAll( p.getRepresentatives() );
            // tempReps = p.getRepresentatives();
            for ( final Patient r : tempReps ) {
                if ( r.equals( rep ) ) {
                    tempReps.remove( r );
                }
            }
            // tempReps.remove( rep );
            tempPatients.addAll( rep.getRepresentees() );
            for ( final Patient patient : tempPatients ) {
                if ( patient.equals( p ) ) {
                    tempPatients.remove( patient );
                }
            }
            p.getRepresentatives().clear();
            rep.getRepresentees().clear();
            // rep.declareSelfRep();
            p.save();
            rep.save();
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
            for ( final Patient represent : p.getRepresentatives() ) {
                represent.setPersonalRepresentatives( new HashSet<Patient>() );
                represent.setPersonalRepresentees( new HashSet<Patient>() );
            }
            // sf.save( p );
            // sf.save( rep );
            // sf.getTransaction().commit();
            // sf.close();
            LoggerUtil.log( TransactionType.UNDEC_SELF_REP, LoggerUtil.currentUser(), username,
                    rep.getSelf().getUsername() + " undeclared self as representative of " + username );
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to undeclare self as representative from " + username ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    @SuppressWarnings ( "unused" )
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getPatientRepresentatives ( @PathVariable ( "username" ) final String username ) {
        Patient patient = Patient.getByName( username );
        if ( patient == null ) {
            patient = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        if ( patient == null ) {
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

    @SuppressWarnings ( "unused" )
    @PostMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity declareRepForPatient ( @PathVariable final String username, @RequestBody Patient rep ) {
        Patient patient = Patient.getByName( username );
        if ( patient == null ) {
            patient = new Patient( User.getByNameAndRole( username, Role.ROLE_PATIENT ) );
        }
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            // final Session sf = HibernateUtil.openSession();
            // sf.beginTransaction();
            final Set<Patient> tempReps = new HashSet<Patient>();
            final Set<Patient> tempPatients = new HashSet<Patient>();
            tempReps.addAll( patient.getRepresentatives() );
            // tempReps = p.getRepresentatives();
            tempReps.add( rep );
            tempPatients.addAll( rep.getRepresentees() );
            tempPatients.add( patient );
            patient.getRepresentatives().clear();
            rep.getRepresentees().clear();
            // rep.declareSelfRep();
            patient.save();
            rep.save();
            for ( final Patient represent : tempReps ) {
                patient.getRepresentatives().add( represent );
            }
            for ( final Patient patients : tempPatients ) {
                rep.getRepresentees().add( patients );
            }
            patient.save();
            rep.declareSelfRep();
            rep.save();
            // sf.save( patient );
            // sf.save( p );
            // sf.getTransaction().commit();
            // sf.close();
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
