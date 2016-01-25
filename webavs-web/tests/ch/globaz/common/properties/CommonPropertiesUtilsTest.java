package ch.globaz.common.properties;

import org.junit.Assert;
import org.junit.Test;

public class CommonPropertiesUtilsTest {
    @Test
    public void testIsEmptyValueTrue() throws Exception {
        Assert.assertTrue(CommonPropertiesUtils.isEmptyValue(null));
        Assert.assertTrue(CommonPropertiesUtils.isEmptyValue(""));
        Assert.assertTrue(CommonPropertiesUtils.isEmptyValue("  "));
    }

    @Test
    public void testIsEmptyValueFalse() throws Exception {
        Assert.assertFalse(CommonPropertiesUtils.isEmptyValue("true"));
    }
}
