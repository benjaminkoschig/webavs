package ch.globaz.common.codesystem;

import globaz.globall.db.BSession;
import ch.globaz.common.sql.CodeSystemQueryExecutor;

/**
 * Il s'agit d'une classe utilitaire qui permet de rechercher des codes systèmes en fonction d'une langue
 * passée en paramètre.
 * 
 * Cette classe est prévue pour être utilisée avec l'ancienne persistance
 * 
 * @author jwe
 * 
 */
public class CodeSystemUtils {

    /**
     * Cette méthode permet de retrouver la langue d'un tiers et de rechercher un codesystème.
     * 
     * @param codeSystemID
     * @param session
     * @param idTiers
     * @return un objet de type CodeSystem
     */
    public static CodeSystem searchTiersLanguageAndCodeSystemTraduction(String codeSystemID, BSession session,
            String idTiers) {
        String langueTiers = CodeSystemQueryExecutor.searchLangueByIDTiers(idTiers, session);

        return CodeSystemQueryExecutor.searchCodeSystemTraduction(codeSystemID, session, langueTiers);

    }

    /**
     * Cette méthode permet de retrouver un code système en fonction de la langue passée en paramètre.
     * 
     * @param codeSystemID
     * @param session
     * @param idTiers
     * @return un objet de type CodeSystem
     */
    public static CodeSystem searchCodeSystemTraduction(String codeSystemID, BSession session, String langueID) {
        return CodeSystemQueryExecutor.searchCodeSystemTraduction(codeSystemID, session, langueID);

    }

}
