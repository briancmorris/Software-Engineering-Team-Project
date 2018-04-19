package edu.ncsu.csc.itrust2.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller enables Admins to add and delete LOINC Codes to the system.
 *
 * @author Brian Morris
 *
 */

@Controller
public class LOINCCodeController {

    /**
     * Manage LOINC codes.
     *
     * @param model
     *            Data for the front end.
     * @return Page to navigate to.
     */
    @RequestMapping ( value = "admin/manageLOINCCodes" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String manageCodes ( final Model model ) {
        return "/admin/manageLOINCCodes";
    }

}
