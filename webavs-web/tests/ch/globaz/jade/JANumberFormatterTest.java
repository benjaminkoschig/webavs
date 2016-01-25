package ch.globaz.jade;

import static org.junit.Assert.*;
import globaz.globall.util.JANumberFormatter;
import org.junit.Test;

public class JANumberFormatterTest {

    @Test
    public void fmt_1000_ShouldBe1000_00() {
        String expected = JANumberFormatter.fmt("1000.00", true, true, false, 2);
        assertEquals(expected, "1'000.00");
    }

    @Test
    public void format_1000_ShouldBe1000_00() {
        String expected = JANumberFormatter.format("1000.00", 0.05, 2, JANumberFormatter.NEAR);
        assertEquals(expected, "1'000.00");
    }
}
