package edu.ncsu.csc.itrust2.models.persistent;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.forms.admin.LOINCCodeForm;

/**
 * Maintains information on LOINC compliant lab procedure codes. Used to store
 * persistent information in the database.
 *
 * @author Brian Morris
 *
 */
@Entity
@Table ( name = "LOINCCodes" )
public class LOINCCode extends DomainObject<LabProcedure> {

    /** The database id of this LOINC Code. */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long   id;

    /** The LOINC Code of the lab procedure. Must follow the format: #####-# */
    @Pattern ( regexp = "^\\d{5}-\\d{1}$" )
    private String code;

    /** The long common name of the LOINC lab procedure. */
    @NotNull
    @Length ( max = 1024 )
    private String longCommonName;

    /** The special usage of the LOINC lab procedure. */
    @NotNull
    @Length ( max = 1024 )
    private String specialUsage;

    /** The component of the LOINC lab procedure. */
    @NotNull
    @Length ( max = 1024 )
    private String component;

    /** The prop of the LOINC lab procedure. */
    @NotNull
    @Length ( max = 1024 )
    private String prop;

    /**
     * Empty constructor for Hibernate.
     */
    public LOINCCode () {

    }

    /**
     * Constructs a LOINCCode object based on the provided LOINCCodeForm.
     *
     * @param form
     *            The form to base the LOINCCode object on.
     */
    public LOINCCode ( final LOINCCodeForm form ) {
        id = form.getId();
        code = form.getCode();
        longCommonName = form.getLongCommonName();
        specialUsage = form.getSpecialUsage();
        component = form.getComponent();
        prop = form.getProp();
    }

    /**
     * Returns the database ID of this LOINC code.
     *
     * @return the database ID of this LOINC code.
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the database ID of this LOINC code.
     *
     * @param id
     *            the new databnase ID of this LOINC code.
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the code of this LOINC code.
     *
     * @return the code of this LOINC code.
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the code of this LOINC code. Must be of the form: #####-#
     *
     * @param code
     *            the new code of this LOINC code.
     */
    public void setCode ( final String code ) {
        this.code = code;
    }

    /**
     * Returns the long common name of this LOINC code.
     *
     * @return the long common name of this LOINC code.
     */
    public String getLongCommonName () {
        return longCommonName;
    }

    /**
     * Sets the long common name of this LOINC code.
     *
     * @param longCommonName
     *            the new long common name of this LOINC code.
     */
    public void setLongCommonName ( final String longCommonName ) {
        this.longCommonName = longCommonName;
    }

    /**
     * Returns the special usage of this LOINC code.
     *
     * @return the special usage of this LOINC code.
     */
    public String getSpecialUsage () {
        return specialUsage;
    }

    /**
     * Sets the special usage of this LOINC code.
     *
     * @param specialUsage
     *            the new special usage of this LOINC code.
     */
    public void setSpecialUsage ( final String specialUsage ) {
        this.specialUsage = specialUsage;
    }

    /**
     * Returns the component of this LOINC code.
     *
     * @return the component of this LOINC code.
     */
    public String getComponent () {
        return component;
    }

    /**
     * Sets the component of this LOINC code.
     *
     * @param component
     *            the new component of this LOINC code.
     */
    public void setComponent ( final String component ) {
        this.component = component;
    }

    /**
     * Returns the prop of this LOINC code.
     *
     * @return the prop of this LOINC code.
     */
    public String getProp () {
        return prop;
    }

    /**
     * Sets the prop of this LOINC code.
     *
     * @param prop
     *            the new prop of this LOINC code.
     */
    public void setProp ( final String prop ) {
        this.prop = prop;
    }

    /**
     * Returns a List of LOINCCodes that meet the given WHERE clause.
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return The list of Codes selected.
     */
    @SuppressWarnings ( "unchecked" )
    private static List<LOINCCode> getWhere ( final List<Criterion> where ) {
        return (List<LOINCCode>) getWhere( LOINCCode.class, where );
    }

    /**
     * Returns the LOINC code with the given ID
     *
     * @param id
     *            The ID to retrieve.
     * @return The LOINCCode requested if it exists.
     */
    public static LOINCCode getById ( final Long id ) {
        try {
            return getWhere( createCriterionAsList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }

    }

    /**
     * Gets the LOINCCode with the code matching the given value. Returns null
     * if none found.
     *
     * @param code
     *            the code to search for
     * @return the matching LOINC code.
     */
    public static LOINCCode getByCode ( final String code ) {
        try {
            return getWhere( createCriterionAsList( "code", code ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Returns a list of all LOINC codes in the system.
     *
     * @return The list of LOINC codes in the system.
     */
    @SuppressWarnings ( "unchecked" )
    public static List<LOINCCode> getAll () {
        return (List<LOINCCode>) DomainObject.getAll( LOINCCode.class );
    }

}
