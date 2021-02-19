package bms.sensors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OccupancySensorTest {

    private OccupancySensor occupancySensor;
    private OccupancySensor occGood;
    private OccupancySensor occ2;

    @Before
    public void setup(){
        occupancySensor = new OccupancySensor(new int[]{14,1,3}, 3,20);
        occGood = new OccupancySensor(new int[]{14,1,3}, 3,20);
        occ2 = new OccupancySensor(new int[]{17,1,3}, 3,15);
    }

    @Test
    public void testGetComf() {
        assertEquals(30, occupancySensor.getComfortLevel());
        assertEquals(0, occ2.getComfortLevel());
    }

    @Test
    public void testEquals() {
        assertTrue(occupancySensor.equals(occGood));
        assertFalse(occupancySensor.equals(occ2));
    }

    @Test
    public void testEncode() {
        assertEquals("OccupancySensor:14,1,3:3:20", occupancySensor.encode());
    }



}
