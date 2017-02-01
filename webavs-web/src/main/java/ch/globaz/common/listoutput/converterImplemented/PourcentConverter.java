package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.common.domaine.Pourcentage;
import ch.globaz.simpleoutputlist.converter.Converter;

public class PourcentConverter implements Converter<Pourcentage, String> {

    @Override
    public String getValue(Pourcentage value, Locale locale) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    @Override
    public Class<Pourcentage> getType() {
        return Pourcentage.class;
    }

}
