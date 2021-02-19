package bms.sensors;

/**
 * A class representing a carbon dioxide sensor.
 */
public class CarbonDioxideSensor extends TimedSensor implements HazardSensor {

    // The ideal CO2 value in ppm
    private int idealValue;
    // The acceptable range above and below ideal value in ppm
    private int variationLimit;

    /**
     * Constructs a carbon dioxide sensor.
     *
     * @param sensorReadings array of CO2 sensor readings in ppm
     * @param updateFrequency how often the sensor readings update, in minutes
     * @param idealValue ideal CO2 value in ppm
     * @param variationLimit acceptable range above and below ideal value in
     *                       ppm
     * @throws IllegalArgumentException if the ideal value or variation limit
     *                                  are less than or equal to 0, or if the
     *                                  ideal value less the variation
     *                                  limit is less than 0.
     */
    public CarbonDioxideSensor(int[] sensorReadings, int updateFrequency,
            int idealValue, int variationLimit) throws
            IllegalArgumentException {
        super(sensorReadings, updateFrequency);
        this.idealValue = idealValue;
        this.variationLimit = variationLimit;

        if (idealValue <= 0 | variationLimit <= 0 | (idealValue -
                variationLimit) < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the variation limit
     */
    public int getVariationLimit() {
        return variationLimit;
    }

    /**
     * @return the ideal value
     */
    public int getIdealValue() {
        return idealValue;
    }

    /**
     * Returns the hazard level as detected by the sensor, based on the
     * current reading.
     *
     * @return the hazard level as an integer between 0 and 100.
     */
    @Override
    public int getHazardLevel() {
        int sensorReading = getCurrentReading();
        int result;

        if (sensorReading >= 0 && sensorReading <= 999) {
            result = 0;
        } else if (sensorReading >= 1000 && sensorReading <= 1999) {
            result = 25;
        } else if (sensorReading >= 2000 && sensorReading <= 4999) {
            result = 50;
        } else {
            result = 100;
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
        sensorDetails += " type=CarbonDioxideSensor, idealPPM=" +
                getIdealValue() + ", varLimit=" + getVariationLimit();
        return sensorDetails;
    }
}
