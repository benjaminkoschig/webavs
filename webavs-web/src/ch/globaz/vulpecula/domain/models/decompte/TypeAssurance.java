package ch.globaz.vulpecula.domain.models.decompte;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

/**
 * @author jpa
 * 
 *         Définis tous les types d'assurances possible.
 * 
 */
public enum TypeAssurance {
    COTISATION_AVS_AI(812001),
    COTISATION_AF(812002),
    FRAIS_ADMINISTRATION(812003),
    AUTRES(812004),
    ASSURANCE_MATERNITE(812005),
    ASSURANCE_CHOMAGE(812006),
    COTISATION_AC2(812007),
    COTISATION_LAA(812008),
    COTISATION_FFPP_CAPITATION(812009),
    AFI(812010),
    REDUCTION_PFA_AVS_AF(812011),
    REDUCTION_PFA_DSE(812012),
    REDUCTION_AF_AVS_AF(812013),
    REDUCTION_AF_DSE(812014),
    TAXE_AMENDE(812017),
    IMPOT_SOURCE(812018),
    TAXE_CO2(812019),
    PREVHOR(812020),
    MANUEL(812021),
    COTISATION_PERIODE(812022),
    COTISATION_FEDERATIVE(812023),
    PC_FAMILLE(812024),
    LAE_CICICAM(812026),
    COTISATION_FFPP_MASSE(812027),
    COTISATION_SM_AJ(68904001),
    CONTRIBUTION_GENERALE(68904002),
    CONGES_PAYES(68904003),
    COTISATION_LPP(68904004),
    COTISATION_RETAVAL(68904005),
    CONTRIBUTION_GENERALE_REDUITE(68904006),
    ASSURANCE_MALADIE(68904007),
    CPR_TRAVAILLEUR(68904008),
    CPR_EMPLOYEUR(68904009),
    UNDEFINED(0);

    private int codeSysteme;

    private TypeAssurance(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>Type d'assurance</code> associée au code système passé en paramètre.
     * 
     * @param value un code système valide représentant un type d'assurance
     * @return une instance valide de <code>TypeAssurance</code>
     * @throws GlobazTechnicalException si le code système passé en paramètre n'est pas valide
     */
    public static TypeAssurance fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (TypeAssurance etatAbsenceJustifiee : TypeAssurance.values()) {
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
