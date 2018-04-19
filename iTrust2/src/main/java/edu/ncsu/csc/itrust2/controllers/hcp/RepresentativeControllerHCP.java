/**
 * 
 */
package edu.ncsu.csc.itrust2.controllers.hcp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Timothy Figgins
 *
 */
public class RepresentativeControllerHCP {

    /**
     * Returns the page for hcp to view all Representatives/Representees for patients
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/hcp/editRepresentatives" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String viewOfficeVisits ( final Model model ) {
        return "/hcp/editRepresentatives";
    }
}
