package ch.globaz.common.codesystem;

import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.vulpecula.util.I18NUtil;
import globaz.globall.db.BSession;
import ch.globaz.common.sql.CodeSystemQueryExecutor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public static Map<String, CodeSystem> getCodesSystemes(String famille) throws Exception {
        Map<String, CodeSystem> resultTrie = new TreeMap<>();
        List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService()
                .getFamilleCodeSysteme(famille);
        for (JadeCodeSysteme code : codes) {
            Langues langue = I18NUtil.getLangues();

            String codeValue = code.getCodeUtilisateur(langue);
            String libelleValue = code.getTraduction(langue);

            CodeSystem codeSystem = new CodeSystem();
            codeSystem.setIdCodeSysteme(code.getIdCodeSysteme());
            codeSystem.setCodeUtilisateur(codeValue);
            codeSystem.setTraduction(libelleValue);
            codeSystem.setOrdre(code.getOrdre());

            resultTrie.put(codeSystem.getIdCodeSysteme(), codeSystem);


        }

        return resultTrie;
    }

}
