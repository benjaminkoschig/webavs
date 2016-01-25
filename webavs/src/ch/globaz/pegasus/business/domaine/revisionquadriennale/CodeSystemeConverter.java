package ch.globaz.pegasus.business.domaine.revisionquadriennale;

import globaz.globall.db.BSessionUtil;
import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class CodeSystemeConverter implements Converter<String, String> {

    @Override
    public String getValue(String objet, Locale local) {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(objet);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
