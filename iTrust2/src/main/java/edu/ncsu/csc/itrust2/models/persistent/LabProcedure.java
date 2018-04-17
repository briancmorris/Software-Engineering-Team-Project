package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.forms.labs.LabProcedureForm;
import edu.ncsu.csc.itrust2.models.enums.CompletionStatus;
import edu.ncsu.csc.itrust2.models.enums.PriorityLevel;

/**
 * Maintains data relevant to a Lab Procedure, stored in the database.
 *
 * @author Brian Morris
 *
 */
@Entity
@Table ( name = "LabProcedures" )
public class LabProcedure extends DomainObject<LabProcedure> {

    /** ID of the lab procedure. */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long             id;

    /** The LOINC code for this lab procedure. */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "LOINC_Code" )
    private LOINCCode        code;

    /** The priority level of this lab procedure, specified by an HCP. */
    @NotNull
    @Enumerated ( EnumType.STRING )
    private PriorityLevel    priorityLevel;

    /** The date this object was assigned. */
    @NotNull
    private Calendar         date;

    /** The comments of this lab procedure, left by the HCP. */
    @NotNull
    @Length ( max = 1024 )
    private String           comments;

    /** The completion status of this lab procedure. */
    @NotNull
    @Enumerated ( EnumType.STRING )
    private CompletionStatus completionStatus;

    /** The lab tech assigned to this procedure. */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "Lab_Tech" )
    private User             labTech;

    /** The office visit that this lab procedure is a part of. */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "Office_Visit" )
    private OfficeVisit      officeVisit;

    /**
     * Empty constructor for Hibernate.
     */
    public LabProcedure () {

    }

    /**
     * Constructs a LabProcedure object based on the provided LabProcedureForm.
     *
     * @param form
     *            The LabProcedureForm to base the LabProcedure object on.
     * @throws ParseException
     *             If there is an error with date formatting.
     */
    public LabProcedure ( final LabProcedureForm form ) throws ParseException {
        setId( form.getID() );
        setCode( LOINCCode.getByCode( form.getCode() ) );
        setPriorityLevel( PriorityLevel.parse( form.getPriorityLevel() ) );

        // Parse the date.
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm aaa", Locale.ENGLISH );
        final Date parsedDate = sdf.parse( form.getDate() + " " + form.getTime() );
        final Calendar temp = Calendar.getInstance();
        temp.setTime( parsedDate );

        setDate( temp );
        setComments( form.getComments() );
        setCompletionStatus( CompletionStatus.parse( form.getCompletionStatus() ) );
        setLabTech( User.getByName( form.getLabTech() ) );
        setOfficeVisit( OfficeVisit.getById( form.getOfficeVisitId() ) );

    }

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
     * Returns the LOINC code of this lab procedure.
     *
     * @return the LOINC code of this lab procedure.
     */
    public LOINCCode getCode () {
        return code;
    }

    /**
     * Sets the LOINC code of this lab procedure.
     *
     * @param code
     *            The code to set.
     */
    public void setCode ( final LOINCCode code ) {
        this.code = code;
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
    public Calendar getDate () {
        return date;
    }

    /**
     * Sets the date of this lab procedure. Must be of the format dd-MM-yyyy.
     *
     * @param date
     *            The new date of this lab procedure.
     */
    public void setDate ( final Calendar date ) {
        this.date = date;
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
     * Returns the completion status of this lab procedure.
     *
     * @return The completion status of this lab procedure.
     */
    public CompletionStatus getCompletionStatus () {
        return completionStatus;
    }

    /**
     * Sets the completion status of this lab procedure.
     *
     * @param completionStatus
     *            The new completion status of this lab procedure.
     */
    public void setCompletionStatus ( final CompletionStatus completionStatus ) {
        this.completionStatus = completionStatus;
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

    /**
     * Returns a List of lab procedures that meet the given WHERE clause.
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return The list of lab procedures selected.
     */
    @SuppressWarnings ( "unchecked" )
    private static List<LabProcedure> getWhere ( final List<Criterion> where ) {
        return (List<LabProcedure>) getWhere( LabProcedure.class, where );
    }

    /**
     * Returns the lab procedure with the given ID
     *
     * @param id
     *            The ID to retrieve.
     * @return The lab procedure requested if it exists.
     */
    public static LabProcedure getById ( final Long id ) {
        try {
            return getWhere( createCriterionAsList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }

    }

    /**
     * Returns a list of all lab procedures in the system.
     *
     * @return The list of lab procedures in the system.
     */
    @SuppressWarnings ( "unchecked" )
    public static List<LabProcedure> getAll () {
        return (List<LabProcedure>) DomainObject.getAll( LabProcedure.class );
    }
}
