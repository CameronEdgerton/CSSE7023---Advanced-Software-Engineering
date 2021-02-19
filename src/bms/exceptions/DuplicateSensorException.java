package bms.exceptions;

/**
 * A class representing the exception thrown when a sensor is added to a room
 * that already contains a sensor of the same type.
 */
public class DuplicateSensorException extends Exception {

    /**
     * Constructs a normal DuplicateSensorException with no error message or
     * cause.
     */
    public DuplicateSensorException() {
        super();
    }

    /**
     * Constructs a DuplicateSensorException that contains a helpful message
     * detailing why the exception occurred.
     *
     * @param message detail message
     */
    public DuplicateSensorException(String message) {
        super(message);
    }
}
