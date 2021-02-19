package bms.exceptions;

/**
 * A class representing the exception thrown when a floor is added to a
 * building but the area of the floor below is smaller than the area of the
 * new floor.
 */
public class FloorTooSmallException extends Exception {

    /**
     * Constructs a normal FloorBelowTooSmallException with no error message
     * or cause.
     */
    public FloorTooSmallException() {
        super();
    }

    /**
     * Constructs a FloorTooSmallException that contains a helpful message
     * detailing why the exception occurred.
     *
     * @param message detail message
     */
    public FloorTooSmallException(String message) {
        super(message);
    }
}
