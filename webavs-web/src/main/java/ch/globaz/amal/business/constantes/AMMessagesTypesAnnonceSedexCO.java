package ch.globaz.amal.business.constantes;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;

/**
 * Enum for types messages sedex co
 * 
 * @author CBU
 * 
 */
public enum AMMessagesTypesAnnonceSedexCO {
    LISTE_PERSONNES_NE_DEVANT_PAS_ETRE_POURSUIVIES("5222"),
    CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE("5232"),
    DECOMPTE_TRIMESTRIEL("5234"),
    DEMANDE_FINAL("5234");

    public static AMMessagesTypesAnnonceSedexCO getType(String value) throws AnnonceSedexException {

        value = JadeStringUtil.fillWithZeroes(value, 4);

        if (LISTE_PERSONNES_NE_DEVANT_PAS_ETRE_POURSUIVIES.getValue().equals(value)) {
            return LISTE_PERSONNES_NE_DEVANT_PAS_ETRE_POURSUIVIES;
        } else if (CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE.getValue().equals(value)) {
            return CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE;
        } else if (DECOMPTE_TRIMESTRIEL.getValue().equals(value)) {
            return DECOMPTE_TRIMESTRIEL;
        } else if (DEMANDE_FINAL.getValue().equals(value)) {
            return DEMANDE_FINAL;
        } else {
            throw new AnnonceSedexException("Subtype not found : " + value);
        }

    }

    // Value storage
    private final String value;

    // Default private constructor
    private AMMessagesTypesAnnonceSedexCO(String value) {
        this.value = value;
    }

    // value getter
    public String getValue() {
        return value;
    }
}
