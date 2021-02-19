package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.ComfortSensor;
import bms.sensors.Sensor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class that provides a recommendation for a study room in a building.
 */
public class StudyRoomRecommender {

    /**
     * Finds the best room in the building for studying based on a variety of
     * factors
     *
     * @param building the building to search
     * @return room in the given building most suitable for studying
     */
    public static Room recommendStudyRoom(Building building) {
        Room bestRoom = null;
        List<Room> bestRooms = new ArrayList<>();
        List<Float> bestRoomValues = new ArrayList<>();

        // Check if there are rooms in the building.
        if (noRoomsInBuilding(building)) {
            return null;
        }
        for (Floor floor : building.getFloors()) {
            // Create copy of list of rooms
            List<Room> rooms = new ArrayList<>(floor.getRooms());
            // Create lists to store avg Comfort levels and corresponding rooms.
            List<Float> avgComfortValues = new ArrayList<>();
            List<Room> avgComfortRooms = new ArrayList<>();
            for (Room room : floor.getRooms()) {
                // Remove non-valid floors from list
                if (room.getType() != RoomType.STUDY ||
                        room.evaluateRoomState() != RoomState.OPEN) {
                    rooms.remove(room);
                    continue;
                }
                // Compute averages and store value / room for remaining rooms.
                avgComfortValues.add(avgComfortLevel(room));
                avgComfortRooms.add(room);
            }
            if (floor.getFloorNumber() == 1) {
                if (rooms.isEmpty()) {
                    return null;
                } else {
                    // Find best room and add it to list of best rooms.
                    bestRooms.add(findBestRoom(avgComfortValues,
                            avgComfortRooms));
                    // Find and store the max avg comfort level on the floor.
                    bestRoomValues.add(Collections.max(avgComfortValues));
                }
            } else {
                if (rooms.isEmpty()) {
                    break;
                } else {
                    bestRooms.add(findBestRoom(avgComfortValues,
                            avgComfortRooms));
                    bestRoomValues.add(Collections.max(avgComfortValues));
                }
            }
        }

        if (bestRoomValues.size() < 2) {
            return bestRooms.get(0);
        } else {
            // Iterate over list of best rooms for all floors to find the best
            // room in the building.
            for (int i = 1; i < bestRoomValues.size(); i++) {
                if (bestRoomValues.get(i) <= bestRoomValues.get(i - 1)) {
                    bestRoom = bestRooms.get(i - 1);
                    break;
                } else {
                    bestRoom = bestRooms.get(i);
                }
            }
        }
        return bestRoom;
    }

    /*
    Checks if the building has no rooms
     */
    private static boolean noRoomsInBuilding(Building building) {
        int emptyFloorCount = 0;

        for (Floor floor : building.getFloors()) {
            if (floor.occupiedArea() == 0) {
                emptyFloorCount += 1;
            }
        }
        return emptyFloorCount == building.getFloors().size();
    }

    /*
    Calculates the average comfort level of the sensors in a room.
     */
    private static float avgComfortLevel(Room room) {
        int comfortLevel = 0;
        int comfortSensorCount = 0;
        int notComfortSensorCount = 0;
        float avgComfortLevel = 0;

        for (Sensor sensor : room.getSensors()) {
            if (sensor instanceof ComfortSensor) {
                comfortLevel +=
                        ((ComfortSensor) sensor).getComfortLevel();
                comfortSensorCount += 1;
            } else {
                notComfortSensorCount += 1;
            }
        }
        // If no comfort sensors in room, room's comfort level = 0
        if (notComfortSensorCount == room.getSensors().size()) {
            return avgComfortLevel;
        } else {
            return (float)comfortLevel / comfortSensorCount;
        }
    }

    /*
    Finds the best room on a floor based on the highest average comfort level
     */
    private static Room findBestRoom(List<Float> avgComfortValues, List<Room>
            avgComfortRooms) {
        int index = avgComfortValues.indexOf(Collections.max(avgComfortValues));
        return avgComfortRooms.get(index);
    }
}