package ch.globaz.common.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;

public class DatesTest {

    @Test
    public void formatSwiss_avecDate_ok() {
        Assertions.assertThat(Dates.formatSwiss(LocalDate.of(2021, 4, 9))).isEqualTo("09.04.2021");
    }

    @Test
    public void formatSwiss_sansDate_ok() {
        Assertions.assertThat(Dates.formatSwiss(null)).isEqualTo("");
    }
}
