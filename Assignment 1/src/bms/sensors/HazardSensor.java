package bms.sensors;

/**
 * Interface representing a hazard sensor
 */
public interface HazardSensor extends Sensor {

    /**
     * Returns the hazard level detected by the sensor.
     *
     * A value of 0 represents a very low hazard, and a value of 100 indicates
     * very high hazard.
     *
     * @return hazard level as an integer between 0 and 100
     */
    int getHazardLevel();
}
