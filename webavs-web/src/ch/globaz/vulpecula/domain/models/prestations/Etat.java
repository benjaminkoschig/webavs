package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Repr�sente les �tats possibles d'un �tat de prestation.
 * 
 */
public enum Etat {
    SAISIE(68003001),
    TRAITEE(68003002),
    COMPTABILISEE(68003003);

    private int codeSysteme;

    private Etat(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>Etat</code> associ�e au code syst�me pass� en param�tre.
     * 
     * @param value un code syst�me valide repr�sentant un �tat de prestation (absence justifi�e, cong� pay�, ...)
     * @return une instance valide de <code>Etat</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
     */
    public static Etat fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (Etat etatAbsenceJustifiee : Etat.values()) {
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

}
