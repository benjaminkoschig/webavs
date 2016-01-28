package ch.globaz.common.listoutput;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.listoutput.ExecutionTime;

public class ExecutionTimeTest {

    @Test
    public void testComputeAvrage() throws Exception {
        ExecutionTime executionTime = new ExecutionTime();
        assertTrue(0 == executionTime.computeAvrage());
    }

    @Test
    public void testToStringWithOutCheker() throws Exception {
        ExecutionTime executionTime = new ExecutionTime();
        assertEquals("Loader: 0ms | Handle: 0ms | Out: 0ms | nb: 0| Average: 0.0ms | Total:0.0ms",
                executionTime.toString());
    }

}
