package ch.globaz.common.domaine;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class DateRenteTest {
    @Test
    public void testDateSansMMJJ1() throws Exception {
        assertThat(new DateRente("20150000", Date.DATE_PATTERN));
    }

    @Test
    public void testDateSansMMJJ2() throws Exception {
        assertThat(new DateRente("00002015", Date.DATE_PATTERN_ddMMyyyy));
    }

    @Test
    public void testDateSansMMJJ3() throws Exception {
        assertThat(new DateRente("00.00.2015", Date.DATE_PATTERN_SWISS));
    }

    @Test
    public void testGetSwissValue() throws Exception {
        assertThat(new DateRente("00.00.2015", Date.DATE_PATTERN_SWISS).getSwissValue()).isEqualTo("00.00.2015");
        // assertThat(new DateRente("02.12.2015").getSwissValue()).isEqualTo("02.12.2015");
    }

    @Test
    public void testGetSwissValue1() throws Exception {
        assertThat(new DateRente("00.03.2015", Date.DATE_PATTERN_SWISS).getSwissValue()).isEqualTo("00.03.2015");
        // assertThat(new DateRente("02.12.2015").getSwissValue()).isEqualTo("02.12.2015");
    }

    @Test
    public void testDateSansdd3() throws Exception {
        assertThat(new DateRente("00031986", Date.DATE_PATTERN_ddMMyyyy));
    }

}
