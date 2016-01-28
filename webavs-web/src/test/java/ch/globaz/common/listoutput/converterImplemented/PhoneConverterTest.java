package ch.globaz.common.listoutput.converterImplemented;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class PhoneConverterTest {

    private String tel;
    private String expected;

    public PhoneConverterTest(String expected, String tel) {
        this.tel = tel;
        this.expected = expected;
    }

    @Parameters(name = "tel={1}, expected ={0}")
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][] { { "032 435 62 66", "032 435 62 66" }, { "", "" }, { null, null },
                { "032 435 62 66", "0324356266" }, { "41 79 438 54 75", "41794385475" },
                { "0041 794 38 54 75", "0041794385475" }, { "41 32 435 62 66", "41324356266" },
                { "0041 32 435 62 66", "0041324356266" }, { "024 468 13 27", "024.468.13.27." }});
    }

    @Test
    public void test() {
        PhoneConverter converter = new PhoneConverter();

        assertEquals(expected, converter.getValue(tel, null));
    }

}
