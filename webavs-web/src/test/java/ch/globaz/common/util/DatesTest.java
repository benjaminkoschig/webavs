package ch.globaz.common.util;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
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
        Assertions.assertThat(Dates.formatSwiss(null)).isEmpty();
    }

    @Test
    public void toDate_sansDate_ok() {
        Assertions.assertThat(Dates.toDate((String)null)).isNull();
        Assertions.assertThat(Dates.toDate("")).isNull();
        Assertions.assertThat(Dates.toDate("   ")).isNull();
    }

    @Test
    public void toDate_fromJade_ok() throws JAException {
        JADate jaDate = new JADate();
        jaDate.fromString("03.02.2021");
        Assertions.assertThat(Dates.toDate(jaDate)).isEqualTo(LocalDate.of(2021, 2, 3));
    }

    @Test
    public void toDate_avecSwissForamt_ok() {
        Assertions.assertThat(Dates.toDate("01.02.2021")).isEqualTo(LocalDate.of(2021, 2, 1));
    }

    @Test
    public void isEqual_dates_null() {
        Assertions.assertThat(Dates.isEqual(null, Dates.toDate("01.02.2021"))).isFalse();
        Assertions.assertThat(Dates.isEqual(Dates.toDate("01.02.2021"), null)).isFalse();
    }

}
