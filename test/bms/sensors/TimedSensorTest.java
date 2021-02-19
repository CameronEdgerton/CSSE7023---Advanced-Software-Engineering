package bms.sensors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimedSensorTest {

    private CarbonDioxideSensor cdSensor;
    private CarbonDioxideSensor cdSensor2;
    private CarbonDioxideSensor cdSensorBad;

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

    }

    @Test
    public void testInstance() {
        assertTrue(cdSensor instanceof TimedSensor);
    }

    @Test
    public void testEquals() {
        assertTrue(cdSensor.equals(cdSensor2));
        assertFalse(cdSensor.equals(cdSensorBad));
    }
}
