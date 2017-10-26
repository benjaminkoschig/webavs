package ch.globaz.common.listoutput.converterImplemented;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Locale;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.simpleoutputlist.converter.Translater;

public class LabelTranslaterNormal implements Translater {
    private BSession session;
    private String identifier;

    public LabelTranslaterNormal(BSession session, String identifier) {
        super();
        this.session = session;
        this.identifier = identifier;
    }

    public LabelTranslaterNormal() {
        session = BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public String translate(String textToTranslate, String overridedIdentifier, Locale local) {
        if (session == null) {
            throw new CommonTechnicalException("Unable to translate no session founded :" + textToTranslate);
        }

        if (!JadeStringUtil.isEmpty(overridedIdentifier)) {
            identifier = overridedIdentifier;
        }

        String ident = "";
        if (identifier != null) {
            ident = identifier + "_";
        }
        String key = ident + textToTranslate;

        String traduction;
        try {
            traduction = session.getApplication().getLabel(key, local.getLanguage());
            if (traduction.length() == 0) {
                return "[toTranslate]" + textToTranslate;
            }
        } catch (Exception e) {
            return "[Unable to get application]" + textToTranslate;
        }

        return traduction;
    }
}
