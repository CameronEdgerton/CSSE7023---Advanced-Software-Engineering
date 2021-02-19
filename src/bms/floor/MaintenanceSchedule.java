package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.TimedItem;
import bms.util.TimedItemManager;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents a maintenance schedule
 */
public class MaintenanceSchedule implements TimedItem, Encodable {

    /**
     * list of rooms on which to perform maintenance, in order
     */
    private List<Room> roomOrder;

    /**
     * Time elapsed since maintenance began on the current room
     */
    private int timeElapsed;

    /**
     * Constructs a maintenance schedule.
     *
     * @param roomOrder list of rooms on which to perform maintenance, in order
     */
    public MaintenanceSchedule(List<Room> roomOrder) {
        this.roomOrder = roomOrder;
        this.roomOrder.get(0).setMaintenance(true);
        this.timeElapsed = 0;
        TimedItemManager.getInstance().registerTimedItem(this);
    }

    /**
     * Returns the time taken to perform maintenance on the given room, in
     * minutes.
     *
     * @param room room on which to perform maintenance
     * @return room's maintenance time in minutes
     */
    public int getMaintenanceTime(Room room) {
        double baseTime;
        double multiplier = 0.0;
        double area = room.getArea();

        if (area == room.getMinArea()) {
            baseTime = 5.0;
        } else {
            baseTime = 5.0 + (float) ((area - room.getMinArea()) * 0.2);
        }
        if (room.getType() == RoomType.STUDY) {
            multiplier = 1.0;
        } else if (room.getType() == RoomType.OFFICE) {
            multiplier = 1.5;
        } else if (room.getType() == RoomType.LABORATORY) {
            multiplier = 2.0;
        }
        double maintenanceTime = baseTime * multiplier;
        return (int) Math.round(maintenanceTime);
    }

    /**
     * @return room currently in maintenance
     */
    public Room getCurrentRoom() {
        Room inMaintenance = null;
        for (Room room : roomOrder) {
            if (room.maintenanceOngoing() == true) {
                inMaintenance = room;
                break;
            }
        }
        return inMaintenance;
    }

    /**
     * @return time elapsed maintaining current room
     */
    public int getTimeElapsedCurrentRoom() {
        return timeElapsed;
    }

    /**
     * Progresses the maintenance schedule by one minute.
     */
    public void elapseOneMinute() {
        if (getCurrentRoom().evaluateRoomState() != RoomState.EVACUATE) {
            if (getTimeElapsedCurrentRoom() ==
                    getMaintenanceTime(getCurrentRoom())) {
                skipCurrentMaintenance();
            }
            this.timeElapsed++;
        }
    }

    /**
     * Stops the in-progress maintenance of the current room and progresses
     * to the next room.
     */
    public void skipCurrentMaintenance() {
        Room nextRoom;
        timeElapsed = 0;
        if (roomOrder.indexOf(getCurrentRoom()) < roomOrder.size() - 1) {
            nextRoom = roomOrder.get(roomOrder.indexOf(getCurrentRoom())
                    + 1);
        } else {
            nextRoom = roomOrder.get(0);
        }
        getCurrentRoom().setMaintenance(false);
        nextRoom.setMaintenance(true);
    }

    /**
     * @return string representation of this maintenance schedule
     */
    @Override
    public String toString() {
        return String.format("MaintenanceSchedule: currentRoom=#%d, " +
                "currentElapsed=%d", this.getCurrentRoom().getRoomNumber(),
                this.getTimeElapsedCurrentRoom());
    }

    /**
     * @return encoded string representation of this maintenance schedule
     */
    public String encode() {
        StringJoiner output = new StringJoiner(",");
        for (Room room : roomOrder) {
            output.add(String.valueOf(room.getRoomNumber()));
        }
        return String.valueOf(output);
    }
}
