package bms.exceptions;

/**
 * A class representing the exception thrown when a floor is added to a
 * building that already contains a floor on that level.
 */
public class DuplicateFloorException extends Exception {

    /**
     * Constructs a normal DuplicateFloorException with no error message or
     * cause.
     */
    public DuplicateFloorException() {
        super();
    }

    /**
     * Constructs a DuplicateFloorException that contains a helpful message
     * detailing why the exception occurred.
     *
     * @param message detail message
     */
    public DuplicateFloorException(String message) {
        super(message);
    }
}
