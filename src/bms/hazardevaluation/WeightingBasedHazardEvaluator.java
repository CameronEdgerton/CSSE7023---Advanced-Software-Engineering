package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a weighting based hazard evaluator
 */
public class WeightingBasedHazardEvaluator implements HazardEvaluator {

    /**
     * Map containing the sensors and their weightings
     */
    private Map<HazardSensor, Integer> sensors;

    /**
     * Constructs a new weighting based hazard evaluator
     * @param sensors map with which to create the hazard evaluator
     * @throws IllegalArgumentException if weighting below 0 or above 100, or
     *                                  if sum of weightings is greater than
     *                                  100.
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor,Integer> sensors)
            throws IllegalArgumentException {
        this.sensors = sensors;
        int sum = 0;
        for (int weighting : sensors.values()) {
            if (weighting < 0 || weighting > 100) {
                throw new IllegalArgumentException();
            }
            sum += weighting;
        }
        if (sum != 100) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the weighted average of the current hazard levels of all
     * sensors in the map passed to the constructor.
     *
     * @return weighted average of current sensor hazard levels
     */
    @Override
    public int evaluateHazardLevel() {
        double sumOfAverages = 0;
        for (Map.Entry<HazardSensor,Integer> entry : sensors.entrySet()) {
            sumOfAverages += entry.getKey().getHazardLevel() *
                    ((double) entry.getValue() / 100);
        }
        return (int) Math.round(sumOfAverages);
    }

    /**
     * @return list containing the weightings associated with all of
     *         the sensors monitored by this hazard evaluator.
     */
    public List<Integer> getWeightings() {
        List<Integer> weightingList = new ArrayList<>();

        for (int weighting : sensors.values()) {
            weightingList.add(weighting);
        }
        return weightingList;
    }

    /**
     * @return string representation of this hazard evaluator
     */
    @Override
    public String toString() {
        return "WeightingBased";
    }
}
