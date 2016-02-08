package ch.globaz.common.sql.converters;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class DateRenteConverterTest {

    @Test
    public void testToString() throws Exception {
        assertThat(DateRenteConverter.dateToString("20151202")).isEqualTo("02.12.2015");
    }
}
