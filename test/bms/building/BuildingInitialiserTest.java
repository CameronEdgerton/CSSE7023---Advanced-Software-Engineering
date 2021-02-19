package bms.building;

import bms.exceptions.FileFormatException;
import bms.floor.Floor;
import bms.floor.MaintenanceSchedule;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.CarbonDioxideSensor;
import bms.sensors.NoiseSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.TemperatureSensor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildingInitialiserTest {

    private BuildingInitialiser buildingInitialiser;
    private BuildingInitialiser testInitialiser;
    private List<Building> buildingList;
    private Building building1;
    private Building building2;
    private Building building3;
    private Floor b1f1;
    private Floor b1f2;
    private Floor b1f5;
    private Floor b2f1;
    private Floor b3f1;
    private Room b1f1r1;
    private Room b1f1r2;
    private Room b1f1r4;
    private Room b1f2r1;
    private Room b1f5r1;
    private Room b2f1r1;

    @Before
    public void setup() throws IOException, FileFormatException {
        buildingInitialiser = new BuildingInitialiser();
        testInitialiser = new BuildingInitialiser();
        buildingList = new ArrayList<>();
        for (Building building : buildingInitialiser.loadBuildings("saves" +
                "/uqstlucia.txt")) {
            buildingList.add(building);
        }
        building1 = buildingList.get(0);
        building2 = buildingList.get(1);
        building3 = buildingList.get(2);
        b1f1 = building1.getFloorByNumber(1);
        b1f2 = building1.getFloorByNumber(2);
        b1f5 = building1.getFloorByNumber(5);
        b2f1 = building2.getFloorByNumber(1);
        b3f1 = building3.getFloorByNumber(1);
        b1f1r1 = b1f1.getRoomByNumber(101);
        b1f1r2 = b1f1.getRoomByNumber(102);
        b1f1r4 = b1f1.getRoomByNumber(104);
        b1f2r1 = b1f2.getRoomByNumber(201);
        b1f5r1 = b1f5.getRoomByNumber(501);
        b2f1r1 = b2f1.getRoomByNumber(101);

    }

    @Test
    public void testLoading() throws IOException, FileFormatException {
        buildingInitialiser = new BuildingInitialiser();
        buildingList = new ArrayList<>();
        for (Building building : buildingInitialiser.loadBuildings("saves/uqstlucia.txt")) {
            buildingList.add(building);
        }
        assertTrue(buildingList.size() == 3);
    }

    @Test
    public void testBuildingsInstance() {
        assertTrue(building1 instanceof Building);
        assertTrue(building2 instanceof Building);
        assertTrue(building3 instanceof Building);
    }

    @Test
    public void testFloorsInstance() {
        assertTrue(b1f1 instanceof Floor);
        assertTrue(b2f1 instanceof Floor);
        assertTrue(b3f1 instanceof Floor);
    }

    @Test
    public void testRoomInstance() {
        assertTrue(b1f1r1 instanceof Room);
        assertTrue(b2f1r1 instanceof Room);
    }

    ///TEST SENSORS INSTANCES
    @Test
    public void testOccupancySensor() {
        assertTrue(b1f1r2.getSensors().get(0) instanceof OccupancySensor);
        OccupancySensor os = (OccupancySensor)b1f1r2.getSensors().get(0);
        assertEquals(13, os.getCurrentReading());
        assertEquals(4, os.getUpdateFrequency());
        assertEquals(30, os.getCapacity());
    }

    @Test
    public void testCarbonDioxideSensor() {
        assertTrue(b1f1r4.getSensors().get(0) instanceof CarbonDioxideSensor);
        CarbonDioxideSensor co2 =
                (CarbonDioxideSensor)b1f1r4.getSensors().get(0);
        assertEquals(690,co2.getCurrentReading());
        assertEquals(5, co2.getUpdateFrequency());
        assertEquals(700, co2.getIdealValue());
        assertEquals(150, co2.getVariationLimit());
    }

    @Test
    public void testNoiseSensor() {
        assertTrue(b1f2r1.getSensors().get(0) instanceof NoiseSensor);
        NoiseSensor ns = (NoiseSensor)b1f2r1.getSensors().get(0);
        assertEquals(55, ns.getCurrentReading());
        assertEquals(3, ns.getUpdateFrequency());
    }

    @Test
    public void testTemperatureSensor() {
        assertTrue(b1f5r1.getSensors().get(1) instanceof TemperatureSensor);
        TemperatureSensor ts = (TemperatureSensor)b1f5r1.getSensors().get(1);
        assertEquals(25, ts.getCurrentReading());
    }

    @Test
    public void testRuleBasedEval() {
        assertTrue(b1f2r1.getHazardEvaluator() instanceof RuleBasedHazardEvaluator);
    }

    @Test
    public void testWeightingBasedEval() {
        assertTrue(b1f5r1.getHazardEvaluator() instanceof WeightingBasedHazardEvaluator);
        WeightingBasedHazardEvaluator wbhe =
                ((WeightingBasedHazardEvaluator) b1f5r1.getHazardEvaluator());
        int weight1 = wbhe.getWeightings().get(0);
        int weight2 = wbhe.getWeightings().get(1);
        assertEquals(25, weight1);
        assertEquals(75, weight2);
    }

    @Test
    public void testMultipleBuildingName() {
        assertEquals("General Purpose South", building1.getName());
        assertEquals("Forgan Smith Building", building2.getName());
        assertEquals("Andrew N. Liveris Building", building3.getName());
    }

    @Test
    public void testNumFloors() {
        assertEquals(5, building1.getFloors().size());
        assertEquals(1, building2.getFloors().size());
        assertEquals(1, building3.getFloors().size());
    }

    @Test
    public void testNumRooms() {
        assertEquals(4, b1f1.getRooms().size());
        assertEquals(10, b2f1.getRooms().size());
        assertEquals(0, b3f1.getRooms().size());
    }

    @Test
    public void testNumSensors() {
        assertEquals(0, b1f1r1.getSensors().size());
        assertEquals(10, b2f1.getRooms().size());
        assertEquals(0, b3f1.getRooms().size());
    }

    @Test
    public void testSubsequentFloors() {
        assertEquals(2, b1f2.getFloorNumber());
    }

    @Test
    public void testFloor1RoomNum1() {
        assertTrue(b1f1.getRooms().get(0).getRoomNumber() == 101);
        assertTrue(b2f1.getRooms().get(0).getRoomNumber() == 101);
    }

    @Test
    public void testFloor1SubsequentRoom() {
        assertTrue(b1f1.getRooms().get(1).getRoomNumber() == 102);
        assertTrue(b2f1.getRooms().get(1).getRoomNumber() == 102);
    }

    @Test
    public void testFloorWidth() {
        assertEquals(10.00, b1f1.getWidth(), 0);
    }

    @Test
    public void testFloorLength() {
        assertEquals(10.00, b1f1.getLength(), 0);
    }

    @Test
    public void testMaintenanceSchedule() {
        assertEquals("101,103,102,104", b1f1.getMaintenanceSchedule().encode());
        assertTrue(b1f1.getMaintenanceSchedule() instanceof MaintenanceSchedule);
    }

    @Test
    public void testStudyType() {
        assertEquals(RoomType.STUDY, b1f1r1.getType());
    }

    @Test
    public void testLabType() {
        assertEquals(RoomType.LABORATORY, b1f1r4.getType());
    }

    @Test
    public void testOfficeType() {
        assertEquals(RoomType.OFFICE, b1f2r1.getType());
    }

    @Test
    public void testRoomArea() {
        assertEquals(20.00, b1f1r1.getArea(),0);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException, FileFormatException {
        buildingInitialiser.loadBuildings("saves/null.txt");
    }

    @Test(expected = IOException.class)
    public void testIOException2() throws IOException, FileFormatException {
        buildingInitialiser.loadBuildings("saves/null");
    }

    @Test(expected = FileFormatException.class)
    public void testBadFloorNumTooMany() throws IOException, FileFormatException {
        buildingInitialiser.loadBuildings("saves/1BadFloorNumTooMany.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadFloorNumNotEnough() throws IOException,
            FileFormatException {
        buildingInitialiser.loadBuildings("saves/2BadFloorNumNotEnough.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadRoomNumTooMany() throws IOException,
            FileFormatException {
        buildingInitialiser.loadBuildings("saves/3BadRoomNumTooMany.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadRoomNumNotEnough() throws IOException,
            FileFormatException {
        buildingInitialiser.loadBuildings("saves/4BadRoomNumNotEnough.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadSensorNumTooMany() throws IOException,
            FileFormatException {
        buildingInitialiser.loadBuildings("saves/5BadSensorNumTooMany.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadSensorNumNotEnough() throws IOException,
            FileFormatException {
        buildingInitialiser.loadBuildings("saves/6BadSensorNumNotEnough.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testMaintBadRoomWrongFloor() throws IOException,
            FileFormatException {
        buildingInitialiser.loadBuildings("saves/7MaintBadRoomWrongFloor.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidMaintEmptyRoomOrder() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/10InvalidMaintEmptyRoomOrder" +
                ".txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidMaintConsecRoom() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/11InvalidMaintConsecRoom.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidMaintConsecRoomStartEnd() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/12InvalidMaintConsecRoomStartEnd.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testDuplicateFloorNum() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/13DuplicateFloorNum.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorMinFloorWidth() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/14FloorMinFloorWidth.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testMinFloorLength() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/15MinFloorLength.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNoFloorBelow() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/16NoFloorBelow.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorAboveTooBig() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/17FloorAboveTooBig.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testDuplicateRoomNums() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/18DuplicateRoomNums.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testAddRoomNotEnoughSpace() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/19AddRoomNotEnoughSpace.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadRoomType() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/20BadRoomType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomAreaLessThanMin() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/21RoomAreaLessThanMin.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidHEvalType() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/22InvalidHEvalType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidWeightingsBelow0() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/23InvalidWeightingsBelow0.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidWeightingsAbove100() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/24InvalidWeightingsAbove100.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testInvalidWeightingsSumOver100() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/25InvalidWeightingsSumOver100.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testDuplicateSensorType() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/26DuplicateSensorType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testBadSensorType() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/27BadSensorType.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testUpdateFreqLessThan1() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/28UpdateFreqLessThan1.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testUpdateFreqAbove5() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/29UpdateFreqAbove5.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testCO2VarBiggerThanIdeal() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/30CO2VarBiggerThanIdeal.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegFloorNum() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/31NonNegFloorNum.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegRoomNum() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/32NonNegRoomNum.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegSensorNum() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/33NonNegSensorNum.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegSensorReadings() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/34NonNegSensorReadings.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegOccCap() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/35NonNegOccCap.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegCO2Ideal() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/36NonNegCO2Ideal.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNonNegCO2Var() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/37NonNegCO2Var.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorNumsBiggerThanZero() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/38FloorNumsBiggerThanZero.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testFloorColons() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/39FloorColons.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testRoomColons() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/40RoomColons.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testCO2Colons() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/41CO2Colons.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNoiseColons() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/42NoiseColons.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testTempColons() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/43TempColons.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testOccColons() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/44OccColons.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testNumericParse() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/45NumericParse.txt");
    }

    @Test(expected = FileFormatException.class)
    public void testEmptyLine() throws IOException,
            FileFormatException {
        testInitialiser.loadBuildings("saves/46EmptyLine.txt");
    }



}
