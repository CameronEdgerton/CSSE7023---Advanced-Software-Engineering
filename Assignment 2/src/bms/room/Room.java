package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.sensors.HazardSensor;
import bms.sensors.Sensor;
import bms.util.Encodable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a room on a floor of a building.
 * <p>
 * Each room has a room number (unique for this floor, ie. no two rooms on the
 * same floor can have the same room number), a type to indicate its intended
 * purpose, and a total area occupied by the room in square metres.
 * <p>
 * Rooms also need to record whether a fire drill is currently taking place in
 * the room.
 * <p>
 * Rooms can have one or more sensors to monitor hazard levels
 * in the room.
 * @ass1
 */
public class Room implements Encodable {

    /**
     * Unique room number for this floor.
     */
    private int roomNumber;

    /**
     * The type of room. Different types of rooms can be used for different
     * activities.
     */
    private RoomType type;

    /**
     * List of sensors located in the room. Rooms may only have up to one of
     * each type of sensor. Alphabetically sorted by class name.
     */
    private List<Sensor> sensors;

    /**
     * Area of the room in square metres.
     */
    private double area;

    /**
     * Minimum area of all rooms, in square metres.
     * (Note that dimensions of the room are irrelevant).
     * Defaults to 5.
     */
    private static final int MIN_AREA = 5;

    /**
     * Records whether there is currently a fire drill.
     */
    private boolean fireDrill;

    /**
     * Hazard Evaluator of the room.
     */
    private HazardEvaluator hEvaluator;

    /**
     * Maintenance status of the room.
     */
    private boolean maintenance;

    /**
     * Creates a new room with the given room number.
     *
     * @param roomNumber the unique room number of the room on this floor
     * @param type the type of room
     * @param area the area of the room in square metres
     * @ass1
     */
    public Room(int roomNumber, RoomType type, double area) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.area = area;
        this.sensors = new ArrayList<>();
        this.fireDrill = false;
        this.maintenance = false;
    }

    /**
     * Returns room number of the room.
     *
     * @return the room number on the floor
     * @ass1
     */
    public int getRoomNumber() {
        return this.roomNumber;
    }

    /**
     * Returns area of the room.
     *
     * @return the room area in square metres
     * @ass1
     */
    public double getArea() {
        return this.area;
    }

    /**
     * Returns the minimum area for all rooms.
     * <p>
     * Rooms must be at least 5 square metres in area.
     *
     * @return the minimum room area in square metres
     * @ass1
     */
    public static int getMinArea() {
        return MIN_AREA;
    }

    /**
     * Returns the type of the room.
     *
     * @return the room type
     * @ass1
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Returns whether there is currently a fire drill in progress.
     *
     * @return current status of fire drill
     * @ass1
     */
    public boolean fireDrillOngoing() {
        return this.fireDrill;
    }

    public boolean maintenanceOngoing() {
        return this.maintenance;
    }

    /**
     * Returns the list of sensors in the room.
     * <p>
     * The list of sensors stored by the room should always be in alphabetical
     * order, by the sensor's class name.
     * <p>
     * Adding or removing sensors from this list should not affect the room's
     * internal list of sensors.
     *
     * @return list of all sensors in alphabetical order of class name
     * @ass1
     */
    public List<Sensor> getSensors() {
        return new ArrayList<>(this.sensors);
    }

    /**
     * Change the status of the fire drill to the given value.
     *
     * @param fireDrill whether there is a fire drill ongoing
     * @ass1
     */
    public void setFireDrill(boolean fireDrill) {
        this.fireDrill = fireDrill;
    }

    /**
     * Change the status of maintenance to the given value.
     *
     * @param maintenance whether there is maintenance ongoing
     */
    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    /**
     * Returns this room's hazard evaluator, or null if none exists
     *
     * @return room's hazard evaluator
     */
    public HazardEvaluator getHazardEvaluator() {
        if (this.hEvaluator != null) {
            return hEvaluator;
        } else {
            return null;
        }
    }

    /**
     * Sets the room's hazard evaluator to a new hazard evaluator.
     *
     * @param hazardEvaluator new hazard evaluator for the room to use
     */
    public void setHazardEvaluator(HazardEvaluator hazardEvaluator) {
        this.hEvaluator = hazardEvaluator;
    }

    /**
     * Return the given type of sensor if there is one in the list of sensors;
     * return null otherwise.
     *
     * @param sensorType the type of sensor which matches the class name
     *                   returned by the getSimpleName() method,
     *                   e.g. "NoiseSensor" (no quotes)
     * @return the sensor in this room of the given type; null if none found
     * @ass1
     */
    public Sensor getSensor(String sensorType) {
        for (Sensor s : this.getSensors()) {
            if (s.getClass().getSimpleName().equals(sensorType)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Adds a sensor to the room if a sensor of the same type is not
     * already in the room.
     * <p>
     * The list of sensors should be sorted after adding the new sensor, in
     * alphabetical order by simple class name ({@link Class#getSimpleName()}).
     *
     * @param sensor the sensor to add to the room
     * @throws DuplicateSensorException if the sensor to add is of the
     * same type as a sensor already in this room
     * @ass1
     */
    public void addSensor(Sensor sensor)
            throws DuplicateSensorException {
        for (Sensor s : sensors) {
            if (s.getClass().equals(sensor.getClass())) {
                throw new DuplicateSensorException(
                        "Duplicate sensor of type: "
                                + s.getClass().getSimpleName());
            }
        }
        sensors.add(sensor);
        sensors.sort(Comparator.comparing(s -> s.getClass().getSimpleName()));
        setHazardEvaluator(null);
    }

    /**
     * Evaluates the room status based upon current information.
     *
     * @return current room status
     */
    public RoomState evaluateRoomState() {
        for (Sensor sensor : getSensors()) {
            if (sensor.getClass().getSimpleName().equals("TemperatureSensor")) {
                if (((HazardSensor) sensor).getHazardLevel() == 100) {
                    return RoomState.EVACUATE;
                }
            }
        }
        if (fireDrillOngoing() == true) {
            return RoomState.EVACUATE;
        } else if (maintenanceOngoing() == true &&
                fireDrillOngoing() == false) {
            return RoomState.MAINTENANCE;
        } else {
            return RoomState.OPEN;
        }
    }

    /**
     * Returns true if and only if this room is equal to the other given room.
     *
     * @param obj object to evaluate
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Room)) {
            return false;
        }
        Room otherRoom = (Room) obj;

        for (Sensor sensor : otherRoom.getSensors()) {
            if (!this.getSensors().contains(sensor)) {
                return false;
            }
        }

        return this.roomNumber == otherRoom.roomNumber &&
                this.type == otherRoom.type &&
                Math.abs(this.getArea() - otherRoom.getArea()) <= 0.001 &&
                this.getSensors().size() == otherRoom.getSensors().size();
    }

    /**
     * @return hash code of this room
     */
    @Override
    public int hashCode() {
        return (int) this.area + this.roomNumber + this.type.hashCode() +
                this.getSensors().size() + this.getSensors().hashCode();
    }

    /**
     * Returns the human-readable string representation of this room.
     * <p>
     * The format of the string to return is
     * "Room #'roomNumber': type='roomType', area='roomArea'm^2,
     * sensors='numSensors'"
     * without the single quotes, where 'roomNumber' is the room's unique
     * number, 'roomType' is the room's type, 'area' is the room's type,
     * 'numSensors' is the number of sensors in the room.
     * <p>
     * The room's area should be formatted to two (2) decimal places.
     * <p>
     * For example:
     * "Room #42: type=STUDY, area=22.50m^2, sensors=2"
     *
     * @return string representation of this room
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("Room #%d: type=%s, area=%.2fm^2, sensors=%d",
                this.roomNumber,
                this.type,
                this.area,
                this.sensors.size());
    }

    /**
     * Returns the machine-readable string representation of this room and
     * all of its sensors.
     *
     * @return encoded string representation of this room
     */
    public String encode() {
        String output = String.format("%d:%s:%.2f:%d", this.roomNumber,
                this.type, this.getArea(), this.getSensors().size());

        if (this.getHazardEvaluator() != null) {
            output += ":" + this.getHazardEvaluator().toString() +
                    System.lineSeparator();
        } else {
            output += System.lineSeparator();
        }

        if (this.getHazardEvaluator() != null) {
            if (this.getHazardEvaluator().toString().equals("WeightingBased")) {
                List weights = ((WeightingBasedHazardEvaluator) hEvaluator)
                                .getWeightings();
                for (Sensor sensor : getSensors()) {
                    if (sensor instanceof Encodable) {
                        int index = getSensors().indexOf(sensor);
                        output += ((Encodable) sensor).encode() + "@" +
                                weights.get(index) + System.lineSeparator();
                    }
                }
            } else {
                for (Sensor sensor : getSensors()) {
                    if (sensor instanceof Encodable) {
                        output += ((Encodable) sensor).encode()
                                + System.lineSeparator();
                    }
                }
            }
        } else {
            for (Sensor sensor : getSensors()) {
                if (sensor instanceof Encodable) {
                    output += ((Encodable) sensor).encode()
                            + System.lineSeparator();
                }
            }
        }
        return output.stripTrailing();
    }
}