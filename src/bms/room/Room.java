package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.sensors.Sensor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A class representing a room on a floor in a building.
 */
public class Room {

    // The unique room number of the room on this floor
    private int roomNumber;
    // The type of room
    private RoomType type;
    // The area of the room in square meters
    private double area;
    // The fire drill status of the room (whether a drill is ongoing or not)
    private boolean fireDrillStatus;
    // The list of sensors in the room
    private List<Sensor> sensorList = new ArrayList<>();

    /**
     * Constructs a room.
     *
     * @param roomNumber the unique room number of the room on this floor
     * @param type the type of room
     * @param area the area of the room in square metres
     */
    public Room(int roomNumber, RoomType type, double area) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;
        // Set the fire drill status to false by default
        this.fireDrillStatus = false;
    }

    /**
     * @return the room number
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * @return the area of the room
     */
    public double getArea() {
        return area;
    }

    /**
     * @return the minimum allowable area of a room (5 square metres)
     */
    public static int getMinArea() {
        return 5;
    }

    /**
     * @return the type of room
     */
    public RoomType getType() {
        return type;
    }

    /**
     * @return the status (true or false) as to whether a fire drill is ongoing
     *          in the room
     */
    public boolean fireDrillOngoing() {
        return fireDrillStatus;
    }

    /**
     * @return the list of sensors currently in the room
     */
    public List<Sensor> getSensors() {
        return new ArrayList<Sensor>(sensorList);
    }

    /**
     * Changes the status of the fire drill in the room based on the
     * parameter given.
     *
     * If fireDrill is true, the fire drill status will be/change to true.
     * If fireDrill is false, the fire drill status will be/change to false.
     *
     * @param fireDrill whether a fire drill is ongoing.
     */
    public void setFireDrill(boolean fireDrill) {
        fireDrillStatus = fireDrill;
    }

    /*
    * Retrieves string name of a sensor based on the sensor's simple name.
     */
    private String getSensorName(Sensor sensor) {
        return sensor.getClass().getSimpleName();
    }

    /**
     * Returns the given type of sensor if there is one in the list of
     * sensors; returns null otherwise.
     *
     * @param sensorType the type of sensor
     * @return the sensor in this room of the given type; null if none found
     */
    public Sensor getSensor(String sensorType) {
        Sensor result = null;

        for (Sensor sensor : getSensors()) {
            // Check if there is a sensor in the list with the same type
            if (getSensorName(sensor).equals(sensorType)) {
                result = sensor;
                break;
            } else {
                result = null;
            }
        }
        return result;
    }

    /**
     * Adds a sensor to the room if a sensor of the same type is not already
     * in the room.
     *
     * Sorts the list of sensors in alphabetical order after adding a new
     * sensor.
     *
     * @param sensor the sensor to add to the room
     * @throws DuplicateSensorException if there is already a sensor of the
     *                                  same type in the room.
     */
    public void addSensor(Sensor sensor) throws DuplicateSensorException {
        // Check if sensor type already exists in the room and throw exception
        for (Sensor existingSensor : getSensors()) {
            if (getSensorName(existingSensor).equals(getSensorName(sensor))) {
                throw new DuplicateSensorException();
            }
        }
        // Add the sensor to the list and sort the list in alphabetical order
        sensorList.add(sensor);
        Collections.sort(sensorList, new Comparator<Sensor>() {
            @Override
            public int compare(Sensor sensor1, Sensor sensor2) {
                String sensor1Name = getSensorName(sensor1);
                String sensor2Name = getSensorName(sensor2);
                return sensor1Name.compareTo(sensor2Name);
            }
        });
    }

    /**
     * @return The string representation of the room and its associated
     *         details.
     */
    @Override
    public String toString(){
        String twoDecimalArea = String.format("%.2f", getArea());
        return "Room #" + getRoomNumber() + ": type=" + getType() +
                ", area=" + twoDecimalArea + "m^2, sensors=" +
                getSensors().size();
    }
}
