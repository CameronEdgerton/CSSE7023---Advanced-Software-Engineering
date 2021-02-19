package bms.floor;

import bms.exceptions.DuplicateSensorException;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MaintenanceScheduleTest {

    private Room room1;
    private Room room2;
    private Room room3;
    private Room room4;
    private Sensor sensor1;
    private Sensor sensor2;
    private Sensor sensor3;
    private Sensor sensor4;
    private List<Room> roomOrder;
    private MaintenanceSchedule ms;

    @Before
    public void setup() throws DuplicateSensorException {
        room1 = new Room(1, RoomType.OFFICE, 20);
        room2 = new Room(2, RoomType.STUDY, 15.6);
        room3 = new Room(3, RoomType.LABORATORY, 27);
        room4 = new Room(4, RoomType.OFFICE, 20);
        room4.setFireDrill(true);
        sensor1 = new TemperatureSensor(new int[]{25, 34, 61, 85});
        sensor2 = new CarbonDioxideSensor(new int[]{690, 740}, 5, 700, 150);
        sensor3 = new NoiseSensor(new int[]{55, 62, 69, 63}, 3);
        sensor4 = new OccupancySensor(new int[]{32, 35, 26, 4, 3, 2, 6, 16, 17, 22, 28,
                29}, 2, 40);
        room1.addSensor(sensor1);
        room2.addSensor(sensor1);
        room2.addSensor(sensor2);
        room3.addSensor(sensor3);
        room4.addSensor(sensor3);
        room4.addSensor(sensor4);
        roomOrder = new ArrayList<>();
        roomOrder.add(room1);
        roomOrder.add(room3);
        roomOrder.add(room2);
        roomOrder.add(room4);
        ms = new MaintenanceSchedule(roomOrder);
    }


    @Test
    public void testFirstRoomInMaint() {
        assertTrue(room1.maintenanceOngoing());
        assertFalse(room2.maintenanceOngoing());
        assertFalse(room3.maintenanceOngoing());
        assertFalse(room4.maintenanceOngoing());
    }

    @Test
    public void testGetMaintTime() {
        assertEquals(12, ms.getMaintenanceTime(room1));
        assertEquals(7, ms.getMaintenanceTime(room2));
        assertEquals(19, ms.getMaintenanceTime(room3));
    }

    @Test
    public void testGetCurrentRoom() {
        assertEquals(room1, ms.getCurrentRoom());
    }

    @Test
    public void testGetTimeElapsedCurrentRoom() {
        //test no time elapsed
        assertEquals(0, ms.getTimeElapsedCurrentRoom());
        //elapse 3 seconds and check
        for (int i = 0; i < 3; i++) {
            ms.elapseOneMinute();
        }
        assertEquals(3, ms.getTimeElapsedCurrentRoom());
        // elapse 13 and check
        for (int i = 0; i < 9; i++) {
            ms.elapseOneMinute();
        }
        assertEquals(12, ms.getTimeElapsedCurrentRoom());
    }

    @Test
    public void testElapseOneMinuteIncrementing() {
        assertEquals(0, ms.getTimeElapsedCurrentRoom());
        ms.elapseOneMinute();
        assertEquals(1, ms.getTimeElapsedCurrentRoom());
    }

    @Test
    public void testElapseOneMinuteDoingNothingIfEvacuate() {
        assertEquals(0, ms.getTimeElapsedCurrentRoom());
        ms.elapseOneMinute();
        assertEquals(1, ms.getTimeElapsedCurrentRoom());
        room1.setFireDrill(true);
        assertEquals(RoomState.EVACUATE, room1.evaluateRoomState());
        ms.elapseOneMinute();
        assertEquals(1, ms.getTimeElapsedCurrentRoom());
    }

    @Test
    public void testElapseOneMinuteSetNextRoom() {
        assertEquals(0, ms.getTimeElapsedCurrentRoom());
        for (int i = 0; i < 12; i++) {
            ms.elapseOneMinute();
        }
        assertEquals(12, ms.getTimeElapsedCurrentRoom());
        ms.elapseOneMinute();
        assertFalse(room1.maintenanceOngoing());
        assertTrue(room3.maintenanceOngoing());
        assertEquals(room3, ms.getCurrentRoom());
    }

    @Test
    public void testSkipCurrentMaintenance() {
        assertEquals(room1, ms.getCurrentRoom());
        assertTrue(room1.maintenanceOngoing());
        ms.elapseOneMinute();
        assertEquals(1, ms.getTimeElapsedCurrentRoom());
        ms.skipCurrentMaintenance();
        assertEquals(room3, ms.getCurrentRoom());
        assertEquals(0, ms.getTimeElapsedCurrentRoom());
        assertFalse(room1.maintenanceOngoing());
        assertTrue(room3.maintenanceOngoing());
    }

    @Test
    public void testSkipMaintEndOfList() {
        ms.skipCurrentMaintenance();
        ms.skipCurrentMaintenance();
        ms.skipCurrentMaintenance();
        assertTrue(room4.maintenanceOngoing());
        ms.skipCurrentMaintenance();
        assertTrue(room1.maintenanceOngoing());
    }

    @Test
    public void testToString() {
        assertEquals("MaintenanceSchedule: currentRoom=#1, currentElapsed=0",
                ms.toString());
    }

    @Test
    public void testToStringIncrementTime() {
        ms.elapseOneMinute();
        assertEquals("MaintenanceSchedule: currentRoom=#1, currentElapsed=1",
                ms.toString());
    }

    @Test
    public void testToStringDifferentRoom() {
        ms.skipCurrentMaintenance();
        assertEquals("MaintenanceSchedule: currentRoom=#3, currentElapsed=0",
                ms.toString());
    }

    @Test
    public void testEncode() {
        assertEquals("1,3,2,4", ms.encode());
    }












}
