package ch.globaz.vulpecula.ws.bean;

import javax.xml.bind.annotation.XmlType;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * @author jpa
 * 
 *         D�finis tous les types d'assurances possible.
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
     * Construit une instance valide de <code>Type d'assurance</code> associ�e au code syst�me pass� en param�tre.
     * 
     * @param value un code syst�me valide repr�sentant un type d'assurance
     * @return une instance valide de <code>TypeAssurance</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
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
     * Retourne un <code>String</code> repr�sentant le code syst�me associ�.
     * 
     * @return un <code>String</code> repr�sentant le code syst�me associ�.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

    // Warning, pour retour portail, � mettre � jour en cas de changement
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
