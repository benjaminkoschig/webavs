package globaz.corvus.annonce.formatter;

import globaz.corvus.annonce.RENSS;
import globaz.globall.util.JADate;

/**
 * f
 * 
 * @author lga
 * 
 */
public class RECreationAnnonce10EmeRevisionFormatter extends REAbstractCreationAnnonceFormatter {

    // -------------------------------------------------------------------------
    // REANN44

    /**
     * REANN44.ZENNNA</br>
     * </br>
     * Retourne la valeur <code>nouveauNoAssureAyantDroit</code> formatée</br>
     * </br>
     * -> Retourne une chaîne vide si <code>nouveauNoAssureAyantDroit</code> est null</br>
     * -> Sinon retourne le NSS formaté sans les point (13 positions)</br>
     * 
     * @param nouveauNoAssureAyantDroit Le nouveau numéro NSS de l'ayant droit
     * @return la valeur <code>nouveauNoAssureAyantDroit</code> formatée
     */
    public String formatNouveauNoAssureAyantDroit(RENSS nouveauNoAssureAyantDroit) {
        return formatNSS(nouveauNoAssureAyantDroit);
    }

    // -------------------------------------------------------------------------
    // REAAL3A

    /**
     * REAAL3A.YZNBTA</br>
     * </br>
     * Format la valeur 'nombre d'année de BTA.</br>
     * </br>
     * -> Retourne une chaîne vide si <code>nbreAnneeBTA</code> est null</br>
     * </br>
     * 
     * @param nombreAnnee Le nombre d'année de BTA
     * @param nombreMois Le nombre de mois de BTA
     */
    public String formatNombreAnneeBTA(Integer nombreAnnee, Integer nombreMois) {
        String result = "";
        String annee = "00";
        String mois = "00";
        boolean valid = false;
        if ((nombreAnnee != null && nombreAnnee > 0)) {
            valid = true;
            annee = indentLeftWithZero(nombreAnnee, 2);
        }
        if (nombreMois != null && nombreMois > 0) {
            valid = true;
            mois = indentLeftWithZero(nombreMois, 2);
        }
        if (valid) {
            result = annee + mois;
        }
        return result;
    }

    /**
     * REAAL3A.YZNBON (Integer)</br>
     * </br>
     * Format la valeur 'nombre d'année de bonification transitoire'</br>
     * </br>
     * -> Retourne une chaîne vide si <code>nbreAnneeBonifTrans</code> est null</br>
     * -> formate sur 2 positions avec des zéros à gauche</br>
     * </br>
     * 
     * @param nbreAnneeBonifTrans Le nombre d'année de bonification transitoire
     * @param boolean1
     * @return Le nombre d'année de bonification transitoire formaté
     */
    public String formatNombreAnneeBonifTrans(Integer nbreAnneeBonifTrans, Boolean isDemiAnnee) {
        String result = "";
        String annee = "0";
        String mois = "0";
        boolean valid = false;
        if ((nbreAnneeBonifTrans != null && nbreAnneeBonifTrans > 0)) {
            valid = true;
            annee = String.valueOf(nbreAnneeBonifTrans);
        }
        if (isDemiAnnee != null) {
            valid = true;
            if (isDemiAnnee) {
                mois = "5";
            }
        }
        if (valid && !"00".equals(annee + mois)) {
            result = annee + mois;
        }
        return result;
    }

    /**
     * REAAL3A.YZNRED</br>
     * </br>
     * Format le champ <code>reductionAnticipation</code></br>
     * </br>
     * -> Retourne une chaîne vide <code>reductionAnticipation</code> est null</br>
     * -> Sinon format la valeur sur 5 positions avec des zéro à gauche</br>
     * </br>
     * 
     * @param reductionAnticipation La réduction pour anticipation
     * 
     * @return La valeur <code>reductionAnticipation</code> formatée
     */
    public String formatReductionAnticipation(Integer reductionAnticipation) {
        String result = "";
        if (reductionAnticipation != null && reductionAnticipation > 0) {
            result = String.valueOf(reductionAnticipation);
            result = indentLeftWithZero(result, 5);
        }
        return result;
    }

    /**
     * REAAL3A.YZTCOD (Boolean)</br>
     * </br>
     * Retourne la valeur <code>codeRevenuSplitte</code> formatée</br>
     * </br>
     * -> Retourne une chaîne vide si <code>codeRevenuSplitte</code> est null</br>
     * -> Sinon retourne '1' si <code>codeRevenuSplitte</code> == true</br>
     * -> Ou retourne '0' si <code>codeRevenuSplitte</code> == false</br>
     * </br>
     * 
     * @param codeRevenuSplitte La valeur 'code revenu splitté'
     * @return la valeur <code>codeRevenuSplitte</code> formatée
     */
    public String formatCodeRevenuSplitte(Boolean codeRevenuSplitte) {
        return formatBoolean(codeRevenuSplitte);
    }

    /**
     * REAAL3A.YZBSUR (Boolean)</br>
     * </br>
     * Retourne la valeur 'isSurvivant' formatée
     * </br>
     * -> Retourne une chaîne vide si <code>survivant</code> est null</br>
     * -> Sinon retourne '1' si <code>survivant</code> == true</br>
     * -> Ou retourne '0' si <code>survivant</code> == false</br>
     * </br>
     * 
     * @param survivant Si c'est un survivant
     * @return la valeur 'isSurvivant' formatée
     */
    public String formatIsSurvivant(Boolean survivant) {
        return formatBoolean(survivant);
    }

    /**
     * REAAL3A.YZDDEB (Date)</br>
     * </br>
     * Retourne la valeur <code>dateDebutAnticipation</code> formatée</br>
     * </br>
     * -> Retourne une chaîne vide si <code>dateDebutAnticipation</code> est null</br>
     * -> Sinon retourne la date formatée en AAMM</br>
     * </br>
     * 
     * @param dateDebutAnticipation La date de début d'anticipation
     * @return la valeur <code>dateDebutAnticipation</code> formatée
     */
    public String formatDateDebutAnticipation(JADate dateDebutAnticipation) throws IllegalArgumentException {
        return formatDateToMMAA(dateDebutAnticipation);
    }

    /**
     * REAAL3A.YZNANT (Integer)</br>
     * </br>
     * Format la valeur <code>nbreAnneeAnticipation</code> </br>
     * -> Retourne une chaîne vide si <code>nbreAnneeAnticipation</code> est null</br>
     * -> Sinon retourne la valeur sans formatage particulier
     * </br>
     * 
     * @param nbreAnneeAnticipation Le nombre d'année d'anticipation
     * @return LA valeur 'nombre d'année d'anticipation' formatée
     */
    public String formatNombreAnneeAnticipation(Integer nbreAnneeAnticipation) {
        String result = "";
        if (nbreAnneeAnticipation != null) {
            if (nbreAnneeAnticipation != 0) {
                result = String.valueOf(nbreAnneeAnticipation);
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Méthodes surchargées car format différents entre le format de base (9ème révision) et la 10ème

    /**
     * REAAL2A.YYLOAI (Integer)</br>
     * </br>
     * Retourne la valeur <code>officeAICompetent</code> formatée sur 3 position</br>
     * </br>
     * -> Retourne une chaîne vide si <code>officeAICompetent</code> est null</br>
     * -> Sinon retourne la valeur brut sans formatage</br>
     * </br>
     * 
     * @param officeAICompetent Le numéro de l'office AI compétent
     * @return la valeur <code>officeAICompetent</code> formatée
     */
    // TODO est-ce qu'il faut indenter avec des 0 à gauche ?
    @Override
    public String formatOfficeAICompetent(Integer officeAICompetent) {
        String result = "";
        if (officeAICompetent != null) {
            result = String.valueOf(officeAICompetent);
        }
        return result;
    }
}
