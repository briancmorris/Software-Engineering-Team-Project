/**
 *
 */
package edu.ncsu.csc.itrust2.controllers.patient;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Timothy Figgins
 *
 */
public class RepresentativeController {

    /**
     * Returns the page for patient to view all Representatives/Representees
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/patient/editPersonalRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public String viewOfficeVisits ( final Model model ) {
        return "/patient/editPersonalRepresentatives";
    }
}
