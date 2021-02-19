package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.NoiseSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class WeightingBasedHazardEvaluatorTest {

    private WeightingBasedHazardEvaluator wb;
    private List<HazardSensor> sensors;
    private List<HazardSensor> sensorsEmpty;
    private TemperatureSensor t1;
    private TemperatureSensor t2;
    private NoiseSensor n1;
    private OccupancySensor o1;
    private Map<HazardSensor,Integer> map;
    private Map<HazardSensor,Integer> map2;
    private List<Integer> weights;

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
        map = new LinkedHashMap<>();
        map.put(t1,25);
        map.put(t2,25);
        map.put(n1,35);
        map.put(o1,15);
        map2 = new HashMap<>();
        map2.put(t1,25);
        map2.put(t2,25);
        map2.put(n1,35);
        map2.put(o1,10);
        wb = new WeightingBasedHazardEvaluator(map);
        weights = new ArrayList<>();
        weights.add(25);
        weights.add(25);
        weights.add(35);
        weights.add(15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadWeightings() {
        wb = new WeightingBasedHazardEvaluator(map2);
    }

    @Test
    public void testEval() {
        assertEquals(64, wb.evaluateHazardLevel());
    }

    @Test
    public void testGetWeightings() {
        assertEquals(weights, wb.getWeightings());
    }

    @Test
    public void testToString() {
        assertEquals("WeightingBased", wb.toString());
    }

}
