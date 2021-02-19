package bms.util;

import bms.exceptions.FireDrillException;
import bms.room.RoomType;

/**
 * Interface representing a fire drill.
 */
public interface FireDrill {

    /**
     * Sets the fireDrill method which all classes that implement FireDrill
     * must implement.
     * @param roomType type of room to call a fire drill on
     * @throws FireDrillException thrown when trying to call a fire drill on
     *                            a building with no rooms in it on any floors.
     */
    void fireDrill(RoomType roomType) throws FireDrillException;
}
