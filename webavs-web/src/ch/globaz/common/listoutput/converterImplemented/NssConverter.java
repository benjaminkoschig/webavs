package ch.globaz.common.listoutput.converterImplemented;

import java.text.MessageFormat;
import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class NssConverter implements Converter<String, String> {

    @Override
    public String getValue(String nss, Locale local) {
        if (nss != null && nss.length() == 13) {
            MessageFormat text = new java.text.MessageFormat("{0}.{1}.{2}.{3}");
            String[] telArr = { nss.substring(0, 3), nss.substring(3, 7), nss.substring(7, 11), nss.substring(11, 13) };
            return text.format(telArr);
        }
        return nss;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
