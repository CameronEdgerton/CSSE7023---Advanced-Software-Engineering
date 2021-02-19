package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTest {

    private Building buildingWithFloors;
    private Building buildingGood;
    private Building buildingBad;
    private Building buildingNoFloors;
    private Floor floor1;
    private Floor floor2;
    private Floor floor3;
    private Floor bigFloor;
    private Floor duplicate2;
    private Room office1;
    private Room study1;
    private Room office2;
    private Room office3;
    private Floor floor1NoRooms;


    @Before
    public void setup() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException, InsufficientSpaceException, DuplicateRoomException {
        buildingWithFloors = new Building("BuildingWithFloors");
        buildingGood = new Building("BuildingWithFloors");
        buildingBad = new Building("BuildingBad");
        buildingNoFloors = new Building("BuildingNoFloors");
        floor1 = new Floor(1, 20, 20);
        floor2 = new Floor(2, 18, 18);
        floor3 = new Floor(3, 18, 18);
        bigFloor = new Floor(3, 19, 19);
        duplicate2 = new Floor(2, 10, 10);
        floor1NoRooms = new Floor(1, 10, 10);
        office1 = new Room(1, RoomType.OFFICE, 20);
        study1 = new Room(2, RoomType.STUDY, 20);
        office2 = new Room(3, RoomType.OFFICE, 20);
        office3 = new Room(3, RoomType.OFFICE, 324);
        floor1.addRoom(office1);
        floor1.addRoom(study1);
        floor2.addRoom(office2);
        buildingWithFloors.addFloor(floor1);
        buildingWithFloors.addFloor(floor2);
        buildingWithFloors.addFloor(floor3);
        buildingGood.addFloor(floor1);
        buildingGood.addFloor(floor2);
        buildingGood.addFloor(floor3);
    }

    @Test(expected = FloorTooSmallException.class)
    public void testRenoSmallBelow() throws FloorTooSmallException {
        buildingWithFloors.renovateFloor(3,19,18);
    }

    @Test(expected = FloorTooSmallException.class)
    public void testRenoTooBigAbove() throws FloorTooSmallException {
        buildingWithFloors.renovateFloor(2,17,18);
    }

    @Test(expected = FloorTooSmallException.class)
    public void testRenoTooBigRooms() throws FloorTooSmallException, InsufficientSpaceException, DuplicateRoomException {
        floor3.addRoom(office3);
        buildingWithFloors.renovateFloor(3,17,18);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenoMinWidth() throws FloorTooSmallException {
        buildingWithFloors.renovateFloor(3,4,18);
    }

    @Test
    public void testRenoWorking() throws FloorTooSmallException {
        assertEquals(324, floor3.calculateArea(), 0.001);
        buildingWithFloors.renovateFloor(3,10,10);
        assertEquals(100, floor3.calculateArea(), 0.001);
    }

    @Test
    public void testEqualsWithFloors() {
        assertTrue(buildingGood.equals(buildingWithFloors));
    }

    @Test
    public void testEqualsDifName() throws DuplicateFloorException, NoFloorBelowException, FloorTooSmallException {
        assertFalse(buildingGood.equals(buildingBad));
        buildingBad.addFloor(floor1);
        assertFalse(buildingGood.equals(buildingBad));
        buildingBad.addFloor(floor2);
        buildingBad.addFloor(floor3);
        assertFalse(buildingGood.equals(buildingBad));
    }

    @Test
    public void testHashCode() {
        assertEquals(buildingGood.hashCode(), buildingWithFloors.hashCode());
        assertFalse(buildingGood.hashCode() == (buildingBad.hashCode()));
    }






}
