package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.simpleoutputlist.converter.Converter;

public class SexeHFConverter implements Converter<String, String> {

    @Override
    public String getValue(String sexe, Locale local) {
        if ("h".equalsIgnoreCase(sexe) || "m".equalsIgnoreCase(sexe)) {
            if (Langues.Allemand.getCodeIso().equals(local.getLanguage())) {
                return "M";
            } else if (Langues.Francais.getCodeIso().equals(local.getLanguage())) {
                return "H";
            } else if (Langues.Italien.getCodeIso().equals(local.getLanguage())) {
                return "H";
            }
        } else if ("f".equalsIgnoreCase(sexe) || "w".equalsIgnoreCase(sexe)) {
            if (Langues.Allemand.getCodeIso().equals(local.getLanguage())) {
                return "W";
            } else if (Langues.Francais.getCodeIso().equals(local.getLanguage())) {
                return "F";
            } else if (Langues.Italien.getCodeIso().equals(local.getLanguage())) {
                return "F";
            }
        }
        return "";
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}