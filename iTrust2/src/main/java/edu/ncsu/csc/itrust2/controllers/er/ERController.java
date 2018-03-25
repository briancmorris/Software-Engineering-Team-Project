package edu.ncsu.csc.itrust2.controllers.er;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * Returns the emergencyHealthRecords screen for the ER.
     *
     * @param model
     *            Data from the front end
     * @return The page to display
     */
    @RequestMapping ( value = "er/emergencyHealthRecords" )
    @PreAuthorize ( "hasRole('ROLE_ER')" )
    public String emergencyHealthRecords ( final Model model ) {
        return "/er/emergencyHealthRecords";
    }

}
