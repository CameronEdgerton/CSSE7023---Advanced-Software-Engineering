package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.NoiseSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RuleBasedHazardEvaluatorTest {

    private RuleBasedHazardEvaluator rb;
    private List<HazardSensor> sensors;
    private List<HazardSensor> sensorsEmpty;
    private TemperatureSensor t1;
    private TemperatureSensor t2;
    private NoiseSensor n1;
    private OccupancySensor o1;

    @Before
    public void setup() {
        sensors = new ArrayList<>();
        sensorsEmpty = new ArrayList<>();
        t1 = new TemperatureSensor(new int[]{69}); //hazard 100
        t2 = new TemperatureSensor(new int[]{30}); //hazard 0
        n1 = new NoiseSensor(new int[]{67},3); //hazard 81
        o1 = new OccupancySensor(new int[]{14},3,20); //hazard 70
        sensors.add(t1);
        sensors.add(n1);
        sensors.add(o1);

    }

    @Test
    public void testEvalNoSensors() {
        rb = new RuleBasedHazardEvaluator(sensorsEmpty);
        assertEquals(0,rb.evaluateHazardLevel());
    }

    @Test
    public void testEvalOneSensor() {
        sensorsEmpty.add(t1);
        rb = new RuleBasedHazardEvaluator(sensorsEmpty);
        assertEquals(100,rb.evaluateHazardLevel());
    }

    @Test
    public void testEvalNonOcc100() {
        rb = new RuleBasedHazardEvaluator(sensors);
        assertEquals(100, rb.evaluateHazardLevel());
    }

    @Test
    public void testEvalNoOcc() {
        sensorsEmpty.add(t2);
        sensorsEmpty.add(n1);
        rb = new RuleBasedHazardEvaluator(sensorsEmpty);
        assertEquals(41, rb.evaluateHazardLevel());
    }

    @Test
    public void testEvalOcc() {
        sensorsEmpty.add(t2);
        sensorsEmpty.add(n1);
        sensorsEmpty.add(o1);
        rb = new RuleBasedHazardEvaluator(sensorsEmpty);
        assertEquals(28, rb.evaluateHazardLevel());
    }

    @Test
    public void testToString() {
        rb = new RuleBasedHazardEvaluator(sensorsEmpty);
        assertEquals("RuleBased", rb.toString());
    }
}
