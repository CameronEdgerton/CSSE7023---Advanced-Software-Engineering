package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.Sensor;
import java.lang.Math;
import java.util.List;

/**
 * Represents a rule based hazard evaluator.
 */
public class RuleBasedHazardEvaluator implements HazardEvaluator {

    /**
     * List of hazard sensors
     */
    private List<HazardSensor> sensors;

    /**
     * Constructs a rule based hazard evaluator
     * @param sensors list of sensors with which to create the hazard evaluator
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        this.sensors = sensors;
    }

    /**
     * Returns a calculated hazard level based on applying a set of rules to
     * the list of sensors passed to the constructor.
     *
     * @return calculated hazard level according to a set of rules
     */
    @Override
    public int evaluateHazardLevel() {
        int count = 0;
        int sum = 0;
        float average;
        int hazardLevel;
        float occupancyConstant = 1;

        if (sensors.isEmpty()) {
            return 0;
        } else if (sensors.size() == 1) {
            return sensors.get(0).getHazardLevel();
        } else {
            for (Sensor sensor : sensors) {
                if (sensor.getClass().getSimpleName().equals(
                        "OccupancySensor")) {
                    occupancyConstant =
                            (float) ((HazardSensor) sensor).getHazardLevel()
                            / 100;
                } else if (!sensor.getClass().getSimpleName().equals(
                        "OccupancySensor") &&
                        ((HazardSensor) sensor).getHazardLevel()
                        == 100) {
                        return 100;
                } else {
                    count += 1;
                    sum += ((HazardSensor) sensor).getHazardLevel();
                }
            }
            average = ((float) sum / count) * occupancyConstant;
            hazardLevel = Math.round(average);
        }
        return hazardLevel;
    }

    /**
     * @return string representation of this room
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}
