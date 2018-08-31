package ch.globaz.vulpecula.domain.models.registre;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Type de cotisation pour les cotisation de type caisse m�tier.
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
     * Construit une instance valide de <code>CategorieFacture</code> associ�e au code syst�me pass� en
     * param�tre.
     * 
     * @param value un code syst�me valide de type CategorieFacture
     * @return une instance valide de <code>CategorieFacture</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
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
     * Retourne un <code>String</code> repr�sentant le code syst�me associ�.
     * 
     * @return un <code>String</code> repr�sentant le code syst�me associ�.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

}
