package ch.globaz.vulpecula.domain.models.registre;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Type de cotisation pour les cotisation de type caisse m�tier.
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
     * Construit une instance valide de <code>GenreCotisationCM</code> associ�e au code syst�me pass� en
     * param�tre.
     * 
     * @param value un code syst�me valide de type GenreCotisationCM
     * @return une instance valide de <code>GenreCotisationCM</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
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
     * Retourne un <code>String</code> repr�sentant le code syst�me associ�.
     * 
     * @return un <code>String</code> repr�sentant le code syst�me associ�.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }
    
    public static boolean isNonMembre(GenreCotisationAssociationProfessionnelle genre) {
    	return NON_MEMBRE.getValue().equals(genre.getValue());
    }

}
