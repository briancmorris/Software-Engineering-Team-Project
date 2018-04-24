package edu.ncsu.csc.itrust2.models.enums;

/**
 * Represents the completion status of a task.
 *
 * @author Brian Morris
 *
 */
public enum CompletionStatus {

    /**
     * The task has not been started.
     */
    NOT_STARTED ( "Not started" ),

    /**
     * The task is in-progress.
     */
    IN_PROGRESS ( "In-progress" ),

    /**
     * The task has been completed.
     */
    COMPLETED ( "Completed" ),

    /**
     * A completion status that has not been specified.
     */
    NOT_SPECIFIED ( "Not specified" );

    /** The name of this completion status. */
    private String name;

    /**
     * Constructor to establish the name of the enum.
     *
     * @param name
     *            The name of the enum status.
     */
    private CompletionStatus ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the name of this completion status.
     *
     * @return The name of this completion status.
     */
    public String getName () {
        return name;
    }

    /**
     * Finds the matching CompletionStatus Enum from the status provided.
     *
     * @param statusStr
     *            Name of the CompletionStatus to find an Enum record for.
     * @return The CompletionStatus found from the string, or Not Specified if
     *         none could be found.
     */
    public static CompletionStatus parse ( final String statusStr ) {
        for ( final CompletionStatus status : values() ) {
            if ( status.getName().equals( statusStr ) ) {
                return status;
            }
        }
        return NOT_SPECIFIED;
    }
}
