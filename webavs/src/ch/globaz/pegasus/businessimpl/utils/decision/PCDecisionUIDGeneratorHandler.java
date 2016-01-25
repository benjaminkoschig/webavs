/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

/**
 * @author SCE
 * 
 *         26 juil. 2010
 */
public class PCDecisionUIDGeneratorHandler {

    private final static String PC_MODULE_PREFIX = "PC";
    private final static String RFM_MODULE_PREFIX = "RF";

    /**
     * Retourne un numero de decision unique avec l'année passé en paramètre Par defaut le form est AAAA-######
     * 
     * @param annee
     * @return String, le numero de decision
     * @throws JadePersistenceException
     */
    private static String getNoDecision(String prefix, String annee) throws JadePersistenceException {

        final int nbreDigit = 6; // Nombre de chiffre apres annee
        final String separator = "-";
        final String counterKey = prefix + "_" + annee;

        // Seconde partie du numero
        StringBuilder partieIncrement = new StringBuilder("");
        // Recuperation de l'increment
        String increment = JadePersistenceManager.incIndentifiant(counterKey);
        // Nombre de 'zero' apres increment
        int cptBeforeDigitIncr = nbreDigit - increment.length();
        for (int cpt = 0; cpt < cptBeforeDigitIncr; cpt++) {
            partieIncrement.append("0");
        }
        // Ajout de l'incrmeent
        partieIncrement.append(increment);
        // Construction de la chaine
        String noDecision = annee + separator + partieIncrement;
        return noDecision;
    }

    /**
     * 
     * @return
     */
    public static String getNoDecisionForPC(String annee) {

        String returnKey = "";

        try {
            returnKey = PCDecisionUIDGeneratorHandler.getNoDecision(PCDecisionUIDGeneratorHandler.PC_MODULE_PREFIX,
                    annee);
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }

        return returnKey;
    }

    /**
     * 
     * @return
     */
    public static String getNoDecisionForRFM(String annee) {
        String returnKey = "";

        try {
            returnKey = PCDecisionUIDGeneratorHandler.getNoDecision(PCDecisionUIDGeneratorHandler.RFM_MODULE_PREFIX,
                    annee);
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }

        return returnKey;
    }
}
