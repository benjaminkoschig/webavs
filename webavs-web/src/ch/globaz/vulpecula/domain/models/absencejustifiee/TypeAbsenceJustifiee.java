package ch.globaz.vulpecula.domain.models.absencejustifiee;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Représente les types possibles d'absence justifiée.
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
     * Construit une instance valide de <code>TypeAbsenceJustifiee</code> associée au code système passé en paramètre.
     * 
     * @param value un code système valide représentant un type d'absence justifiée
     * @return une instance valide de <code>TypeAbsenceJustifiee</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
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
                + " ne correspond à aucun type d'absence justifié connu");
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
