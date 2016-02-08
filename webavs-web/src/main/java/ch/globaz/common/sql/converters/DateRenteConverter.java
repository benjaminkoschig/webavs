package ch.globaz.common.sql.converters;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.DateRente;
import ch.globaz.common.sql.ConverterDb;

public class DateRenteConverter implements ConverterDb<DateRente> {

    @Override
    public DateRente convert(Object date, String fieldName, String alias) {
        if (date == null || date.equals(0)) {
            return null;
        }
        String dateString = date.toString();
        if (dateString.equals("0") || dateString.trim().isEmpty()) {
            return null;
        }

        if (dateString.length() == 8) {
            return new DateRente(dateString, Date.DATE_PATTERN);
        } else {
            return new DateRente(dateString, Date.DATE_PATTERN_MONTH);

        }
    }

    static String dateToString(String date) {
        if (date.length() == 8) {
            return date.substring(6, 8) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
        } else {
            return date.substring(4, 6) + "." + date.substring(0, 4);
        }
    }

    @Override
    public Class<DateRente> forType() {
        return DateRente.class;
    }
}
