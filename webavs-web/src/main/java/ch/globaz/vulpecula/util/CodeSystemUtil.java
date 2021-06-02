package ch.globaz.vulpecula.util;

import globaz.globall.db.BSession;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

/**
 * Classe utilitaire permettant de r�cup�rer des codes syst�mes facilement injectables dans les pages JSP.
 * 
 */
public class CodeSystemUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeSystemUtil.class);

    /**
     * Retourne une liste de codes syst�mes par rapport � la famille pass�e en param�tres.
     * 
     * @param famille String repr�sentant le nom d'une famille de codes syst�mes
     * @return Vecteur de codes syst�mes (contenant uniquement des Strings)
     */
    public static Vector<CodeSystem> getCodesSystemesForFamilleAsVector(String famille) {
        return new Vector<CodeSystem>(getCodesSystemesForFamille(famille));
    }

    /**
     * Retourne la liste des codes syst�mes.
     * 
     * @param famille String repr�sentant le nom d'une famille de codes syst�mes
     * @return List de codes syst�mes (contenant uniquement des Strings)
     */
    public static List<CodeSystem> getCodesSystemesForFamille(String famille) {
        Map<String, CodeSystem> resultTrie = new TreeMap<String, CodeSystem>();
        try {
            List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(
                    famille);
            for (JadeCodeSysteme code : codes) {
                Langues langue = I18NUtil.getLangues();

                String codeValue = code.getCodeUtilisateur(langue);
                String libelleValue = code.getTraduction(langue);

                CodeSystem codeSystem = new CodeSystem();
                codeSystem.setId(code.getIdCodeSysteme());
                codeSystem.setCode(codeValue);
                codeSystem.setLibelle(libelleValue);
                codeSystem.setOrdre(code.getOrdre());
                // TODO: Ajout de l'activit�

                resultTrie.put(codeSystem.getCode(), codeSystem);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return new ArrayList<CodeSystem>(resultTrie.values());
    }

    /**
     * @param stCodeSystem
     * @param langue
     * @return le code syst�me en fonction de sont num�ro et de la langue
     */
    public static CodeSystem getCodeSysteme(String stCodeSystem, Langues langue) {
        CodeSystem codeSystem = new CodeSystem();
        try {
            JadeCodeSysteme code = JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(stCodeSystem);
            if (code == null) {
                return codeSystem;
            }
            String codeValue = code.getCodeUtilisateur(langue);
            String libelleValue = code.getTraduction(langue);

            codeSystem.setId(code.getIdCodeSysteme());
            codeSystem.setCode(codeValue);
            codeSystem.setLibelle(libelleValue);
            codeSystem.setOrdre(code.getOrdre());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return codeSystem;
    }

    public static List<CodeSystem> getCodeSystem(List<String> codesSystemes, Langues langue) {
        List<CodeSystem> codes = new ArrayList<CodeSystem>();
        for (String code : codesSystemes) {
            codes.add(getCodeSysteme(code, langue));
        }
        return codes;
    }

    public static String getFormulePolitesse(BSession session, String idTiers) throws Exception {
        TITiers tiers = new TITiers();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        tiers.retrieve();
        return tiers.getFormulePolitesse(null);
    }


}
