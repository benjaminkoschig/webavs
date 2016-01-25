package ch.globaz.vulpecula.web.views.comptabilite;

import globaz.globall.db.BSessionUtil;
import globaz.osiris.parser.CASelectBlockParser;

public class ReferencesRubriquesViewService {
    public static String getForIdCodeReferenceSelectBlock(String idCodeReference) {
        return CASelectBlockParser.getForIdCodeReferenceSelectBlock(BSessionUtil.getSessionFromThreadContext(),
                idCodeReference);
    }

    public static String getForIdCodeReferenceSelectBlockWithoutCodeReference(String unusedParameter) {
        return CASelectBlockParser.getForIdCodeReferenceSelectBlock(BSessionUtil.getSessionFromThreadContext(), "");
    }
}
