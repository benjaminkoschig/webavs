package globaz.aquila.ts.rules.utils;

import globaz.aquila.service.COServiceLocator;
import globaz.aquila.ts.opge.COTSOPGEExecutor;
import globaz.aquila.ts.utils.COTSTiersUtils;
import globaz.globall.db.BSession;
import globaz.osiris.external.IntTiers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Méthodes utilitaires pour FER.<br/>
 * Test le contenu de nom et longueur de texte.
 * 
 * @author DDA
 */
public class COOPGESpellChecker {

    /**
     * Pour une personne physique uniquement, la ligne contient bien le nom du tiers ?
     * 
     * @param ligne
     * @param nomTiers
     * @return
     */
    public static boolean containsName(String ligne, String nomTiers) {
        String regex = "(\\s|^)(" + nomTiers.toUpperCase() + "){1}(\\s|$)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ligne.toUpperCase());

        return matcher.find();
    }

    /**
     * Test longueur. Max LENGTH_NOM
     * 
     * @see LENGTH_NOM
     * @param text
     * @return
     */
    public static boolean isLengthNomAllowed(String text) {
        return text.length() <= COTSOPGEExecutor.LENGTH_NOM;
    }

    /**
     * Test longueur. Max LENGTH_SUITE_NOM
     * 
     * @see LENGTH_SUITE_NOM
     * @param text
     * @return
     */
    public static boolean isLengthSuiteNomAllowed(String text) {
        return text.length() <= COTSOPGEExecutor.LENGTH_SUITE_NOM;
    }

    /**
     * Test longueur. LENGTH_MAX_TEXTE_DEBITEUR
     * 
     * @see LENGTH_MAX_TEXTE_DEBITEUR
     * @param text
     * @return
     */
    public static boolean isLengthTexteComplementAllowed(String text) {
        return text.length() <= COTSOPGEExecutor.LENGTH_MAX_TEXTE_DEBITEUR;
    }

    /**
     * Test le text passé en paramètre.<br/>
     * Doit contenir uniquement les caractères : A-Z, ' - et espace<br/>
     * Effectué uniquement sur les personnes physiques.
     * 
     * @param session
     * @param tiers
     * @param text
     * @return
     */
    public static boolean isWellFormated(BSession session, IntTiers tiers, String text) throws Exception {
        if (!COServiceLocator.getTiersService().isPersonnePhysique(session, tiers)) {
            return true;
        } else {
            int count = 0;
            int lastCharacter = 0;

            if (text == null) {
                text = "";
            }

            try {
                text = COTSTiersUtils.convertSpecialChars(text);
            } catch (Exception e) {
                return false;
            }

            for (int i = 0; i < text.length(); i++) {
                int test = text.toUpperCase().charAt(i);

                if ((test >= 'A') && (test <= 'Z')) {
                    if (test != lastCharacter) {
                        count++;
                        lastCharacter = test;
                    }
                } else if ((test != '\'') && (test != '-') && (test != ' ')) {
                    return false;
                }
            }

            return (count >= 2);
        }
    }

}
