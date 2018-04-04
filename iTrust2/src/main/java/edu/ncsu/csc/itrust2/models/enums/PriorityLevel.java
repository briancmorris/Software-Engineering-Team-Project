package edu.ncsu.csc.itrust2.models.enums;

/**
 * Maintains a priority level for a specific task.
 *
 * @author Brian Morris
 *
 */
public enum PriorityLevel {

    /**
     * The highest priority.
     */
    ONE ( 1 ),

    /**
     * The second highest priority.
     */
    TWO ( 2 ),

    /**
     * The third highest priority.
     */
    THREE ( 3 ),

    /**
     * The lowest priority.
     */
    FOUR ( 4 );

    private int code;

    /**
     * Constructs a priority level with the given code value.
     * 
     * @param code
     *            the value to give this priority level.
     */
    private PriorityLevel ( final int code ) {
        this.code = code;
    }

    /**
     * Returns the code of this enumeration value.
     * 
     * @return The code of this enumeration value.
     */
    public int getCode () {
        return code;
    }

}
