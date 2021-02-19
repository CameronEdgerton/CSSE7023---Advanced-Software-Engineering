package bms.sensors;

/**
 * A class representing a temperature sensor.
 */
public class TemperatureSensor extends TimedSensor implements HazardSensor{

    /**
     * Constructs a temperature sensor
     * @param sensorReadings  A non-empty array of sensor readings
     */
    public TemperatureSensor(int[] sensorReadings){
        super(sensorReadings, 1);
    }

    /**
     * Retrieves the hazard level based on the current sensor reading.
     *
     * If the temperature is above 68 degrees, the hazard level is 100.
     * Otherwise, it is 0.
     *
     * @return the current hazard level.
     */
    @Override
    public int getHazardLevel() {
        int result;
        if (getCurrentReading() >= 68) {
            result = 100;
        } else {
            result = 0;
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
        sensorDetails += " type=TemperatureSensor";
        return sensorDetails;
    }
}
