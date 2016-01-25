package globaz.corvus.anakin;

import org.junit.Assert;
import org.junit.Test;

public class REAnakinParserTest {

    @Test
    public void testFormatToAnakinDate() {
        String value1 = "02.2011";
        String expected1 = "0211";

        String value2 = "01.02.2011";
        String expected2 = value2;

        String value3 = "abc";
        String expected3 = value3;

        Assert.assertEquals(expected1, REAnakinParser.getInstance().formatToAnakinDate(value1));
        Assert.assertEquals(expected2, REAnakinParser.getInstance().formatToAnakinDate(value2));
        Assert.assertEquals(expected3, REAnakinParser.getInstance().formatToAnakinDate(value3));
    }

    @Test
    public void testGetInstance() {
        Assert.assertEquals(REAnakinParser.getInstance(), REAnakinParser.getInstance());
    }
}
