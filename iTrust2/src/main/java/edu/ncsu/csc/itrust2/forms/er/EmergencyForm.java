package edu.ncsu.csc.itrust2.forms.er;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Form for user to fill out a Patient emergency health record
 *
 * @author Garrett Watts
 *
 */
public class EmergencyForm {

    /**
     * Populate the emergency form from a patient user name string
     *
     * @param patient
     *            the patient object to set the form with
     */
    @SuppressWarnings ( "deprecation" )
    public EmergencyForm ( final String username ) {
        final User user2 = User.getByName( username );
        final Patient patient = Patient.getByName( username );
        if ( null == patient ) {
            setId( "" );
            return; /* Nothing to do here */
        }

        setFirstName( patient.getFirstName() );
        setLastName( patient.getLastName() );

        setName( patient.getFirstName() + " " + patient.getLastName() );

        final SimpleDateFormat date = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        if ( null != patient.getDateOfBirth() ) {
            setDateOfBirth( date.format( patient.getDateOfBirth().getTime() ) );
            final Date today = Calendar.getInstance().getTime();
            final Date dob = patient.getDateOfBirth().getTime();
            int age = today.getYear() - dob.getYear();

            if ( today.getMonth() < dob.getMonth() ) {
                age--;
            }
            else if ( today.getMonth() == dob.getMonth() ) {
                if ( today.getDay() < dob.getDay() ) {
                    age--;
                }
            }
            setAge( Integer.toString( age ) );
        }

        if ( null != patient.getBloodType() ) {
            setBloodType( patient.getBloodType().toString() );
        }

        if ( null != patient.getGender() ) {
            setGender( patient.getGender().toString() );
        }

        setId( user2.getId() );

        setSelf( patient.getSelf().getUsername() );

    }

    /** The username of the patient **/
    private String self;

    /** The first name of the patient **/
    private String firstName;

    /** The last name of the patient **/
    private String lastName;

    /** The date of birth of the patient **/
    private String dateOfBirth;

    /** The blood type of the patient **/
    private String bloodType;

    /** The gender of the patient **/
    private String gender;

    /** The id of the patient **/
    private String id;

    /** The age of the patient */
    private String age;

    /** The full name of the patient */
    private String name;

    /**
     * Get the full name of the patient
     *
     * @return the full name of the patient
     */
    public String getName () {
        return name;
    }

    /**
     * Set the full name of the patient
     *
     * @param name
     *            the full name of the patient
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Get the age of the patient
     *
     * @return the age of the patient
     */
    public String getAge () {
        return age;
    }

    /**
     * Set the age of the patient
     *
     * @param age
     *            the age of the patient
     */
    public void setAge ( final String age ) {
        this.age = age;
    }

    /**
     * Empty constructor
     */
    public EmergencyForm () {
    }

    /**
     * Get the first name of the patient
     *
     * @return the first name of the patient
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Set the first name of the patient
     *
     * @param firstName
     *            the first name of the patient
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of the patient
     *
     * @return the last name of the patient
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Set the last name of the patient
     *
     * @param lastName
     *            the last name of the patient
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Get the date of birth of the patient
     *
     * @return the date of birth of the patient
     */
    public String getDateOfBirth () {
        return dateOfBirth;
    }

    /**
     * Set the date of birth of the patient
     *
     * @param dateOfBirth
     *            the date of birth of the patient
     */
    public void setDateOfBirth ( final String dateOfBirth ) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get the blood type of the patient
     *
     * @return the blood type of the patient
     */
    public String getBloodType () {
        return bloodType;
    }

    /**
     * Set the blood type of the patient
     *
     * @param bloodType
     *            the blood type of the patient
     */
    public void setBloodType ( final String bloodType ) {
        this.bloodType = bloodType;
    }

    /**
     * Get the gender of the patient
     *
     * @return the gender of the patient
     */
    public String getGender () {
        return gender;
    }

    /**
     * Set the gender of the patient
     *
     * @param gender
     *            the gender of the patient
     */
    public void setGender ( final String gender ) {
        this.gender = gender;
    }

    /**
     * Get the id of the patient
     *
     * @return the id of the patient
     */
    public String getId () {
        return id;
    }

    /**
     * Set the id of the patient
     *
     * @param id
     *            the id of the patient
     */
    public void setId ( final String id ) {
        this.id = id;
    }

    /**
     * Get the username of the patient
     *
     * @return the username of the patient
     */
    public String getSelf () {
        return self;
    }

    /**
     * Set the username of the patient
     *
     * @param self
     *            the username of the patient
     */
    public void setSelf ( final String self ) {
        this.self = self;
    }

}
