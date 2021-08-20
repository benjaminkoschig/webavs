package ch.globaz.common.ws;

import ch.globaz.common.util.Dates;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonJsonProviderTest {

    @Test
    public void toDate_withDate() throws JsonProcessingException {
        DateTest<Date> dateTest = new DateTest<>();
        dateTest.setDate(Dates.toJavaDate(LocalDate.of(2011, 1, 1)));
        String date = JacksonJsonProvider.getInstance().writeValueAsString(dateTest);
        assertThat(date).isEqualTo("{\"date\":\"2011-01-01\"}");
    }

    @Test
    public void toDate_withLocDateTime() throws JsonProcessingException {
        DateTest<LocalDateTime> dateTest = new DateTest<>();
        dateTest.setDate(LocalDateTime.of(2011, 1, 1,14,23,15));
        String date = JacksonJsonProvider.getInstance().writeValueAsString(dateTest);
        assertThat(date).isEqualTo("{\"date\":\"2011-01-01T14:23:15\"}");
    }

    @Test
    public void toDate_withLocaDate() throws JsonProcessingException {
        DateTest<LocalDate> dateTest = new DateTest<>();
        dateTest.setDate(LocalDate.of(2011, 1, 1));
        String date = JacksonJsonProvider.getInstance().writeValueAsString(dateTest);
        assertThat(date).isEqualTo("{\"date\":\"2011-01-01\"}");
    }

    @Data
    static class DateTest<T> {
        private T date;
    }
}
