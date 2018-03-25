package edu.ncsu.csc.itrust2.controllers.lt;

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
public class LTController {

    /**
     * Landing screen for a Patient when they log in
     *
     * @param model
     *            The data from the front end
     * @return The page to show to the user
     */
    @RequestMapping ( value = "lt/index" )
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    public String index ( final Model model ) {
        return edu.ncsu.csc.itrust2.models.enums.Role.ROLE_LT.getLanding();
    }

    /**
     * Returns the lab procedures screen for the LT.
     *
     * @param model
     *            Data from the front end
     * @return The page to display
     */
    @RequestMapping ( value = "lt/labProcedures" )
    @PreAuthorize ( "hasRole('ROLE_LT')" )
    public String labProcedures ( final Model model ) {
        return "/lt/labProcedures";
    }

}
