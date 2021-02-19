package bms.util;

import bms.building.Building;
import bms.exceptions.DuplicateSensorException;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.NoiseSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;

public class StudyRoomRecommenderTest {

    private StudyRoomRecommender sr;
    private Building building;
    private Building buildingNoneOnF1;
    private Floor floor1;
    private Floor floor2;
    private Floor floor3;
    private Room room1;
    private Room room2;
    private Room room3;
    private TemperatureSensor t1;
    private NoiseSensor n1;
    private OccupancySensor o1;

    @Before
    public void setup() throws DuplicateSensorException {
        sr = new StudyRoomRecommender();
        building = new Building("building");
        floor1 = new Floor(1,10,10);
        floor2 = new Floor(2,10,10);
        floor3 = new Floor(3,10,10);
        room1 = new Room(1, RoomType.STUDY,20);
        room2 = new Room(2, RoomType.LABORATORY,20);
        room3 = new Room(3, RoomType.STUDY,20);
        t1 = new TemperatureSensor(new int[]{21}); //comfort 100
        n1 = new NoiseSensor(new int[]{67},3); //comfort 18
        o1 = new OccupancySensor(new int[]{14},3,20); //comfort 30
    }

}
