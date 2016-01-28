package ch.globaz.common.codesystem;

import globaz.globall.db.BSession;
import ch.globaz.common.sql.CodeSystemQueryExecutor;

/**
 * Il s'agit d'une classe utilitaire qui permet de rechercher des codes syst�mes en fonction d'une langue
 * pass�e en param�tre.
 * 
 * Cette classe est pr�vue pour �tre utilis�e avec l'ancienne persistance
 * 
 * @author jwe
 * 
 */
public class CodeSystemUtils {

    /**
     * Cette m�thode permet de retrouver la langue d'un tiers et de rechercher un codesyst�me.
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
     * Cette m�thode permet de retrouver un code syst�me en fonction de la langue pass�e en param�tre.
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
