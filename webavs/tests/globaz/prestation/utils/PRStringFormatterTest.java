package globaz.prestation.utils;

import globaz.prestation.utils.PRStringFormatter.StringIndentation;
import junit.framework.Assert;
import org.junit.Test;

public class PRStringFormatterTest {

    @Test
    public void test() {

        StringIndentation si = null;
        StringBuilder nullSb = null;
        StringBuilder result = PRStringFormatter.indent(nullSb, 0, null, si);
        Assert.assertTrue(result == null);

        result = PRStringFormatter.indent(nullSb, 0, "", si);
        Assert.assertTrue(result == null);

        StringBuilder sb = new StringBuilder("A B");
        result = PRStringFormatter.indent(sb, 0, "", si);
        Assert.assertTrue("A B".equals(result.toString()));

        result = PRStringFormatter.indent(sb, 0, " ", si);
        Assert.assertTrue("A B".equals(result.toString()));

        result = PRStringFormatter.indent(sb, 5, " ", si);
        Assert.assertTrue("A B".equals(result.toString()));

        result = PRStringFormatter.indent(sb, 5, " ", StringIndentation.LEFT);
        Assert.assertTrue("  A B".equals(result.toString()));

        result = PRStringFormatter.indent(sb, 5, " ", StringIndentation.RIGHT);
        Assert.assertTrue("A B  ".equals(result.toString()));

    }
}
