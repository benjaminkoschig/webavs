package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.common.domaine.Montant;
import ch.globaz.simpleoutputlist.converter.Converter;

public class MontantStringConverter implements Converter<String, String> {

    @Override
    public String getValue(String montant, Locale paramLocale) {
        if (montant != null) {
            return new Montant(montant).toStringFormat();
        }
        return null;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
