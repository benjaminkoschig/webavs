package ch.globaz.common.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstancesTest {

    @Test
    public void test_withNullValue_ok() {
        String result = Instances.of(null)
                                 .is(Integer.class, dataInt -> "+")
                                 .is(String.class, dataString -> "string")
                                 .result();

        assertThat(result).isNull();
    }

    @Test
    public void test_withString_ok() {
        String result = Instances.of("")
                                 .is(Integer.class, dataInt -> "+")
                                 .is(String.class, dataString -> "string")
                                 .result();

        assertThat(result).isEqualTo("string");
    }

    @Test
    public void test_withObject_ok() {
        String result = Instances.of("")
                                 .is(Object.class, dataInt -> "object")
                                 .is(String.class, dataString -> "string")
                                 .result();

        assertThat(result).isEqualTo("object");
    }
}
