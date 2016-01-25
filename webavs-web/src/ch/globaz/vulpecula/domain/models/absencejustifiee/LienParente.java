package ch.globaz.vulpecula.domain.models.absencejustifiee;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * Lien de parent� utilis� en cas d'absence justifi�e pour cause de d�c�s.
 * 
 */
public enum LienParente {
    AUCUN(-1), // ne doit pas �tre persist�
    CONJOINT(68002001),
    PARENT(68002002),
    ENFANT(68002003),
    BEAU_PARENT(68002004),
    FRERE(68002005),
    SOEUR(68002006),
    GRAND_PARENT(68002007),
    BEAU_FILS(68002008),
    BELLE_FILLE(68002009),
    BEAU_FRERE(68002010),
    BELLE_SOEUR(68002011),
    PETIT_FILS(68002012),
    PETITE_FILLE(68002013);

    private int codeSysteme;

    private LienParente(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>LienParente</code> associ�e au code syst�me pass� en
     * param�tre.
     * 
     * @param value un code syst�me valide repr�sentant le lien de parent� associ� � l'absence justifi�e en cas de d�c�s
     * @return une instance valide de <code>LienParente</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
     */
    public static LienParente fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return LienParente.AUCUN;
        }
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (LienParente lienParente : LienParente.values()) {
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
