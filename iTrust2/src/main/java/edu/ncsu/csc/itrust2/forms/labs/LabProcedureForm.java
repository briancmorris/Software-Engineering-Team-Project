package edu.ncsu.csc.itrust2.forms.labs;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import edu.ncsu.csc.itrust2.models.persistent.LabProcedure;

/**
 * A form for REST API communication. Contains fields for constructing
 * LabProcedure objects.
 *
 * @author Brian Morris
 *
 */
public class LabProcedureForm implements Serializable {

    /** ID used to serialize this object to JSON. */
    private static final long serialVersionUID = 1L;

    /** The database ID of the associated LabProcedure object. */
    private Long              id;

    /** The LOINC code of the associated LabProcedure object. */
    private String            code;

    /** The priority level of the associated LabProcedure object. */
    private String            priorityLevel;

    /** The date the associated LabProcedure object was assigned. */
    private String            date;

    /** The comments of the associated LabProcedure object. */
    private String            comments;

    /** The status of the associated LabProcedure object. */
    private String            status;

    /** The assigned lab tech to the associated LabProcedure object. */
    private String            labTech;

    /**
     * The database ID of the office visit linked to the associated LabProcedure
     * object.
     */
    private Long              officeVisitID;

    /**
     * Empty constructor for when an HCP or LT fills in a lab procedure.
     */
    public LabProcedureForm () {

    }

    /**
     * Constructs a LabProcedureForm based on a given LabProcedure object.
     *
     * @param procedure
     *            The LabProcedureObject that represents this form.
     */
    public LabProcedureForm ( final LabProcedure procedure ) {
        setID( procedure.getId() );
        setCode( procedure.getCode().getCode() );
        setPriorityLevel( "" + procedure.getPriorityLevel().getCode() );
        final SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy" );
        setDate( format.format( procedure.getDate().getTime() ) );
        setComments( procedure.getComments() );
        setStatus( "" + procedure.getStatus().getCode() );
        setLabTech( procedure.getLabTech().getId() );
        setOfficeVisitID( procedure.getOfficeVisit().getId() );
    }

    /**
     * Returns the id of the LabProcedureForm.
     *
     * @return the id of the LabProcedureForm.
     */
    public Long getID () {
        return id;
    }

    /**
     * Sets the id of the LabProcedureForm.
     *
     * @param id
     *            the id to set.
     */
    public void setID ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the code of the LabProcedureForm.
     *
     * @return the code of the LabProcedureForm.
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the code of the LabProcedureForm
     *
     * @param code
     *            the code to set.
     */
    public void setCode ( final String code ) {
        this.code = code;
    }

    /**
     * Returns the priority level of the LabProcedureForm.
     *
     * @return the priorityLevel of the LabProcedureForm.
     */
    public String getPriorityLevel () {
        return priorityLevel;
    }

    /**
     * Sets the priority level of the LabProcedureForm.
     *
     * @param priorityLevel
     *            the priorityLevel to set
     */
    public void setPriorityLevel ( final String priorityLevel ) {
        this.priorityLevel = priorityLevel;
    }

    /**
     * Returns the date of the LabProcedureForm.
     *
     * @return the date of the LabProcedureForm.
     */
    public String getDate () {
        return date;
    }

    /**
     * Sets the date of the LabProcedureForm.
     *
     * @param date
     *            the date to set.
     */
    public void setDate ( final String date ) {
        this.date = date;
    }

    /**
     * Returns the comments of the LabProcedureForm.
     *
     * @return the comments of the LabProcedureForm.
     */
    public String getComments () {
        return comments;
    }

    /**
     * Sets the comments of the LabProcedureForm.
     *
     * @param comments
     *            the comments to set.
     */
    public void setComments ( final String comments ) {
        this.comments = comments;
    }

    /**
     * Returns the status of the LabProcedureForm.
     *
     * @return the status of the LabProcedureForm.
     */
    public String getStatus () {
        return status;
    }

    /**
     * Sets the status of the LabProcedureForm.
     *
     * @param status
     *            the status to set
     */
    public void setStatus ( final String status ) {
        this.status = status;
    }

    /**
     * Returns the lab tech of the LabProcedureForm.
     *
     * @return the lab tech of the LabProcedureForm.
     */
    public String getLabTech () {
        return labTech;
    }

    /**
     * Sets the lab tech of the LabProcedureForm.
     *
     * @param labTech
     *            the lab tech to set.
     */
    public void setLabTech ( final String labTech ) {
        this.labTech = labTech;
    }

    /**
     * Returns the office visit ID of the LabProcedureForm.
     *
     * @return the office visit ID of the LabProcedureForm.
     */
    public Long getOfficeVisitID () {
        return officeVisitID;
    }

    /**
     * Sets the office visit ID of the LabProcedureForm.
     *
     * @param officeVisitID
     *            the office visit ID to set.
     */
    public void setOfficeVisitID ( final Long officeVisitID ) {
        this.officeVisitID = officeVisitID;
    }

}
