/**
 * 
 */
package edu.ncsu.csc.itrust2.controllers.api;

import java.util.Set;

import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.utils.HibernateUtil;
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
    @GetMapping ( BASE_PATH + "/editRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<Patient> getRepresentatives () {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        LoggerUtil.log( TransactionType.VIEW_REP_LIST, p.getSelf() );
        return p.getRepresentatives();
    }
    
    @GetMapping ( BASE_PATH + "/editRepresentatives/myPatients" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<Patient> getRepresentees () {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        if (!p.isRep()) {
            return null;
        }
        LoggerUtil.log( TransactionType.VIEW_REP_PATIENT_LIST, p.getSelf() );
        return p.getRepresentees();
    }
    
    @PostMapping (BASE_PATH + "/editRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity declareRep( @RequestBody final String username  ) {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final Patient rep = Patient.getByName( username );
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            Session sf = HibernateUtil.openSession();
            sf.beginTransaction();
            p.getRepresentatives().add( rep );
            rep.getRepresentees().add( p);
            rep.declareSelfRep();
            sf.save( p );
            sf.save( rep );
            sf.getTransaction().commit();
            sf.close();
        } catch (Exception e) {
            return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.DEC_REP, LoggerUtil.currentUser(), username, p.getSelf().getUsername() + " declared representative: " + username );
        return new ResponseEntity(p, HttpStatus.OK);
    }
    
    @DeleteMapping (BASE_PATH + "/editRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareRep( @RequestBody final String username  ) {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final Patient rep = Patient.getByName( username );
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            Session sf = HibernateUtil.openSession();
            sf.beginTransaction();
            p.getRepresentatives().remove( rep );
            rep.getRepresentees().remove( p);
            if (rep.getRepresentees().isEmpty()) {
                rep.undeclareSelfRep();
            }
            sf.save( p );
            sf.save( rep );
            sf.getTransaction().commit();
            sf.close();
        } catch (Exception e) {
            return new ResponseEntity( errorResponse( "Failed to undeclare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.UNDEC_REP, LoggerUtil.currentUser(), username, p.getSelf().getUsername() + " undeclared representative " + username );
        return new ResponseEntity(p, HttpStatus.OK);
    }
    
    
    @DeleteMapping (BASE_PATH + "/editRepresentatives/removeSelf")
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareSelfRep( @RequestBody final String username ) {
        final Patient rep = Patient.getByName( LoggerUtil.currentUser() );
        final Patient p = Patient.getByName( username );
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            Session sf = HibernateUtil.openSession();
            sf.beginTransaction();
            p.getRepresentatives().remove( rep );
            rep.getRepresentees().remove( p);
            if (rep.getRepresentees().isEmpty()) {
                rep.undeclareSelfRep();
            }
            sf.save( p );
            sf.save( rep );
            sf.getTransaction().commit();
            sf.close();
        } catch (Exception e) {
            return new ResponseEntity( errorResponse( "Failed to undeclare self as representative from " + username),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.UNDEC_SELF_REP, LoggerUtil.currentUser(), username, rep.getSelf().getUsername() + " undeclared self as representative of " + username );
        return new ResponseEntity(p, HttpStatus.OK);
    }
    
    
    @GetMapping (BASE_PATH + "/editRepresentatives/{username}")
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getPatientRepresentatives (@PathVariable ( "username" ) final String username ) {
        final Patient patient = Patient.getByName( username );
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }  else {
            LoggerUtil.log( TransactionType.HCP_VIEW_PATIENT_REP_LIST, LoggerUtil.currentUser(), username,
                    "HCP retrieved representatives for patient with username " + username );
            return new ResponseEntity( patient.getRepresentatives(), HttpStatus.OK );
        }
    }
    
    @PostMapping (BASE_PATH + "/editRepresentatives/{username}")
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity declareRepForPatient( @PathVariable final String username, @RequestBody Patient p ) {
        final Patient patient = Patient.getByName( "username ");
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            Session sf = HibernateUtil.openSession();
            sf.beginTransaction();
            patient.getRepresentatives().add( p );
            p.getRepresentees().add( patient );
            sf.save( patient );
            sf.save( p );
            sf.getTransaction().commit();
            sf.close();
        } catch (Exception e) {
            return new ResponseEntity( errorResponse( "Failed to declare representative for username " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.HCP_DEC_REP, LoggerUtil.currentUser(), username, "HCP declared representative: " + p.getSelf().getUsername() + " for patient with username " + username );
        return new ResponseEntity(patient, HttpStatus.OK);
    }
    
    
}
