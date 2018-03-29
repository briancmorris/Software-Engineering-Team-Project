package edu.ncsu.csc.itrust2.controllers.api;

import java.util.HashSet;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Controller for personal representatives, used to add and edit the lists of
 * personal representatives a patient has.
 *
 * @author Brian Morris
 *
 */
@Controller
public class APIPersonalRepresentativeController extends APIController {

    /**
     * Adds a personal representative to the representative list of the
     * currently logged in patient. The representative must be a valid patient
     * in the system.
     *
     * @param repID
     *            The ID of the representative to add to the currently logged in
     *            patient's representative list.
     */
    @PostMapping ( BASE_PATH + "/patient/addRepresentative/{repID}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public void addRepresentativePatient ( @PathVariable ( "repID" ) final String repID ) {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final Patient rep = Patient.getByName( repID );
        p.addToRepresentatives( rep );
        rep.addToRepresent( p );
    }

    /**
     * Removes a personal representative from the list representatives for the
     * currently logged in patient.
     *
     * @param repID
     *            The ID of the representative to remove.
     */
    @PostMapping ( BASE_PATH + "/patient/removeRepresentative/{repID}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public void removeRepresentativePatient ( @PathVariable ( "repID" ) final String repID ) {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final Patient rep = Patient.getByName( repID );
        p.removeFromRepresentatives( rep );
        rep.removeFromRepresent( p );
    }

    /**
     * Removes currently logged in patient from the list of the patient with the
     * specified ID.
     *
     * @param patientID
     *            The ID of the patient to remove self from.
     */
    @PostMapping ( BASE_PATH + "/patient/removeRepresentative/{patientID}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public void removeSelf ( @PathVariable ( "patientID" ) final String patientID ) {
        final Patient self = Patient.getByName( LoggerUtil.currentUser() );
        final Patient p = Patient.getByName( patientID );
        p.removeFromRepresentatives( self );
        self.removeFromRepresent( p );
    }

    /**
     * Retrieves the set of representatives for the currently logged in patient.
     *
     * @return The set of representatives.
     */
    @GetMapping ( BASE_PATH + "/patient/getRepresentatives/" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public HashSet<Patient> getRepresentativesPatient () {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final HashSet<Patient> representatives = p.getRepresentatives();

        return representatives;
    }

    /**
     * Retrieves the set of patients that the currently logged in user
     * represents.
     *
     * @return The set of patients that the currently logged in user represents.
     */
    @GetMapping ( BASE_PATH + "/patient/getRepresent/" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public HashSet<Patient> getRepresentPatient () {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final HashSet<Patient> represent = p.getRepresent();

        return represent;
    }

    /**
     * Adds a personal representative to the representative list of the
     * specified patient. The representative must be a valid patient in the
     * system.
     *
     * @param patientID
     *            The ID of the patient that needs a personal representative.
     * @param repID
     *            The ID of the representative to add to the specified patient's
     *            representative list.
     */
    @PostMapping ( BASE_PATH + "/hcp/addRepresentative/{patientID}/{repID}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public void addRepresentativeHCP ( @PathVariable ( "patientID" ) final String patientID,
            @PathVariable ( "repID" ) final String repID ) {
        final Patient p = Patient.getByName( patientID );
        final Patient rep = Patient.getByName( repID );

        p.addToRepresentatives( rep );
        rep.addToRepresent( p );
    }

    /**
     * Retrieves the set of representatives for the specified patient.
     *
     * @param patientID
     *            The ID of the patient to get the set of representatives for.
     * @return The set of representatives for the specified patient..
     */
    @GetMapping ( BASE_PATH + "/hcp/getRepresentatives/{patientID}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public HashSet<Patient> getRepresentativesHCP ( @PathVariable ( "patientID" ) final String patientID ) {
        final Patient p = Patient.getByName( patientID );
        final HashSet<Patient> representatives = p.getRepresentatives();

        return representatives;
    }

    /**
     * Retrieves the set of patients that the specified patient represents.
     *
     * @param patientID
     *            The ID of the patient that represents other patients.
     * @return The set of patients that the specified patient represents.
     *
     */
    @GetMapping ( BASE_PATH + "/hcp/getRepresent/{patientID}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public HashSet<Patient> getRepresentHCP ( @PathVariable ( "patientID" ) final String patientID ) {
        final Patient p = Patient.getByName( patientID );
        final HashSet<Patient> represent = p.getRepresent();

        return represent;
    }
}
