package bms.building;

import bms.exceptions.DuplicateFloorException;
import bms.exceptions.FireDrillException;
import bms.exceptions.FloorTooSmallException;
import bms.exceptions.NoFloorBelowException;
import bms.floor.Floor;
import bms.room.RoomType;
import bms.util.FireDrill;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a building of floors, which in turn, contain rooms.
 */
public class Building implements FireDrill {

    // The name of the building
    private String name;
    // The list of floors in the building
    private List<Floor> floorList = new ArrayList<>();

    /**
     * Constructs a building
     *
     * @param name the name of the building
     */
    public Building(String name) {
        this.name = name;
    }

    /**
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * @return the list of floors in the building
     */
    public List<Floor> getFloors() {
        return new ArrayList<Floor>(floorList);
    }

    /**
     * Searches for the floor with the specified floor number.
     * @param floorNumber floor number of floor to search for
     * @return floor with the given number if found; null if not found
     */
    public Floor getFloorByNumber(int floorNumber) {
        Floor result = null;

        for (Floor floor : getFloors()) {
            //Check if there is a floor with the same number
            if (floor.getFloorNumber() == floorNumber) {
                result = floor;
                break;
            } else {
                result = null;
            }
        }
        return result;
    }

    /**
     * Adds a floor to the building.
     *
     * @param newFloor object representing the new floor
     * @throws IllegalArgumentException if the floor number is below 1, or
     *                                  the width / length are less than the
     *                                  minimum width / length
     * @throws DuplicateFloorException if a floor with the same number
     *                                 already exists
     * @throws NoFloorBelowException  if this is at level 2 or above and
     *                                there is no floor below to support this
     *                                new floor
     * @throws FloorTooSmallException if this is at level 2 or above and the
     *                                floor below is not big enough to
     *                                support this new floor
     */
    public void addFloor(Floor newFloor) throws IllegalArgumentException,
            DuplicateFloorException, NoFloorBelowException,
            FloorTooSmallException {
        Floor floorBelow = getFloorByNumber(newFloor.getFloorNumber()
                - 1);

        if (newFloor.getFloorNumber() <= 0 |
                newFloor.getWidth() < Floor.getMinWidth() |
                newFloor.getLength() < Floor.getMinLength()) {
            throw new IllegalArgumentException();
        } else if (getFloorByNumber(newFloor.getFloorNumber()) != null) {
            throw new DuplicateFloorException();
        } else if (newFloor.getFloorNumber() >= 2 && floorBelow == null) {
            throw new NoFloorBelowException();
        } else if (newFloor.getFloorNumber() >= 2 && floorBelow.calculateArea()
                < newFloor.calculateArea()) {
            throw new FloorTooSmallException();
        }
        floorList.add(newFloor);
    }

    /**
     * Start a fire drill in all rooms of the given type in the building.
     *
     * If the room type given is null, then all rooms in the building will
     * start a fire drill.
     *
     * @param roomType type of room to call a fire drill on
     * @throws FireDrillException if there are no rooms of any type in the
     *                            building
     */
    public void fireDrill(RoomType roomType) throws FireDrillException {
        int count = 0;
        // Throw exception if there are no floors and therefore no rooms
        if (getFloors().isEmpty()) {
            throw new FireDrillException();
        }

        for (Floor floor : getFloors()) {
            // Set a fire drill in rooms of the given roomType if the floor has
            // rooms
            if (!floor.getRooms().isEmpty()) {
                floor.fireDrill(roomType);
            // If no rooms are found on the floor, add to the count
            } else {
                count += 1;
            }
            // Throws exception if every floor has no rooms.
            if (count == getFloors().size()) {
                throw new FireDrillException();
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in the building.
     */
    public void cancelFireDrill() {
        for (Floor floor : getFloors()) {
            floor.cancelFireDrill();
        }
    }

    /**
     * @return The string representation of the building and its associated
     *         details.
     */
    @Override
    public String toString() {
        return "Building: name=\"" + getName() + "\", floors=" +
                getFloors().size();
    }
}
