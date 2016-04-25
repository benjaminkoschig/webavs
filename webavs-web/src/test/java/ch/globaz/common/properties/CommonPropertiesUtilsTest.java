package ch.globaz.common.properties;

import static org.fest.assertions.api.Assertions.*;
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

    @Test
    public void testDecoupeStringValueEmpty() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue("")).isEmpty();
    }

    @Test
    public void testDecoupeStringValueNull() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue(null)).isEmpty();
    }

    @Test
    public void testDecoupeStringValue1() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue("test")).contains("test");
        assertThat(CommonPropertiesUtils.decoupeStringValue("test")).hasSize(1);
    }

    @Test
    public void testDecoupeStringValue2() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue("test,test2")).contains("test", "test2");
        assertThat(CommonPropertiesUtils.decoupeStringValue("test,test2")).hasSize(2);
    }

    @Test
    public void testDecoupeStringValue3() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue(",")).hasSize(0);
    }

    @Test
    public void testDecoupeStringValue4() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue("9100.2000.1000,2450.2100.1200")).contains(
                "9100.2000.1000", "2450.2100.1200");
        assertThat(CommonPropertiesUtils.decoupeStringValue("9100.2000.1000,2450.2100.1200")).hasSize(2);
    }

    @Test
    public void testDecoupeStringValue5() throws Exception {
        assertThat(CommonPropertiesUtils.decoupeStringValue("    9100.2000.1000 ,  2450.2100.1200 ")).contains(
                "9100.2000.1000", "2450.2100.1200");
        assertThat(CommonPropertiesUtils.decoupeStringValue("    9100.2000.1000 ,  2450.2100.1200 ")).hasSize(2);
    }
}
