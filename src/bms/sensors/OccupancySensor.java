package bms.sensors;

/**
 * A class representing an occupancy sensor
 */
public class OccupancySensor extends TimedSensor implements HazardSensor {

    // The maximum allowable number of people in the room
    private int capacity;

    /**
     * Constructs an occupancy sensor.
     *
     * @param sensorReadings a non-empty array of sensor readings
     * @param updateFrequency how often the sensor readings update, in minutes
     * @param capacity the maximum allowable number of people in the room
     */
    public OccupancySensor(int[] sensorReadings, int updateFrequency,
            int capacity) {
        super(sensorReadings, updateFrequency);
        this.capacity = capacity;

        // Throws an exception disallowing the room capacity from being below 0.
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the capacity of the room.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the hazard level based on the ratio of the current reading to
     * the capacity of the room.
     *
     * @return the current hazard level as an integer between 0 and 100
     */
    @Override
    public int getHazardLevel() {
        int result;
        // Calculate the ratio and round it to the nearest integer
        float ratio = (float) getCurrentReading()/getCapacity() * 100;
        int hazardLevel = Math.round(ratio);

        if (hazardLevel >= 100) {
            result = 100;
        } else {
            result = hazardLevel;
        }
        return result;
    }

    /**
     * @return The string representation of the sensor and its associated
     *         details.
     */
    @Override
    public String toString(){
        String sensorDetails = super.toString();
        sensorDetails += " type=OccupancySensor, capacity=" + getCapacity();
        return sensorDetails;
    }
}
