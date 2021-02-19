package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.exceptions.InsufficientSpaceException;
import bms.room.Room;
import bms.room.RoomType;
import bms.util.FireDrill;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a floor in a building which contains rooms.
 */
public class Floor implements FireDrill {

    // the unique floor number, corresponds to how many floors above ground
    // floor (inclusive)
    private int floorNumber;
    // The width of the floor in metres
    private double width;
    // The length of the floor in metres
    private double length;
    // The list of all rooms on the floor
    private List<Room> roomList = new ArrayList<>();

    /**
     * Constructs a floor.
     *
     * @param floorNumber the unique floor number
     * @param width the width of the floor
     * @param length the length of the floor
     */
    public Floor(int floorNumber, double width, double length){
        this.floorNumber = floorNumber;
        this.width = width;
        this.length = length;
    }

    /**
     * @return the floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * @return the minimum possible width of the floor, being 5 square metres
     */
    public static int getMinWidth() {
        return 5;
    }

    /**
     * @return the minimum possible length of the floor, being 5 square metres
     */
    public static int getMinLength() {
        return 5;
    }

    /**
     * @return the list of rooms on the floor
     */
    public List<Room> getRooms() {
        return new ArrayList<Room>(roomList);
    }

    /**
     * @return the width of the floor
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the length of the floor
     */
    public double getLength() {
        return length;
    }

    /**
     * Searches for the room with the specified room number on the floor.
     * @param roomNumber the room number
     * @return room object with the given number if found; null if not found
     */
    public Room getRoomByNumber(int roomNumber) {
        Room result = null;

        for (Room room : getRooms()) {
            // Check if there is a room with the same number
            if (room.getRoomNumber() == roomNumber) {
                result = room;
                break;
            } else {
                result = null;
            }
        }
        return result;
    }

    /**
     * @return the area of the floor.
     */
    public double calculateArea() {
        return getWidth() * getLength();
    }

    /**
     * Calculates the area on the floor that is currently occupied by rooms.
     *
     * @return area of the floor that is currently occupied, in square metres
     */
    public float occupiedArea() {
        float occupiedArea = 0;

        for (Room room : getRooms()) {
            occupiedArea += room.getArea();
        }
        return occupiedArea;
    }

    /**
     * Adds a room to the floor.
     *
     * @param newRoom room object to add to the floor
     * @throws DuplicateRoomException if a room with the same number already
     *                                exists on the floor.
     * @throws InsufficientSpaceException if there is insufficient floor space
     *                                    to allow the room to be added
     */
    public void addRoom(Room newRoom) throws DuplicateRoomException,
            InsufficientSpaceException {
        if (newRoom.getArea() < Room.getMinArea()) {
            throw new IllegalArgumentException();
        } else if (getRoomByNumber(newRoom.getRoomNumber()) != null) {
            throw new DuplicateRoomException();
        }
        else if (newRoom.getArea() + occupiedArea() > calculateArea()) {
            throw new InsufficientSpaceException();
        }
        roomList.add(newRoom);
    }

    /**
     * Starts a fire drill in all rooms of the given type on the floor.
     *
     * If the room type given is null, then all rooms on the floor must start
     * a fire drill.
     *
     * @param roomType type of room to call a fire drill on
     */
    public void fireDrill(RoomType roomType) {
        // Throw an exception if there are no rooms on the floor
        if (getRooms().isEmpty()) {
            throw new AssertionError();
        }
        for (Room room : getRooms()) {
            if (room.getType() == roomType | roomType == null) {
                room.setFireDrill(true);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in rooms on the floor.
     */
    public void cancelFireDrill() {
        // Throw an exception if there are no rooms on the floor
        if (getRooms().isEmpty()) {
            throw new AssertionError();
        }
        for (Room room : getRooms()) {
            room.setFireDrill(false);
        }
    }

    /**
     * @return The string representation of the floor and its associated
     *         details.
     */
    @Override
    public String toString(){
        String twoDecimalWidth = String.format("%.2f", getWidth());
        String twoDecimalLength = String.format("%.2f", getLength());
        return "Floor #" + getFloorNumber() + ": width=" + twoDecimalWidth +
            "m, length=" + twoDecimalLength + "m, rooms=" + getRooms().size();
    }
}
