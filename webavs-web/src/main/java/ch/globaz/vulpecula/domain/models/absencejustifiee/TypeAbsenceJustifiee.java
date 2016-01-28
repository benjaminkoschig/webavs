package ch.globaz.vulpecula.domain.models.absencejustifiee;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Repr�sente les types possibles d'absence justifi�e.
 * 
 */
public enum TypeAbsenceJustifiee {
    DEUIL(68001001),
    DEMENAGEMENT(68001002),
    INSPECTION(68001003),
    MARIAGE(68001004),
    NAISSANCE(68001005),
    INFO_RECRUTEMENT(68001006),
    LIBERATION(68001007),
    MARIAGE_ENFANT(68001008);

    private int codeSysteme;

    private TypeAbsenceJustifiee(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>TypeAbsenceJustifiee</code> associ�e au code syst�me pass� en param�tre.
     * 
     * @param value un code syst�me valide repr�sentant un type d'absence justifi�e
     * @return une instance valide de <code>TypeAbsenceJustifiee</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
     */
    public static TypeAbsenceJustifiee fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (TypeAbsenceJustifiee etatAbsenceJustifiee : TypeAbsenceJustifiee.values()) {
            if (etatAbsenceJustifiee.codeSysteme == intValue) {
                return etatAbsenceJustifiee;
            }
        }
        throw new IllegalArgumentException("La valeur " + value
                + " ne correspond � aucun type d'absence justifi� connu");
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
