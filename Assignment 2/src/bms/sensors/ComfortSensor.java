package bms.sensors;

/**
 * Interface representing a Comfort Sensor.
 */
public interface ComfortSensor extends Sensor {

    /**
     * @return the comfort level in a location as detected by this sensor
     * (as a percentage).
     */
    int getComfortLevel();
}
