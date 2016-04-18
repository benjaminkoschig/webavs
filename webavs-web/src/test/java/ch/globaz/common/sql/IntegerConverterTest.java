package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.Test;

public class IntegerConverterTest {

    @Test
    public void testConvertForString() throws Exception {
        IntegerConverter converter = new IntegerConverter();
        assertThat(converter.convert("0", null, null)).isZero();
        assertThat(converter.convert("1000", null, null)).isEqualTo(new Integer(1000));
        assertThat(converter.convert(" 0 ", null, null)).isZero();
        assertThat(converter.convert("", null, null)).isNull();
        assertThat(converter.convert(null, null, null)).isNull();
    }

    @Test
    public void testConvertForBigDecimal() throws Exception {
        IntegerConverter converter = new IntegerConverter();
        assertThat(converter.convert(new BigDecimal(0), null, null)).isZero();
        assertThat(converter.convert(new BigDecimal(1000), null, null)).isEqualTo(new Integer(1000));
        assertThat(converter.convert(null, null, null)).isNull();
    }

    @Test
    public void testConvertForInteger() throws Exception {
        IntegerConverter converter = new IntegerConverter();
        assertThat(converter.convert(new Integer(0), null, null)).isZero();
        assertThat(converter.convert(new Integer(1000), null, null)).isEqualTo(new Integer(1000));
        assertThat(converter.convert(null, null, null)).isNull();
    }

}
