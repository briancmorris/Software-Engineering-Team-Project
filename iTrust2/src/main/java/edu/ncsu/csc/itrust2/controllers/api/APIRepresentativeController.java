/**
 * 
 */
package edu.ncsu.csc.itrust2.controllers.api;

import java.util.Set;

import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping ( BASE_PATH + "/representatives" )
    public Set<Patient> getRepresentatives () {
        // TODO
        return null;
    }
    
    @GetMapping (BASE_PATH + "/representatives{username}")
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getRepresentatives (@PathVariable ( "username" ) final String username ) {
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
    
    @PostMapping (BASE_PATH + "/representatives{username}")
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
