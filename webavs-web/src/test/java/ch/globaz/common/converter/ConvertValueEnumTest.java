package ch.globaz.common.converter;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class ConvertValueEnumTest {

    @Test
    public void testConvertE() throws Exception {
        ConvertValueEnum<String, Testenum> map = new ConvertValueEnum<String, Testenum>();
        map.put("1", Testenum.T);
        try {
            map.put("2", Testenum.T);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    public enum Testenum {
        T,
        T1;
    }
}
