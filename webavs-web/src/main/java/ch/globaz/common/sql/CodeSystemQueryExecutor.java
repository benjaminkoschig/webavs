package ch.globaz.common.sql;

import globaz.globall.db.BSession;
import java.util.List;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.exceptions.CommonTechnicalException;

/**
 * Cette classe a été créée dans le but de lancer des requêtes sur la table FWCOUP pour récupérer les codes systèmes
 * associés à une langue d'un tiers.
 * 
 * @author jwe
 * 
 */
public class CodeSystemQueryExecutor {

    /**
     * Cette méthode permet d'aller interroger la base de données pour récupérer la langue d'un tiers
     * 
     * @param idTiers
     * @param session
     * @return La langue du tiers sous forme d'ID pour la récupération d'un code système en DB.
     */
    public static String searchLangueByIDTiers(String idTiers, BSession session) {

        Checkers.checkNotNull(idTiers, "idTiers");
        Checkers.checkNotNull(session, "session");
        StringBuilder query = new StringBuilder();

        query.append("SELECT TIERS.HTTLAN FROM schema.TITIERP TIERS").append(" WHERE TIERS.HTITIE = ")
                .append(Integer.parseInt(idTiers));

        List<String> codeSystemLanguages = QueryExecutor.execute(query.toString(), String.class, session);
        // récupération de la langue du tiers
        return LanguageResolver.resolveTiersLanguage(codeSystemLanguages.get(0));
    }

    /**
     * Cette méthode permet d'aller interroger la base de données pour récupérer un code système associé à une langue
     * passée en paramètre
     * 
     * Dans ce cas, on connaît déjà la langue de tiers (obligatoire!)
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

        // l'appel de cette méthode permet de vérifier que la langue que l'on passe en paramètre est bien dans le bon
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
        // On aura besoin que d'un code système par langue, on récupère donc la première entrée de la liste.
        CodeSystem csToReturn = csList.get(0);

        return csToReturn;

    }

}