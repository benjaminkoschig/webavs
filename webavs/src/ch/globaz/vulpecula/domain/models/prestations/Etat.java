package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Représente les états possibles d'un état de prestation.
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
     * Construit une instance valide de <code>Etat</code> associée au code système passé en paramètre.
     * 
     * @param value un code système valide représentant un état de prestation (absence justifiée, congé payé, ...)
     * @return une instance valide de <code>Etat</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
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
     * Retourne un <code>String</code> représentant le code système associé.
     * 
     * @return un <code>String</code> représentant le code système associé.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

}
