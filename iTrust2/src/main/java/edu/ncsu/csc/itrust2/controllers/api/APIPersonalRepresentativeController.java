package edu.ncsu.csc.itrust2.controllers.api;

import java.util.ArrayList;
import java.util.List;

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
        p.addRepresentaive( rep );
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
        p.removeRepresentaive( rep );
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
        p.removeRepresentaive( self );
    }

    /**
     * Retrieves the list of representatives for the currently logged in
     * patient.
     *
     * @return The list of representatives.
     */
    @GetMapping ( BASE_PATH + "/patient/getRepresentatives/" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public List<Patient> getRepresentativesPatient () {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final ArrayList<Patient> representatives = p.getRepresentatives();

        return representatives;
    }

    /**
     * Retrieves the list of patients that the currently logged in user
     * represents.
     *
     * @return The list of patients that the currently logged in user
     *         represents.
     */
    @GetMapping ( BASE_PATH + "/patient/getRepresent/" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public List<Patient> getRepresentPatient () {
        final Patient p = Patient.getByName( LoggerUtil.currentUser() );
        final ArrayList<Patient> represent = p.getRepresent();

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

        p.addRepresentaive( rep );
    }

    /**
     * Retrieves the list of representatives for the specified patient.
     *
     * @param patientID
     *            The ID of the patient to get the list of representatives for.
     * @return The list of representatives for the specified patient..
     */
    @GetMapping ( BASE_PATH + "/hcp/getRepresentatives/{patientID}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public List<Patient> getRepresentativesHCP ( @PathVariable ( "patientID" ) final String patientID ) {
        final Patient p = Patient.getByName( patientID );
        final ArrayList<Patient> representatives = p.getRepresentatives();

        return representatives;
    }

    /**
     * Retrieves the list of patients that the specified patient represents.
     *
     * @param patientID
     *            The ID of the patient that represents other patients.
     * @return The list of patients that the specified patient represents.
     *
     */
    @GetMapping ( BASE_PATH + "/hcp/getRepresent/{patientID}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public List<Patient> getRepresentHCP ( @PathVariable ( "patientID" ) final String patientID ) {
        final Patient p = Patient.getByName( patientID );
        final ArrayList<Patient> represent = p.getRepresent();

        return represent;
    }
}
