package ch.globaz.common.sql;

import globaz.globall.db.BSession;
import java.util.List;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.exceptions.CommonTechnicalException;

/**
 * Cette classe a �t� cr��e dans le but de lancer des requ�tes sur la table FWCOUP pour r�cup�rer les codes syst�mes
 * associ�s � une langue d'un tiers.
 * 
 * @author jwe
 * 
 */
public class CodeSystemQueryExecutor {

    /**
     * Cette m�thode permet d'aller interroger la base de donn�es pour r�cup�rer la langue d'un tiers
     * 
     * @param idTiers
     * @param session
     * @return La langue du tiers sous forme d'ID pour la r�cup�ration d'un code syst�me en DB.
     */
    public static String searchLangueByIDTiers(String idTiers, BSession session) {

        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(session, "session");
        StringBuilder query = new StringBuilder();

        query.append("SELECT TIERS.HTTLAN FROM schema.TITIERP TIERS").append(" WHERE TIERS.HTITIE = ")
                .append(Integer.parseInt(idTiers));

        List<String> codeSystemLanguages = QueryExecutor.execute(query.toString(), String.class, session);
        // r�cup�ration de la langue du tiers
        return LanguageResolver.resolveTiersLanguage(codeSystemLanguages.get(0));
    }

    /**
     * Cette m�thode permet d'aller interroger la base de donn�es pour r�cup�rer un code syst�me associ� � une langue
     * pass�e en param�tre
     * 
     * Dans ce cas, on conna�t d�j� la langue de tiers (obligatoire!)
     * 
     * @param codeSystemID
     * @param session
     * @param tiersLanguage
     * @return un objet de type CodeSystem
     */
    public static CodeSystem searchCodeSystemTraduction(String codeSystemID, BSession session, String tiersLanguage) {

        Checkers.checkNotNull(codeSystemID, "codeSystemID");
        Checkers.checkNotNull(session, "session");
        Checkers.checkNotNull(tiersLanguage, "tiersLanguage");

        // l'appel de cette m�thode permet de v�rifier que la langue que l'on passe en param�tre est bien dans le bon
        // format -> si ce n'est pas le cas, on modifie le format pour obtenir un ID de langue.
        tiersLanguage = LanguageResolver.resolveTiersLanguage(tiersLanguage);
        StringBuilder query = new StringBuilder();

        query.append(
                "SELECT PCOSID as idCodeSysteme, PCOLUT as traduction, PCOUID as codeUtilisateur FROM schema.fwcoup where pcosid = ")
                .append(Integer.parseInt(codeSystemID)).append(" AND PLAIDE = '").append(tiersLanguage).append("'");

        List<CodeSystem> csList = QueryExecutor.execute(query.toString(), CodeSystem.class, session);

        if (csList == null) {
            throw new CommonTechnicalException(
                    "the given system code is malformed or doesn't exist in database. Check the following system code : "
                            + codeSystemID);
        }
        // On aura besoin que d'un code syst�me par langue, on r�cup�re donc la premi�re entr�e de la liste.
        CodeSystem csToReturn = csList.get(0);

        return csToReturn;

    }

}