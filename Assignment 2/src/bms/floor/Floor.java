package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.exceptions.FloorTooSmallException;
import bms.exceptions.InsufficientSpaceException;
import bms.room.Room;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.FireDrill;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a floor of a building.
 * <p>
 * All floors have a floor number (ground floor is floor 1), a list of rooms,
 * and a width and length.
 * <p>
 * A floor can be evacuated, which causes all rooms on the floor to be
 * evacuated.
 * @ass1
 */
public class Floor implements FireDrill, Encodable {
    /**
     * Unique floor number for this floor. Corresponds to how many floors above
     * ground floor (inclusive).
     */
    private int floorNumber;

    /**
     * List of rooms on the floor level.
     */
    private List<Room> rooms;

    /**
     * Width of the floor in metres.
     */
    private double width;

    /**
     * Length of the floor in metres.
     */
    private double length;

    /**
     * Minimum width of all floors, in metres.
     */
    private static final int MIN_WIDTH = 5;

    /**
     * Minimum length of all floors, in metres.
     */
    private static final int MIN_LENGTH = 5;

    /**
     * The floor's maintenance schedule
     */
    private MaintenanceSchedule maintenanceSchedule;

    /**
     * Creates a new floor with the given floor number.
     *
     * @param floorNumber a unique floor number, corresponds to how many floors
     * above ground floor (inclusive)
     * @param width the width of the floor in metres
     * @param length the length of the floor in metres
     * @ass1
     */
    public Floor(int floorNumber, double width, double length) {
        this.floorNumber = floorNumber;
        this.width = width;
        this.length = length;
        this.rooms = new ArrayList<>();
    }

    /**
     * Returns the floor number of this floor.
     *
     * @return floor number
     * @ass1
     */
    public int getFloorNumber() {
        return this.floorNumber;
    }

    /**
     * Returns the minimum width for all floors.
     *
     * @return 5
     * @ass1
     */
    public static int getMinWidth() {
        return MIN_WIDTH;
    }

    /**
     * Returns the minimum length for all floors.
     *
     * @return 5
     * @ass1
     */
    public static int getMinLength() {
        return MIN_LENGTH;
    }

    /**
     * Returns a new list containing all the rooms on this floor.
     * <p>
     * Adding or removing rooms from this list should not affect the
     * floor's internal list of rooms.
     *
     * @return new list containing all rooms on the floor
     * @ass1
     */
    public List<Room> getRooms() {
        return new ArrayList<>(this.rooms);
    }

    /**
     * Returns width of the floor.
     *
     * @return floor width
     * @ass1
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Returns length of the floor.
     *
     * @return floor length
     * @ass1
     */
    public double getLength() {
        return this.length;
    }

    /**
     * @return the floor's maintenance schedule, or null if it does not exist.
     */
    public MaintenanceSchedule getMaintenanceSchedule() {
        if (this.maintenanceSchedule != null) {
            return this.maintenanceSchedule;
        } else {
            return null;
        }
    }

    /**
     * Search for the room with the specified room number.
     * <p>
     * Returns the corresponding Room object, or null if the room was not
     * found.
     *
     * @param roomNumber room number of room to search for
     * @return room with the given number if found; null if not found
     * @ass1
     */
    public Room getRoomByNumber(int roomNumber) {
        for (Room room : this.rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    /**
     * Changes the width and length of this floor.
     *
     * @param newWidth new width dimension for the floor
     * @param newLength new length dimension for the floor
     * @throws IllegalArgumentException if newWidth < Floor.getMinWidth(); or
     *                                  newLength < Floor.getMinLength()
     * @throws FloorTooSmallException if the total size of the current rooms
     *                                could not be supported by decreased
     *                                dimensions
     */
    public void changeDimensions(double newWidth, double newLength) throws
            IllegalArgumentException, FloorTooSmallException {
        if (newWidth < getMinWidth() || newLength < getMinLength()) {
            throw new IllegalArgumentException();
        } else if ((newWidth * newLength) < occupiedArea()) {
            throw new FloorTooSmallException();
        } else {
            width = newWidth;
            length = newLength;
        }
    }

    /**
     * Calculates the area of the floor in square metres.
     * <p>
     * The area should be calculated as {@code getWidth()} multiplied by
     * {@code getLength()}.
     * <p>
     * For example, a floor with a length of 20.5 and width of 35.2, would be
     * 721.6 square metres.
     *
     * @return area of the floor in square metres
     * @ass1
     */
    public double calculateArea() {
        return this.getWidth() * this.getLength();
    }

    /**
     * Calculates the area of the floor which is currently occupied by all the
     * rooms on the floor.
     *
     * @return area of the floor that is currently occupied, in square metres
     * @ass1
     */
    public float occupiedArea() {
        float area = 0;
        for (Room room : rooms) {
            area += room.getArea();
        }
        return area;
    }

    /**
     * Adds a room to the floor.
     * <p>
     * The dimensions of the room are managed automatically. The length and
     * width of the room do not need to be specified, only the required space.
     *
     * @param newRoom object representing the new room
     * @throws IllegalArgumentException if area is less than Room.getMinArea()
     * @throws DuplicateRoomException if the room number on this floor is
     * already taken
     * @throws InsufficientSpaceException if there is insufficient space
     * available on the floor to be able to add the room
     * @ass1
     */
    // check that there is enough space available left on the floor
    public void addRoom(Room newRoom)
            throws DuplicateRoomException, InsufficientSpaceException {
        if (newRoom.getArea() < Room.getMinArea()) {
            throw new IllegalArgumentException(
                    "Area cannot be less than " + Room.getMinArea());
        }

        if (this.getRoomByNumber(newRoom.getRoomNumber()) != null) {
            throw new DuplicateRoomException(
                    "The room number " + newRoom.getRoomNumber()
                            + " is already taken on this floor.");
        }

        if ((this.occupiedArea() + newRoom.getArea()) > this.calculateArea()) {
            throw new InsufficientSpaceException("Insufficient space to add "
                    + "room. Floor area:" + this.calculateArea()
                    + "m^2, Occupied area: " + this.occupiedArea()
                    + "m^2, This room: " + newRoom.getArea() + "m^2");
        }

        // No problems, so add room to the list of rooms
        rooms.add(newRoom);
    }

    /**
     * Starts a fire drill in all rooms of the given type on the floor.
     * <p>
     * Only rooms of the given type must start a fire drill.
     * Rooms other than the given type must not start a fire drill.
     * <p>
     * If the room type given is null, then <b>all</b> rooms on the floor
     * must start a fire drill.
     *
     * @param roomType the type of room to carry out fire drills on; null if
     *                 fire drills are to be carried out in all rooms
     * @ass1
     */
    public void fireDrill(RoomType roomType) {
        for (Room r : this.rooms) {
            if (roomType == null || roomType == r.getType()) {
                r.setFireDrill(true);
            }
        }
    }

    /**
     * Cancels any ongoing fire drill in rooms on the floor.
     * <p>
     * All rooms must have their fire alarm cancelled regardless of room type.
     *
     * @ass1
     */
    public void cancelFireDrill() {
        for (Room r : this.rooms) {
            r.setFireDrill(false);
        }
    }

    /**
     * Adds a maintenance schedule to this floor with the given room order.
     *
     * @param roomOrder rooms on which to perform maintenance, in order
     * @throws IllegalArgumentException  if the given order is null or empty,
     *                                   if a room in the order is not on
     *                                   this floor, or if a room appears
     *                                   twice or more consecutively (the
     *                                   start of the list and the end of the
     *                                   list are also considered
     *                                   consecutive) in an order with at
     *                                   least two rooms.
     */
    public void createMaintenanceSchedule(List<Room> roomOrder)
            throws IllegalArgumentException {
        if (roomOrder.isEmpty() || roomOrder == null) {
            throw new IllegalArgumentException();
        }
        if (roomOrder.size() > 1) {
            if (roomOrder.get(0).equals(roomOrder.get(roomOrder.size() - 1))) {
                throw new IllegalArgumentException();
            }
            for (int i = 1; i < roomOrder.size(); i++) {
                if (roomOrder.get(i).equals(roomOrder.get(i - 1))) {
                    throw new IllegalArgumentException();
                }
            }
        }
        for (Room room : roomOrder) {
            if (!this.getRooms().contains(room)) {
                throw new IllegalArgumentException();
            }
        }
        for (Room room : this.getRooms()) {
            room.setMaintenance(false);
        }
        maintenanceSchedule = new MaintenanceSchedule(roomOrder);
    }

    /**
     * Returns true if and only if this floor is equal to the other given floor.
     *
     * @param obj other object to compare equality
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Floor)) {
            return false;
        }
        Floor otherFloor = (Floor) obj;
        for (Room room : otherFloor.getRooms()) {
            if (!this.getRooms().contains(room)) {
                return false;
            }
        }
        float epsilon = (float) 0.001;
        return this.getFloorNumber() == otherFloor.getFloorNumber() &&
                Math.abs(this.getWidth() - otherFloor.getWidth()) <= epsilon &&
                Math.abs(this.getLength() - otherFloor.getLength()) <= epsilon
                && this.getRooms().size() == otherFloor.getRooms().size();
    }

    /**
     * @return hash code of this floor
     */
    @Override
    public int hashCode() {
        return (int) width + (int) length + floorNumber + getRooms().size() +
                getRooms().hashCode();
    }

    /**
     * Returns the human-readable string representation of this floor.
     * <p>
     * The format of the string to return is
     * "Floor #'floorNumber': width='floorWidth'm, length='floorLength'm,
     * rooms='numRooms'"
     * without the single quotes, where 'floorNumber' is the floor's unique
     * number in the building, 'floorWidth' is the floor's width, 'floorLength'
     * is the floor's length, 'numRooms' is the number of rooms in the floor.
     * <p>
     * The floor's length and width should be formatted to two (2)
     * decimal places.
     * <p>
     * For example:
     * "Floor #6: width=12.80m, length=10.25m, rooms=15"
     *
     * @return string representation of this floor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Floor #%d: width=%.2fm, length=%.2fm, rooms=%d",
                this.floorNumber,
                this.width,
                this.length,
                this.rooms.size());
    }

    /**
     * Returns the machine-readable string representation of this floor and
     * all of its rooms and sensors.
     *
     * @return encoded string representation of this floor
     */
    public String encode() {
        String output = String.format("%d:%.2f:%.2f:%d", floorNumber, width,
                length, getRooms().size());

        if (getMaintenanceSchedule() != null) {
            output += ":" + maintenanceSchedule.encode() +
                    System.lineSeparator();
        } else {
            output += System.lineSeparator();
        }
        for (Room room : getRooms()) {
            output += room.encode() + System.lineSeparator();
        }
        return output.stripTrailing();
    }
}