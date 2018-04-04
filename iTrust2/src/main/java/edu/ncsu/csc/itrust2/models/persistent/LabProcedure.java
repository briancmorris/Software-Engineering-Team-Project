package edu.ncsu.csc.itrust2.models.persistent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.models.enums.PriorityLevel;
import edu.ncsu.csc.itrust2.models.enums.Status;

/**
 * Maintains data relevant to a Lab Procedure, stored in the database.
 *
 * @author Brian Morris
 *
 */
@Entity
@Table ( name = "LabProcedure" )
public class LabProcedure extends DomainObject<LabProcedure> {

    /** ID of the lab procedure. */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long          id;

    /** The LOINC code for this lab procedure. */
    @NotNull
    private LOINCCode     code;

    /**
     * The priority level of this lab procedure, specified by an HCP.
     */
    @NotNull
    private PriorityLevel priorityLevel;

    /**
     * The date this object was assigned.
     */
    @NotNull
    @Length ( min = 10, max = 10 )
    private String        date;

    /** The comments of this lab procedure, left by the HCP. */
    @NotNull
    @Length ( max = 1024 )
    private String        comments;

    /** The status of this lab procedure. */
    private Status        status;

    /**
     * The lab tech assigned to this procedure.
     */
    private User          labTech;

    /**
     * The office visit that this lab procedure is a part of.
     */
    private OfficeVisit   officeVisit;

    /**
     * Returns the id associated with this lab procedure.
     *
     * @return The id of the lab procedure.
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the lab procedure's ID to the given value. All saved lab procedures
     * must have unique IDs.
     *
     * @param id
     *            The new ID.
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the priority level of this lab procedure.
     *
     * @return The priority level of this lab procedure.
     */
    public PriorityLevel getPriorityLevel () {
        return priorityLevel;
    }

    /**
     * Sets the priority level of this lab procedure.
     *
     * @param priorityLevel
     *            The priority level to assign.
     */
    public void setPriorityLevel ( final PriorityLevel priorityLevel ) {
        this.priorityLevel = priorityLevel;
    }

    /**
     * Returns the date of this lab procedure.
     *
     * @return The date of this lab procedure.
     */
    public String getDate () {
        return date;
    }

    /**
     * Sets the date of this lab procedure. Must be of the format dd-MM-yyyy.
     *
     * @param date
     *            The new date of this lab procedure.
     */
    public void setDate ( final String date ) {
        /**
         * StackOverflow Solution:
         * https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
         */
        try {
            final DateFormat df = new SimpleDateFormat( "dd-MM-yyyy" );
            df.setLenient( false );
            df.parse( date );
            this.date = date;
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid date string format." );
        }
    }

    /**
     * Returns the comments of this lab procedure.
     *
     * @return The comments of this lab procedure.
     */
    public String getComments () {
        return comments;
    }

    /**
     * Sets the comments of this lab procedure.
     *
     * @param comments
     *            The new comments of this lab procedure.
     */
    public void setComments ( final String comments ) {
        this.comments = comments;
    }

    /**
     * Returns the status of this lab procedure.
     *
     * @return The status of this lab procedure.
     */
    public Status getStatus () {
        return status;
    }

    /**
     * Sets the status of this lab procedure.
     *
     * @param status
     *            The new status of this lab procedure.
     */
    public void setStatus ( final Status status ) {
        this.status = status;
    }

    /**
     * Returns the assigned lab tech of this lab procedure.
     *
     * @return The assigned lab tech of this lab procedure.
     */
    public User getLabTech () {
        return labTech;
    }

    /**
     * Sets the assigned lab tech of this lab procedure.
     *
     * @param labTech
     *            The new lab tech for this procedure.
     */
    public void setLabTech ( final User labTech ) {
        this.labTech = labTech;
    }

    /**
     * Returns the office visit that this lab procedure is tied to.
     *
     * @return The office visit that this lab procedure is tied to.
     */
    public OfficeVisit getOfficeVisit () {
        return officeVisit;
    }

    /**
     * Sets the office visit that this lab procedure is tied to.
     *
     * @param officeVisit
     *            The office visit that this lab procedure is tied to.
     */
    public void setOfficeVisit ( final OfficeVisit officeVisit ) {
        this.officeVisit = officeVisit;
    }
}
