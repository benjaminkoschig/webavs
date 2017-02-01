package ch.globaz.common.listoutput.converterImplemented;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Locale;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.simpleoutputlist.converter.Translater;
import com.google.common.base.CaseFormat;

public class LabelTranslater implements Translater {
    private BSession session;
    private String identifier;

    public LabelTranslater(BSession session, String identifier) {
        super();
        this.session = session;
        this.identifier = identifier;
    }

    public LabelTranslater() {
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
            ident = identifier.toLowerCase() + "_";
        }
        String key = ident + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, textToTranslate);

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
