package ch.globaz.pegasus.business.domaine.revisionquadriennale;

import java.util.Locale;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.simpleoutputlist.converter.Converter;

public class SexeConverter implements Converter<String, String> {

    @Override
    public String getValue(String cs, Locale local) {
        if (Sexe.FEMME.getCodeSysteme().toString().equals(cs)) {
            return "Madame";
        } else {
            return "Monsieur";
        }
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
