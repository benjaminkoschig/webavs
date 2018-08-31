package ch.globaz.vulpecula.domain.models.registre;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Type de cotisation pour les cotisation de type caisse métier.
 * 
 */
public enum GenreCotisationAssociationProfessionnelle {
    AUCUN(-1),
    MEMBRE(68020001),
    NON_MEMBRE(68020002),
    NON_TAXE(68020003);

    private int codeSysteme;

    private GenreCotisationAssociationProfessionnelle(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>GenreCotisationCM</code> associée au code système passé en
     * paramètre.
     * 
     * @param value un code système valide de type GenreCotisationCM
     * @return une instance valide de <code>GenreCotisationCM</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
     */
    public static GenreCotisationAssociationProfessionnelle fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return GenreCotisationAssociationProfessionnelle.AUCUN;
        }
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (GenreCotisationAssociationProfessionnelle lienParente : GenreCotisationAssociationProfessionnelle.values()) {
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
    
    public static boolean isNonMembre(GenreCotisationAssociationProfessionnelle genre) {
    	return NON_MEMBRE.getValue().equals(genre.getValue());
    }

}
