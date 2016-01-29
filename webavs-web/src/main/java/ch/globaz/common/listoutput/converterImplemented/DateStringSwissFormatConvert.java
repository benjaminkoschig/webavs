package ch.globaz.common.listoutput.converterImplemented;

import java.text.SimpleDateFormat;
import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class DateStringSwissFormatConvert implements Converter<String, String> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public String getValue(String date, Locale locale) {
        if (date != null) {
            return dateFormat.format(date);
        }
        return null;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
