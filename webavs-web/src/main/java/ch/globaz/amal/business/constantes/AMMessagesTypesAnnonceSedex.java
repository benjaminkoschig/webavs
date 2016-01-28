package ch.globaz.amal.business.constantes;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;

/**
 * Enum for types messages sedex
 * 
 * @author CBU
 * 
 */
public enum AMMessagesTypesAnnonceSedex {
    CONFIRMATION_DECISION("5211"),
    CONFIRMATION_INTERRUPTION("5211"),
    DECOMPTE_ANNUEL("5214"),
    DEMANDE_RAPPORT_ASSURANCE("5202"),
    EFFECTIFS_ASSURES("5213"),
    ETAT_DECISIONS("5203"),
    INTERRUPTION("5201"),
    MUTATION_RAPPORT_ASSURANCE("5211"),
    NOUVELLE_DECISION("5201"),
    REJET_DECISION("5211"),
    REJET_INTERRUPTION("5211"),
    REPONSE_RAPPORT_ASSURANCE("5212");

    public static AMMessagesTypesAnnonceSedex getType(String value) throws AnnonceSedexException {

        value = JadeStringUtil.fillWithZeroes(value, 4);

        if (CONFIRMATION_DECISION.getValue().equals(value)) {
            return CONFIRMATION_DECISION;
        } else if (CONFIRMATION_INTERRUPTION.getValue().equals(value)) {
            return CONFIRMATION_INTERRUPTION;
        } else if (DECOMPTE_ANNUEL.getValue().equals(value)) {
            return DECOMPTE_ANNUEL;
        } else if (DEMANDE_RAPPORT_ASSURANCE.getValue().equals(value)) {
            return DEMANDE_RAPPORT_ASSURANCE;
        } else if (EFFECTIFS_ASSURES.getValue().equals(value)) {
            return EFFECTIFS_ASSURES;
        } else if (ETAT_DECISIONS.getValue().equals(value)) {
            return ETAT_DECISIONS;
        } else if (INTERRUPTION.getValue().equals(value)) {
            return INTERRUPTION;
        } else if (MUTATION_RAPPORT_ASSURANCE.getValue().equals(value)) {
            return MUTATION_RAPPORT_ASSURANCE;
        } else if (NOUVELLE_DECISION.getValue().equals(value)) {
            return NOUVELLE_DECISION;
        } else if (REJET_DECISION.getValue().equals(value)) {
            return REJET_DECISION;
        } else if (REJET_INTERRUPTION.getValue().equals(value)) {
            return REJET_INTERRUPTION;
        } else if (REPONSE_RAPPORT_ASSURANCE.getValue().equals(value)) {
            return REPONSE_RAPPORT_ASSURANCE;
        } else {
            throw new AnnonceSedexException("Subtype not found : " + value);
        }

    }

    // Value storage
    private final String value;

    // Default private constructor
    private AMMessagesTypesAnnonceSedex(String value) {
        this.value = value;
    }

    // value getter
    public String getValue() {
        return value;
    }
}
