package bms.sensors;

/**
 * A class representing a noise sensor.
 */
public class NoiseSensor extends TimedSensor implements HazardSensor {

    /**
     * Constructs a noise sensor
     *
     * @param sensorReadings array of noise sensor readings in decibels
     * @param updateFrequency how often the sensor readings update, in minutes
     */
    public NoiseSensor(int[] sensorReadings, int updateFrequency) {
        super(sensorReadings, updateFrequency);
    }

    /**
     * Calculates the relative loudness level compared to a reference of 70.0
     * decibels
     *
     * @return the relative loudness of the current reading
     */
    public double calculateRelativeLoudness() {
        return  Math.pow(2, ((getCurrentReading()-70.0)/10.0));
    }

    /**
     * Returns the hazard level based on the relative loudness of the current
     * reading.
     *
     * @return level of hazard as an integer between 0 and 100.
     */
    @Override
    public int getHazardLevel() {
        int result;
        int hazardLevel = (int) Math.floor(calculateRelativeLoudness() * 100);
        if (hazardLevel > 100) {
            result = 100;
        } else {
            result = hazardLevel;
        }
        return result;
    }

    /**
     *
     * @return The string representation of the sensor and its associated
     *         details.
     */
    @Override
    public String toString(){
        String sensorDetails = super.toString();
        sensorDetails += " type=NoiseSensor";
        return sensorDetails;
    }

}
