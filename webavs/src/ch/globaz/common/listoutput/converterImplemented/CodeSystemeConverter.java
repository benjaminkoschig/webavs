package ch.globaz.common.listoutput.converterImplemented;

import globaz.globall.db.BSessionUtil;
import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class CodeSystemeConverter implements Converter<String, String> {

    @Override
    public String getValue(String objet, Locale local) {
        if (objet != null) {
            String libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(objet);
            if (libelle != null && libelle.length() == 0) {
                libelle = objet;
            }
            if ("0".equals(libelle)) {
                libelle = "";
            }
            return libelle;
        } else {
            return "";
        }
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
