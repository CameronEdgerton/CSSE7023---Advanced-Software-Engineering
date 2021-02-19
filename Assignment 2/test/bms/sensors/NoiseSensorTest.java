package bms.sensors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class NoiseSensorTest {

    private NoiseSensor noiseSensor;
    private NoiseSensor noiseSensor2;

    @Before
    public void setup(){
        noiseSensor = new NoiseSensor(new int[]{82,2,3},3);
        noiseSensor2 = new NoiseSensor(new int[]{67,2,3},3);
    }

    @Test
    public void testComft() {
        assertEquals(0, noiseSensor.getComfortLevel());
        assertEquals(18, noiseSensor2.getComfortLevel());
    }

    @Test
    public void testEncode() {
        assertEquals("NoiseSensor:82,2,3:3", noiseSensor.encode());
    }
}
