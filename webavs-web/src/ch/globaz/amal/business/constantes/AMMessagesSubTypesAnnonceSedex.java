package ch.globaz.amal.business.constantes;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMSousTypeMessageSedexLibellesSubside;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;

/**
 * Enum for subtypes messages sedex
 * 
 * @author CBU
 * 
 */
public enum AMMessagesSubTypesAnnonceSedex {
    CONFIRMATION_DECISION("102"),
    CONFIRMATION_INTERRUPTION("202"),
    DECOMPTE_ANNUEL("701"),
    DEMANDE_RAPPORT_ASSURANCE("401"),
    EFFECTIFS_ASSURES("601"),
    ETAT_DECISIONS("501"),
    INTERRUPTION("201"),
    MUTATION_RAPPORT_ASSURANCE("301"),
    NOUVELLE_DECISION("101"),
    REJET_DECISION("103"),
    REJET_INTERRUPTION("203"),
    REPONSE_RAPPORT_ASSURANCE("402");

    public static Collection<AMMessagesSubTypesAnnonceSedex> getSortedEnums() {
        SortedMap<String, AMMessagesSubTypesAnnonceSedex> map = new TreeMap<String, AMMessagesSubTypesAnnonceSedex>();
        for (AMMessagesSubTypesAnnonceSedex st : AMMessagesSubTypesAnnonceSedex.values()) {
            map.put(st.getValue(), st);
        }
        return map.values();
    }

    public static AMMessagesSubTypesAnnonceSedex getSubType(String value) throws AnnonceSedexException {
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

    public static String getSubTypeCSLibelle(String value) {
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        AMSousTypeMessageSedexLibellesSubside subtype = null;

        if (CONFIRMATION_DECISION.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.CONFIRMATION_DECISION;
        } else if (CONFIRMATION_INTERRUPTION.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.CONFIRMATION_INTERRUPTION;
        } else if (DECOMPTE_ANNUEL.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.DECOMPTE_ANNUEL;
        } else if (DEMANDE_RAPPORT_ASSURANCE.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.DEMANDE_RAPPORT_ASSURANCE;
        } else if (EFFECTIFS_ASSURES.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.EFFECTIFS_ASSURES;
        } else if (ETAT_DECISIONS.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.ETAT_DECISIONS;
        } else if (INTERRUPTION.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.INTERRUPTION;
        } else if (MUTATION_RAPPORT_ASSURANCE.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.MUTATION_RAPPORT_ASSURANCE;
        } else if (NOUVELLE_DECISION.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.NOUVELLE_DECISION;
        } else if (REJET_DECISION.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.REJET_DECISION;
        } else if (REJET_INTERRUPTION.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.REJET_INTERRUPTION;
        } else if (REPONSE_RAPPORT_ASSURANCE.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexLibellesSubside.REPONSE_RAPPORT_ASSURANCE;
        } else {
            return "N/A";
        }

        return currentSession.getCodeLibelle(subtype.getValue());
    }

    private final String value;

    private AMMessagesSubTypesAnnonceSedex(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}