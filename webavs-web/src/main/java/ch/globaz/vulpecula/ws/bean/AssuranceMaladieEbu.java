package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlType;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * @author jpa
 * 
 *         Définis tous les types d'assurances possible.
 * 
 */
@XmlType(name = "assuranceMaladie")
public enum AssuranceMaladieEbu {
    HELSANA(1),
    CSS(2),
    MUTUEL(3),
    NON_CTT(4);

    private int codeSysteme;

    private AssuranceMaladieEbu(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>Type d'assurance</code> associée au code système passé en paramètre.
     * 
     * @param value un code système valide représentant un type d'assurance
     * @return une instance valide de <code>TypeAssurance</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
     */
    public static AssuranceMaladieEbu fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (AssuranceMaladieEbu etatAbsenceJustifiee : AssuranceMaladieEbu.values()) {
            if (etatAbsenceJustifiee.codeSysteme == intValue) {
                return etatAbsenceJustifiee;
            }
        }
        throw new GlobazTechnicalException(ExceptionMessage.UNKNOWN_VALUE_CODE_SYSTEME);
    }

    /**
     * Retourne un <code>String</code> représentant le code système associé.
     * 
     * @return un <code>String</code> représentant le code système associé.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

    // Warning, pour retour portail, à mettre à jour en cas de changement
    public static AssuranceMaladieEbu getFromMetierId(String idAdmin) {
        if ("1000000".equals(idAdmin)) {
            return AssuranceMaladieEbu.HELSANA;
        }
        if ("1000001".equals(idAdmin)) {
            return AssuranceMaladieEbu.CSS;
        }
        if ("1000002".equals(idAdmin)) {
            return AssuranceMaladieEbu.MUTUEL;
        }
        if ("1000043".equals(idAdmin)) {
            return AssuranceMaladieEbu.NON_CTT;
        } else {
            return null;
        }
    }

}
