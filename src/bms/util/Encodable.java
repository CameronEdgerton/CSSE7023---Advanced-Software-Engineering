package bms.util;

/**
 * Interface representing an encodable object.
 */
public interface Encodable {

    /**
     * @return the String representation of the current state of this object.
     */
    String encode();
}
