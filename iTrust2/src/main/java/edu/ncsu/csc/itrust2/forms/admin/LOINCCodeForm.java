package edu.ncsu.csc.itrust2.forms.admin;

import java.io.Serializable;

import edu.ncsu.csc.itrust2.models.persistent.LOINCCode;

/**
 * A form for REST API communication. Contains fields for constructing LOINCCode
 * objects.
 *
 * @author Brian Morris
 *
 */
public class LOINCCodeForm implements Serializable {

    /** ID used to serialize this object to JSON. */
    private static final long serialVersionUID = 1L;

    /** The database ID of the associated LOINC code. */
    private Long              id;

    /** The code of the associated LOINC code. */
    private String            code;

    /** The long common name of the associated LOINC code. */
    private String            longCommonName;

    /** The special usage of the associated LOINC code. */
    private String            specialUsage;

    /** The component of the associated LOINC code. */
    private String            component;

    /** The prop of the associated LOINC code. */
    private String            prop;

    /**
     * Empty constructor for an admin to use when filling in a LOINC code.
     */
    public LOINCCodeForm () {

    }

    /**
     * Creates a LOINCCodeForm based on the provided LOINCCode object.
     *
     * @param code
     *            The provided LOINCCode object.
     */
    public LOINCCodeForm ( final LOINCCode code ) {
        setId( code.getId() );
        setCode( code.getCode() );
        setLongCommonName( code.getLongCommonName() );
        setSpecialUsage( code.getSpecialUsage() );
        setComponent( code.getComponent() );
        setProp( code.getProp() );
    }

    /**
     * Returns the database id of the associated LOINCCode object.
     *
     * @return the database id of the associated LOINCCode object.
     */
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of the LOINCCodeForm.
     *
     * @param id
     *            the new id of the LOINCCodeForm
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the code of the associated LOINCCode object.
     *
     * @return the code of the associated LOINCCode object.
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the code of the form.
     *
     * @param code
     *            the new code of the form.
     */
    public void setCode ( final String code ) {
        this.code = code;
    }

    /**
     * Returns the long common name of the associated LOINCCode object.
     *
     * @return the long common name of the associated LOINCCode object.
     */
    public String getLongCommonName () {
        return longCommonName;
    }

    /**
     * Sets the long common name of the form.
     *
     * @param longCommonName
     *            the new long common name of the form.
     */
    public void setLongCommonName ( final String longCommonName ) {
        this.longCommonName = longCommonName;
    }

    /**
     * Returns the special usage of the associated LOINCCode object.
     *
     * @return the special usage of the associated LOINCCode object.
     */
    public String getSpecialUsage () {
        return specialUsage;
    }

    /**
     * Sets the special usage of the form.
     *
     * @param specialUsage
     *            the new special usage of the form.
     */
    public void setSpecialUsage ( final String specialUsage ) {
        this.specialUsage = specialUsage;
    }

    /**
     * Returns the component of the associated LOINCCode object.
     *
     * @return the component of the associated LOINCCode object.
     */
    public String getComponent () {
        return component;
    }

    /**
     * Sets the component of the form.
     *
     * @param component
     *            the new component of the form.
     */
    public void setComponent ( final String component ) {
        this.component = component;
    }

    /**
     * Returns the prop of the associated LOINCCode object.
     *
     * @return the prop of the associated LOINCCode object.
     */
    public String getProp () {
        return prop;
    }

    /**
     * Sets the prop of the form.
     *
     * @param prop
     *            the new prop of the form.
     */
    public void setProp ( final String prop ) {
        this.prop = prop;
    }
}
