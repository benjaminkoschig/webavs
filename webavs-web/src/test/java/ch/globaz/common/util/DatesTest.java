package ch.globaz.common.util;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class
DatesTest {

    @Test
    public void formatSwiss_avecDate_ok() {
        assertThat(Dates.formatSwiss(LocalDate.of(2021, 4, 9))).isEqualTo("09.04.2021");
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

    @Test
    public void daysBetween_withSameDay_1(){
        assertThat(Dates.daysBetween(LocalDate.now(), LocalDate.now())).isEqualTo(1);
    }

    @Test
    public void daysBetween_fromBeforeTo_2(){
        assertThat(Dates.daysBetween(LocalDate.now(), LocalDate.now().plusDays(1))).isEqualTo(2);
    }

    @Test
    public void daysBetween_toBeforeFrom_minus2(){
        assertThat(Dates.daysBetween(LocalDate.now(), LocalDate.now().minusDays(1))).isEqualTo(-2);
    }

    @Test
    public void daysBetween_withStringSuisseFormat_ok(){
        assertThat(Dates.daysBetween("01.02.2021","02.02.2021")).isEqualTo(2);
    }


    @Test
    public void formatDate_avecNull_renvoiNull() {
        // arrange
        String format = "dd.MM.yyyy";
        // act
        XMLGregorianCalendar result = Dates.toXMLGregorianCalendar(null, format);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatDate_avecStringNonConvertible_renvoiNull() {
        // arrange
        String format = "dd.MM.yyyy";
        // act
        XMLGregorianCalendar result = Dates.toXMLGregorianCalendar("12.02", format);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatDate_dateDD_MM_YYYY_renvoiDate() throws DatatypeConfigurationException {
        // arrange
        String format = "dd.MM.yyyy";
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021, 2, 12, 0, 0, 0, 0, 60);

        // act
        XMLGregorianCalendar result = Dates.toXMLGregorianCalendar("12.02.2021", format);

        // assert
        assertThat(result).isEqualTo(date);
    }

}
