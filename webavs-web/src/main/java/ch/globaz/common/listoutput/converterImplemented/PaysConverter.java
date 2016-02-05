package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.simpleoutputlist.converter.Converter;

public class PaysConverter implements Converter<Pays, String> {

    @Override
    public String getValue(Pays pays, Locale locale) {
        return pays.getTraduction(locale);
    }

    @Override
    public Class<Pays> getType() {
        return Pays.class;
    }

}
