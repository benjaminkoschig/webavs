package ch.globaz.common.sql.converters;

import static org.assertj.core.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.Test;
import ch.globaz.pyxis.domaine.Sexe;

public class SexeConverterTest {

    @Test
    public void testConvertInteger() throws Exception {
        SexeConverter converter = new SexeConverter();
        assertThat(converter.convert(Sexe.HOMME.getCodeSysteme(), null, null)).isEqualTo(Sexe.HOMME);
    }

    @Test
    public void testConvertBigDecimal() throws Exception {
        SexeConverter converter = new SexeConverter();
        assertThat(converter.convert(new BigDecimal(Sexe.HOMME.getCodeSysteme()), null, null)).isEqualTo(Sexe.HOMME);
    }
}
