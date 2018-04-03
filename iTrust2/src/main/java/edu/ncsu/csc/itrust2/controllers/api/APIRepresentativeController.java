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

import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.models.persistent.User;
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
    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<User> getRepresentatives () {
        final User p = User.getByName( LoggerUtil.currentUser() );
        LoggerUtil.log( TransactionType.VIEW_REP_LIST, p);
        return p.getRepresentatives();
    }

    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/myPatients" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public Set<User> getRepresentees () {
        final User p = User.getByName( LoggerUtil.currentUser() );
        if ( !p.isRep() ) {
            return null;
        }
        LoggerUtil.log( TransactionType.VIEW_REP_PATIENT_LIST, p );
        return p.getRepresentees();
    }

    @PostMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity declareRep ( @RequestBody final String username ) {
        final User p = User.getByName( LoggerUtil.currentUser() );
        final User rep = User.getByNameAndRole( username, Role.ROLE_PATIENT );
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            //final Session sf = HibernateUtil.openSession();
            //sf.beginTransaction();
            p.getRepresentatives().add( rep );
            rep.getRepresentees().add( p );
            rep.declareSelfRep();
            p.save();
            rep.save();
            //sf.save( p );
            //sf.save( rep );
            //sf.getTransaction().commit();
            //sf.close();
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to declare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.DEC_REP, LoggerUtil.currentUser(), username,
                p.getUsername() + " declared representative: " + username );
        return new ResponseEntity( p, HttpStatus.OK );
    }

    @DeleteMapping ( BASE_PATH + "/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareRep ( @RequestBody final String username ) {
        final User p = User.getByName( LoggerUtil.currentUser() );
        final User rep = User.getByNameAndRole( username, Role.ROLE_PATIENT );
        if ( rep == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            //final Session sf = HibernateUtil.openSession();
            //sf.beginTransaction();
            p.getRepresentatives().remove( rep );
            rep.getRepresentees().remove( p );
            if ( rep.getRepresentees().isEmpty() ) {
                rep.undeclareSelfRep();
            }
            p.save();
            rep.save();
            //sf.save( p );
            //sf.save( rep );
            //sf.getTransaction().commit();
            //sf.close();
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to undeclare representative " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.UNDEC_REP, LoggerUtil.currentUser(), username,
                p.getUsername() + " undeclared representative " + username );
        return new ResponseEntity( p, HttpStatus.OK );
    }

    @DeleteMapping ( BASE_PATH + "/editPersonalRepresentatives/removeSelf" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity undelcareSelfRep ( @RequestBody final String username ) {
        final User rep = User.getByName( LoggerUtil.currentUser() );
        final User p = User.getByName( username );
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            //final Session sf = HibernateUtil.openSession();
            //sf.beginTransaction();
            p.getRepresentatives().remove( rep );
            rep.getRepresentees().remove( p );
            if ( rep.getRepresentees().isEmpty() ) {
                rep.undeclareSelfRep();
            }
            p.save();
            rep.save();
            //sf.save( p );
            //sf.save( rep );
            //sf.getTransaction().commit();
            //sf.close();
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to undeclare self as representative from " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.UNDEC_SELF_REP, LoggerUtil.currentUser(), username,
                rep.getUsername() + " undeclared self as representative of " + username );
        return new ResponseEntity( p, HttpStatus.OK );
    }

    @GetMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getPatientRepresentatives ( @PathVariable ( "username" ) final String username ) {
        final User patient = User.getByNameAndRole( username, Role.ROLE_PATIENT );
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            LoggerUtil.log( TransactionType.HCP_VIEW_PATIENT_REP_LIST, LoggerUtil.currentUser(), username,
                    "HCP retrieved representatives for patient with username " + username );
            return new ResponseEntity( patient.getRepresentatives(), HttpStatus.OK );
        }
    }

    @PostMapping ( BASE_PATH + "/editPersonalRepresentatives/{username}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity declareRepForPatient ( @PathVariable final String username, @RequestBody User p ) {
        final User patient = User.getByNameAndRole( username, Role.ROLE_PATIENT );
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found for username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            //final Session sf = HibernateUtil.openSession();
            //sf.beginTransaction();
            patient.getRepresentatives().add( p );
            p.getRepresentees().add( patient );
            patient.save();
            p.save();
            //sf.save( patient );
            //sf.save( p );
            //sf.getTransaction().commit();
            //sf.close();
        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Failed to declare representative for username " + username ),
                    HttpStatus.BAD_REQUEST );
        }
        LoggerUtil.log( TransactionType.HCP_DEC_REP, LoggerUtil.currentUser(), username, "HCP declared representative: "
                + p.getUsername() + " for patient with username " + username );
        return new ResponseEntity( patient, HttpStatus.OK );
    }

}
