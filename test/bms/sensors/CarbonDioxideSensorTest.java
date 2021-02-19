package bms.sensors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CarbonDioxideSensorTest {

    private CarbonDioxideSensor cdSensor;
    private CarbonDioxideSensor cdSensor2;
    private CarbonDioxideSensor cdSensorBad;
    private OccupancySensor occupancySensor;

    @Before
    public void setup(){
        cdSensor = new CarbonDioxideSensor(new int[]{420,1001,2001,5000}, 2,
                500,
                100);
        cdSensor2 = new CarbonDioxideSensor(new int[]{420,1001,2001,5000}, 2,
                500,
                100);
        cdSensorBad = new CarbonDioxideSensor(new int[]{420,1001,2001,5000}, 2,
                500,
                200);
        occupancySensor = new OccupancySensor(new int[]{50,1,3}, 3,3);

    }

    @Test
    public void testGetComfortLevel() {
        assertEquals(20, cdSensor.getComfortLevel());
    }

    @Test
    public void testEquals() {
        assertTrue(cdSensor.equals(cdSensor2));
        assertFalse(cdSensor.equals(cdSensorBad));
    }

    @Test
    public void testHashCode() {
        assertTrue(cdSensor.hashCode() == cdSensor2.hashCode());
        //assertEquals(131148032, cdSensor.hashCode());
        //assertEquals(131148032, cdSensor2.hashCode());
        //assertEquals(262296064, cdSensorBad.hashCode());

    }

    @Test
    public void testEncode() {
        assertEquals("CarbonDioxideSensor:420,1001,2001," +
                "5000:2:500:100", cdSensor.encode());
    }

}
