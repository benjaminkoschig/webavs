package ch.globaz.common.sql.converters;

import ch.globaz.common.util.Instances;
import ch.globaz.queryexc.converters.ConverterDb;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements ConverterDb<LocalDate> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public LocalDate convert(Object valueDate, String fieldName, String alias) {
        return Instances.of(valueDate)
                        .is(BigDecimal.class, date -> parse(String.valueOf(date.intValue())))
                        .is(Integer.class, date -> parse(date.toString()))
                        .is(String.class, this::parse)
                        .result();
    }

    private LocalDate parse(final String date) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }

    @Override
    public Class<LocalDate> forType() {
        return LocalDate.class;
    }
}
