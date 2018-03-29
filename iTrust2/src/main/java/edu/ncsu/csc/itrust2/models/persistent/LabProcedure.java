package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table ( name = "LabProcedure" )
public class LabProcedure extends DomainObject<LabProcedure> {

    /** ID of the lab procedure. */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long      id;

    /** The LOINC code for ths lab procedure. */
    private LOINCCode code;

    /** The name of this lab procedure. */
    @NotEmpty
    @Length ( max = 64 )
    private String    name;

    /** The description of this lab procedure. */
    @NotNull
    @Length ( max = 1024 )
    private String    description;

    /**
     * The priority level of this lab procedure, specified by an HCP.
     */
    private Integer   priorityLevel;

    /**
     * Returns the id associated with this lab procedure.
     *
     * @return The id of the lab procedure.
     */
    @Override
    public Serializable getId () {
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
    public Integer getPriorityLevel () {
        return priorityLevel;
    }

    /**
     * Sets the priority level of this lab procedure.
     *
     * @param priorityLevel
     *            The priority level to assign.
     */
    public void setPriorityLevel ( final Integer priorityLevel ) {
        this.priorityLevel = priorityLevel;
    }

    /**
     * Returns the name of this lab procedure.
     *
     * @return The name of this lab procedure.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of this lab procedure.
     *
     * @param name
     *            The new name of this lab procedure.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the description of this lab procedure.
     *
     * @return The description of this lab procedure.
     */
    public String getDescription () {
        return description;
    }

    /**
     * Sets the description of this lab procedure.
     *
     * @param description
     *            The new description of this lab procedure.
     */
    public void setDescription ( final String description ) {
        this.description = description;
    }

}
