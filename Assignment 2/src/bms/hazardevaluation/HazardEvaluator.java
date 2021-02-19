package bms.hazardevaluation;

/**
 * Interface representing a hazard evaluator
 */
public interface HazardEvaluator {

    /**
     * Calculates a hazard level between 0 and 100.
     *
     * @return the hazard level, between 0 and 100 (inclusive)
     */
    int evaluateHazardLevel();
}
