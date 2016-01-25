package ch.globaz.common.listoutput.converterImplemented;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.Locale;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.simpleoutputlist.converter.Translater;
import com.google.common.base.CaseFormat;

public class LabelTranslater implements Translater {

    @Override
    public String translate(String textToTranslate, String identifier, Locale local) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new CommonTechnicalException("Unable to translate no session founded :" + textToTranslate);
        }
        String separator = "";
        if (identifier != null) {
            separator = "_";
        }
        String key = identifier.toLowerCase() + separator
                + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, textToTranslate);

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
