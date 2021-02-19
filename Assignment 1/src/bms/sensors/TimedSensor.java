package bms.sensors;

import bms.util.TimedItem;
import bms.util.TimedItemManager;

/**
 * Abstract class representing a timed sensor.
 */
public abstract class TimedSensor implements TimedItem, Sensor {

    // A non-empty array of sensor readings
    private int[] sensorReadings;
    // Indicates how often the sensor readings updates, in minutes
    private int updateFrequency;
    // The current time (in minutes) which has elapsed
    private int timeElapsed;
    // The current reading of the sensor in the sensor readings array
    private int currentReading;
    // The current index in the sensor readings array of the current reading
    private int currentIndex;

    /**
     * Constructs a timed sensor.
     *
     * @param sensorReadings a non-empty array of sensor readings
     * @param updateFrequency how often the sensor readings updates, in minutes
     * @throws IllegalArgumentException thrown when updateFrequency is less
     *                                  than 1 or greater than 5, if there
     *                                  are no sensor readings, if the sensor
     *                                  readings array is null, or if there
     *                                  is a sensor reading less than 0.
     */
    public TimedSensor(int[] sensorReadings, int updateFrequency)
            throws IllegalArgumentException {
        this.sensorReadings = sensorReadings;
        this.updateFrequency = updateFrequency;
        // Set time elapsed to 0 by default.
        this.timeElapsed = 0;
        // Set the current reading index to 0 by default.
        this.currentIndex = 0;

        // Handle exceptions and register the sensor to the timed item manager.
        for (int sensorReading : sensorReadings) {
            if (sensorReading < 0) {
                throw new IllegalArgumentException();
            }
        }
        if (updateFrequency < 1 | updateFrequency > 5 | sensorReadings == null |
                sensorReadings.length == 0) {
            throw new IllegalArgumentException();
        } else {
            TimedItemManager.getInstance().registerTimedItem(this);
        }
    }

    /**
     * @return The current reading observed by the sensor
     */
    @Override
    public int getCurrentReading() {
        currentReading = sensorReadings[currentIndex];
        return currentReading;
    }

    /**
     * @return The current time in minutes which have elapsed since the sensor
     *         was instantiated.
     */
    public int getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * @return The number of minutes in between updates to the current sensor
     *         reading
     */
    public int getUpdateFrequency() {
        return updateFrequency;
    }

    /**
     * Increments the time elapsed (in minutes) by one and updates the current
     * sensor reading to the next value in the array if enough time has
     * elapsed to trigger an update.
     *
     * Resets the current reading to the first position in the array if
     * sufficient time has elapsed such that the end of the array has been
     * reached.
     */
    @Override
    public void elapseOneMinute() {
        timeElapsed += 1;
        if (getTimeElapsed() % getUpdateFrequency() == 0) {
            if (currentIndex < sensorReadings.length - 1) {
                currentIndex++;
            } else {
                currentIndex = 0;
            }
        }
    }

    /**
     * @return The string representation of the sensor and its associated
     *         details.
     */
    @Override
    public String toString(){
        String sensorDetails = "TimedSensor: freq=" + getUpdateFrequency() +
                ", readings=";
        for (int reading : sensorReadings) {
            sensorDetails += reading + ",";
        }
        return sensorDetails;
    }
}
