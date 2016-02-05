package ch.globaz.common.sql.converters;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.ConverterDb;

public class DateConverter implements ConverterDb<Date> {

    @Override
    public Date convert(Object date, String fieldName, String alias) {
        if (date == null || date.equals(0)) {
            return null;
        }
        String dateString = date.toString();
        if (dateString.equals("0")) {
            return null;
        }
        return new Date(dateString);
    }

    @Override
    public Class<Date> forType() {
        return Date.class;
    }
}