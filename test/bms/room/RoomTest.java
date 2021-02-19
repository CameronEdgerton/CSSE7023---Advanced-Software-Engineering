package bms.room;

import bms.exceptions.DuplicateSensorException;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.sensors.CarbonDioxideSensor;
import bms.sensors.HazardSensor;
import bms.sensors.NoiseSensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class RoomTest {

    private Room room;
    private Room roomGood;
    private Room roomBad;
    private NoiseSensor noiseSensor;
    private NoiseSensor noiseSensor2;
    private CarbonDioxideSensor carbonDioxideSensor;
    private TemperatureSensor t1;
    private RuleBasedHazardEvaluator rb;
    private List<HazardSensor> sensors;


    @Before
    public void setup(){
        room = new Room(1, RoomType.OFFICE, 20);
        roomGood = new Room(1, RoomType.OFFICE, 20);
        roomBad = new Room(2, RoomType.OFFICE, 20);
        noiseSensor = new NoiseSensor(new int[]{82,2,3},3);
        noiseSensor2 = new NoiseSensor(new int[]{50,5,1},2);
        carbonDioxideSensor = new CarbonDioxideSensor(new int[]{1,2,3}, 2, 4,
                2);
        sensors = new ArrayList<>();
        t1 = new TemperatureSensor(new int[]{69}); //hazard 100
        sensors.add(noiseSensor);
        rb = new RuleBasedHazardEvaluator(sensors);
    }

    @Test
    public void testSetMaint() {
        assertFalse(room.maintenanceOngoing());
        room.setMaintenance(true);
        assertTrue(room.maintenanceOngoing());
    }

    @Test
    public void testGetHazardEval() {
        assertEquals(null, room.getHazardEvaluator());
        room.setHazardEvaluator(rb);
        assertEquals(rb, room.getHazardEvaluator());
    }

    @Test
    public void testAddSensor() throws DuplicateSensorException {
        room.setHazardEvaluator(rb);
        room.addSensor(noiseSensor);
        assertEquals(null, room.getHazardEvaluator());
    }

    @Test
    public void testEvalRoomStateTemp() throws DuplicateSensorException {
        assertEquals(RoomState.OPEN, room.evaluateRoomState());
        room.addSensor(t1);
        assertEquals(RoomState.EVACUATE, room.evaluateRoomState());
    }

    @Test
    public void testEvalRoomStateFireDrill() {
        room.setFireDrill(true);
        assertEquals(RoomState.EVACUATE, room.evaluateRoomState());
    }

    @Test
    public void testEvalRSMain() throws DuplicateSensorException {
        room.setMaintenance(true);
        assertEquals(RoomState.MAINTENANCE, room.evaluateRoomState());
        room.addSensor(t1);
        assertEquals(RoomState.EVACUATE, room.evaluateRoomState());
    }

    @Test
    public void testEquals() throws DuplicateSensorException {
        assertEquals(roomGood, room);
        roomGood.addSensor(t1);
        assertFalse(roomGood.equals(room));
        assertFalse(room.equals(roomBad));
        room.addSensor(noiseSensor);
        roomGood.addSensor(noiseSensor);
        room.addSensor(t1);
        assertEquals(roomGood, room);
    }

    @Test
    public void testHashCode() throws DuplicateSensorException {
        assertEquals(room.hashCode(), roomGood.hashCode());
        room.addSensor(t1);
        assertFalse(room.hashCode() == roomGood.hashCode());
        roomGood.addSensor(noiseSensor);
        assertFalse(room.hashCode() == roomGood.hashCode());
    }

    @Test
    public void testBadHash() {
        assertFalse(room.hashCode() == roomBad.hashCode());
    }

}
