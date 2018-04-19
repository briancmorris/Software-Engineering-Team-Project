package edu.ncsu.csc.itrust2.forms.labs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    /** The time associated the LabProcedure object was assigned. */
    private String            time;

    /** The comments of the associated LabProcedure object. */
    private String            comments;

    /** The completion status of the associated LabProcedure object. */
    private String            completionStatus;

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

        // Parse the date and time.
        final SimpleDateFormat tempDate = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        setDate( tempDate.format( procedure.getDate().getTime() ) );
        final SimpleDateFormat tempTime = new SimpleDateFormat( "hh:mm aaa", Locale.ENGLISH );
        setTime( tempTime.format( procedure.getDate().getTime() ) );

        setComments( procedure.getComments() );
        setCompletionStatus( procedure.getCompletionStatus().getName() );
        setLabTech( procedure.getLabTech().getId() );
        setOfficeVisitId( procedure.getOfficeVisit().getId() );
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
     * Returns the completion status of the LabProcedureForm.
     *
     * @return the completion status of the LabProcedureForm.
     */
    public String getCompletionStatus () {
        return completionStatus;
    }

    /**
     * Sets the completion status of the LabProcedureForm.
     *
     * @param completionStatus
     *            the completion status to set
     */
    public void setCompletionStatus ( final String completionStatus ) {
        this.completionStatus = completionStatus;
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
    public Long getOfficeVisitId () {
        return officeVisitID;
    }

    /**
     * Sets the office visit ID of the LabProcedureForm.
     *
     * @param officeVisitID
     *            the office visit ID to set.
     */
    public void setOfficeVisitId ( final Long officeVisitID ) {
        this.officeVisitID = officeVisitID;
    }

    /**
     * Returns the time of this lab procedure.
     *
     * @return the time of this lab procedure.
     */
    public String getTime () {
        return time;
    }

    /**
     * Sets the time of this lab procedure
     *
     * @param time
     *            the time to set.
     */
    public void setTime ( final String time ) {
        this.time = time;
    }

}
