package ch.globaz.common.listoutput.converterImplemented;

import globaz.globall.db.BSessionUtil;
import java.util.Locale;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.simpleoutputlist.converter.Converter;

public class YesNoConverter implements Converter<Boolean, String> {

    @Override
    public String getValue(Boolean value, Locale local) {
        if (value != null) {
            if (value) {
                if (Langues.Allemand.getCodeIso().equals(BSessionUtil.getSessionFromThreadContext().getIdLangueISO())) {
                    return "ja";
                } else if (Langues.Italien.getCodeIso().equals(
                        BSessionUtil.getSessionFromThreadContext().getIdLangueISO())) {
                    return "sì";
                }
                return "oui";
            } else {
                if (Langues.Allemand.getCodeIso().equals(BSessionUtil.getSessionFromThreadContext().getIdLangueISO())) {
                    return "nein";
                } else if (Langues.Italien.getCodeIso().equals(
                        BSessionUtil.getSessionFromThreadContext().getIdLangueISO())) {
                    return "non";
                }

                return "non";
            }
        }
        return null;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

}
