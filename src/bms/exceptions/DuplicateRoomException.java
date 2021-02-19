package bms.exceptions;

/**
 * A class representing the exception thrown when a room is added to a floor,
 * but the room number is already taken on that floor.
 */
public class DuplicateRoomException extends Exception {

    /**
     * Constructs a normal DuplicateRoomException with no error message or
     * cause.
     */
    public DuplicateRoomException() {
        super();
    }

    /**
     * Constructs a DuplicateRoomException that contains a helpful message
     * detailing why the exception occurred.
     *
     * @param message detail message
     */
    public DuplicateRoomException(String message) {
        super(message);
    }
}
