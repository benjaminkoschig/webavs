package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Représente les trois types de prestations possibles
 * 
 */
public enum TypePrestation {
    ABSENCES_JUSTIFIEES(68009001),
    CONGES_PAYES(68009002),
    SERVICES_MILITAIRE(68009003);

    private int codeSysteme;

    private TypePrestation(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>TypePrestation</code> associée au code système passé en paramètre.
     * 
     * @param value un code système valide représentant un type de prestation (absence justifiée, congé payé, ...)
     * @return une instance valide de <code>TypePrestation</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
     */
    public static TypePrestation fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (TypePrestation etatAbsenceJustifiee : TypePrestation.values()) {
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

}
