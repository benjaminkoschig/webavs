package ch.globaz.vulpecula.domain.models.registre;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Type de cotisation pour les cotisation de type caisse métier.
 * 
 */
public enum CategorieFactureAssociationProfessionnelle {
    A_FACTURER(68032001),
    NON_FACTURE(68032002),
    RABAIS_SPECIAL(68032003),
    SUPPRIMER(68032004), 
    SOLDE_MINIME(68032005);

    private int codeSysteme;

    private CategorieFactureAssociationProfessionnelle(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>CategorieFacture</code> associée au code système passé en
     * paramètre.
     * 
     * @param value un code système valide de type CategorieFacture
     * @return une instance valide de <code>CategorieFacture</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
     */
    public static CategorieFactureAssociationProfessionnelle fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (CategorieFactureAssociationProfessionnelle lienParente : CategorieFactureAssociationProfessionnelle
                .values()) {
            if (lienParente.codeSysteme == intValue) {
                return lienParente;
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
