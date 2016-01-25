package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.common.domaine.Date;
import ch.globaz.simpleoutputlist.converter.Converter;

public class DateSwissFormatConverter implements Converter<Date, String> {

    @Override
    public String getValue(Date date, Locale local) {
        if (date != null) {
            return date.getSwissValue();
        }
        return null;
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

}
