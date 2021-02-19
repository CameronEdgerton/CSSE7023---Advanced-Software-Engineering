package bms.sensors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TempSenTest {

    private TemperatureSensor temperatureSensor;
    private TemperatureSensor temperatureSensor2;
    private TemperatureSensor temperatureSensor3;
    private TemperatureSensor temperatureSensor4;
    private TemperatureSensor temperatureSensor5;

    @Before
    public void setup(){
        temperatureSensor = new TemperatureSensor(new int[]{23,68,30});
        temperatureSensor2 = new TemperatureSensor(new int[]{27,68,30});
        temperatureSensor3 = new TemperatureSensor(new int[]{18,68,30});
        temperatureSensor4 = new TemperatureSensor(new int[]{14,68,30});
        temperatureSensor5 = new TemperatureSensor(new int[]{31,68,30});
    }

    @Test
    public void testComfort() {
        assertEquals(100, temperatureSensor.getComfortLevel());
        assertEquals(80, temperatureSensor2.getComfortLevel());
        assertEquals(60, temperatureSensor3.getComfortLevel());
        assertEquals(0, temperatureSensor4.getComfortLevel());
        assertEquals(0, temperatureSensor5.getComfortLevel());
    }

    @Test
    public void testEncode() {
        assertEquals("TemperatureSensor:23,68,30", temperatureSensor.encode());
    }
}
