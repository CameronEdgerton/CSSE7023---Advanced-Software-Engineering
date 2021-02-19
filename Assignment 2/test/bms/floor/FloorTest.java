package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.exceptions.InsufficientSpaceException;
import bms.room.Room;
import bms.room.RoomType;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FloorTest {

    private Floor floor;
    private Floor floorGood;
    private Floor floorBad;
    private Floor floorWithRooms;
    private Room room1 = new Room(1, RoomType.OFFICE, 10);
    private Room room2 = new Room(2, RoomType.STUDY, 12);
    private Room room4 = new Room(4, RoomType.STUDY, 12);
    private Room smallRoom = new Room(1, RoomType.OFFICE, 4);
    private Room bigRoom = new Room(3,RoomType.OFFICE, 35);
    private Room office2 = new Room(3, RoomType.OFFICE, 15);
    private Room biggerThanFloor = new Room(3, RoomType.LABORATORY, 43);
    private Room duplicate2 = new Room(2, RoomType.LABORATORY, 8);
    private MaintenanceSchedule ms;
    private List<Room> msRooms;

    @Before
    public void setup() throws InsufficientSpaceException, DuplicateRoomException {
        floor = new Floor(1,20,20);
        floorGood = new Floor(1,20,20);
        floorBad = new Floor(1,20.001,20);
        floorWithRooms = new Floor(1, 6, 7);
        floorWithRooms.addRoom(room1);
        floorWithRooms.addRoom(room2);
        floorWithRooms.addRoom(office2);
        msRooms = new ArrayList<>();
        msRooms.add(room1);
        msRooms.add(office2);
        msRooms.add(room2);
    }

    @Test
    public void testGetMaint(){
        assertTrue(floorWithRooms.getMaintenanceSchedule() == null);
        floorWithRooms.createMaintenanceSchedule(msRooms);
        assertTrue(floorWithRooms.getMaintenanceSchedule().encode().equals(
                "1,3,2"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testBadCreate() {
        msRooms.add(room4);
        floorWithRooms.createMaintenanceSchedule(msRooms);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateFail() {
        msRooms.add(room2);
        floorWithRooms.createMaintenanceSchedule(msRooms);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateFail2() {
        msRooms.add(room1);
        floorWithRooms.createMaintenanceSchedule(msRooms);
    }

    @Test
    public void testEqualsMethod() throws InsufficientSpaceException,
            DuplicateRoomException {
        floorGood.addRoom(room1);
        floorBad.addRoom(room1);
        assertTrue(floorGood.equals(floorBad));
    }

    @Test
    public void testHashCode() {

    }
}
