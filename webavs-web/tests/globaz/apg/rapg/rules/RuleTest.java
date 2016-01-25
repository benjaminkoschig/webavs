package globaz.apg.rapg.rules;

import org.junit.Assert;

public abstract class RuleTest {

    protected void assertTrue(boolean expression) {
        Assert.assertTrue(expression);
    }

    protected void assertFalse(boolean expression) {
        Assert.assertFalse(expression);
    }

    protected void assertNull(Object o) {
        Assert.assertTrue(o == null);
    }

    protected void assertNotNull(Object o) {
        Assert.assertTrue(o != null);
    }
}
