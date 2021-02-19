package bms.exceptions;

/**
 * Exception thrown when a save file containing a list of building data is
 * invalid.
 */
public class FileFormatException extends Exception {

    /**
     * Constructs a normal FileFormatException with no error message or cause.
     */
    public FileFormatException() {
        super();
    }

    /**
     * Constructs a FileFormatException that contains a helpful message
     * detailing why the exception occurred.
     * @param message detail message
     */
    public FileFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a FileFormatException that contains a helpful message
     * detailing why the exception occurred, and an underlying cause of the
     * exception.
     *
     * @param message detail message
     * @param cause throwable that caused this exception
     */
    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
