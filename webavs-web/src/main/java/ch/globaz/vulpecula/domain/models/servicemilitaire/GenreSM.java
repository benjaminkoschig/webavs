package ch.globaz.vulpecula.domain.models.servicemilitaire;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Lien de parent� utilis� en cas d'absence justifi�e pour cause de d�c�s.
 * 
 */
public enum GenreSM {
    AUCUN(-1),
    COURS_DE_REPETITION(68008001),
    ECOLE_DE_RECRUE(68008002),
    SERVICE_AVANCEMENT(68008003),
    RECRUTEMENT(68008004),
    SOUS_OFFICIERS_SERVICE_LONG(68008005),
    PROTECTION_CIVILE(68008006),
    PROTECTION_CIVILE_BASE(68008007),
    PROTECTION_CIVILE_SPECIALISTES(68008008),
    JEUNESSE_SPORT(68008009),
    SERVICE_CIVILE(68008010),
    SERVICE_CIVILE_BASE(68008011),
    FONCTION_PUBLIQUE(68008012);

    private int codeSysteme;

    private GenreSM(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>GenreSM</code> associ�e au code syst�me pass� en
     * param�tre.
     * 
     * @param value un code syst�me valide repr�sentant un genre de prestation SM
     * @return une instance valide de <code>GenreSM</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
     */
    public static GenreSM fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return GenreSM.AUCUN;
        }
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (GenreSM lienParente : GenreSM.values()) {
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
