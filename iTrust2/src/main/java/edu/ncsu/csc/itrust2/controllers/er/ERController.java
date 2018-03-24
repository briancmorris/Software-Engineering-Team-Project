package edu.ncsu.csc.itrust2.controllers.er;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Controller for the Patient landing screen and basic patient information
 *
 * @author Kai Presler-Marshall
 *
 */
@Controller
public class ERController {

    /**
     * Landing screen for a Patient when they log in
     *
     * @param model
     *            The data from the front end
     * @return The page to show to the user
     */
    @RequestMapping ( value = "er/index" )
    @PreAuthorize ( "hasRole('ROLE_ER')" )
    public String index ( final Model model ) {
        return edu.ncsu.csc.itrust2.models.enums.Role.ROLE_ER.getLanding();
    }

    /**
     * Provides the page for a User to view and edit their demographics
     *
     * @param model
     *            The data for the front end
     * @return The page to show the user so they can edit demographics
     */
    @GetMapping ( value = "er/editDemographics" )
    @PreAuthorize ( "hasRole('ROLE_ER')" )
    public String viewDemographics ( final Model model ) {
        final User self = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
        final PatientForm form = new PatientForm( Patient.getPatient( self ) );
        model.addAttribute( "PatientForm", form );
        LoggerUtil.log( TransactionType.VIEW_DEMOGRAPHICS, self );
        return "/er/editDemographics";
    }

    /**
     * Processes the Edit Demographics form for a Patient
     *
     * @param form
     *            Form from the user to parse and validate
     * @param result
     *            The validation result on the firm
     * @param model
     *            Data from the front end
     * @return Page to show to the user
     */
    @PostMapping ( "/er/editDemographics" )
    @PreAuthorize ( "hasRole('ROLE_ER')" )
    public String demographicsSubmit ( @Valid @ModelAttribute ( "PatientForm" ) final PatientForm form,
            final BindingResult result, final Model model ) {
        Patient p = null;
        try {
            p = new Patient( form );
            p.setSelf( User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() ) );
        }
        catch ( final Exception e ) {
            e.printStackTrace( System.out );
            result.rejectValue( "dateOfBirth", "dateOfBirth.notvalid", "Expected format: MM/DD/YYYY" );
        }

        if ( result.hasErrors() ) {
            model.addAttribute( "PatientForm", form );
            return "/er/editDemographics";
        }
        else {
            // Delete the patient so that the cache has to refresh.
            final Patient oldPatient = Patient.getByName( p.getSelf().getUsername() );
            if ( oldPatient != null ) {
                oldPatient.delete();
            }
            p.save();
            LoggerUtil.log( TransactionType.EDIT_DEMOGRAPHICS,
                    SecurityContextHolder.getContext().getAuthentication().getName() );
            return "er/editDemographicsResult";
        }
    }

}
