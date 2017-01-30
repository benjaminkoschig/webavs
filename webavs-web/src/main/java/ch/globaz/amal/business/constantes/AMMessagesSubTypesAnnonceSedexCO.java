package ch.globaz.amal.business.constantes;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMSousTypeMessageSedexCOLibellesSubside;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;

/**
 * Enum for subtypes messages sedex
 * 
 * @author CBU
 * 
 */
public enum AMMessagesSubTypesAnnonceSedexCO {
    LISTE_PERSONNE_NE_DEVANT_PAS_ETRE_POURSUIVIES("201"),
    CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE("202"),
    DECOMPTE_TRIMESTRIEL("401"),
    DECOMPTE_FINAL("402");

    public static Collection<AMMessagesSubTypesAnnonceSedexCO> getSortedEnums() {
        SortedMap<String, AMMessagesSubTypesAnnonceSedexCO> map = new TreeMap<String, AMMessagesSubTypesAnnonceSedexCO>();
        for (AMMessagesSubTypesAnnonceSedexCO st : AMMessagesSubTypesAnnonceSedexCO.values()) {
            map.put(st.getValue(), st);
        }
        return map.values();
    }

    public static AMMessagesSubTypesAnnonceSedexCO getSubType(String value) throws AnnonceSedexException {
        if (LISTE_PERSONNE_NE_DEVANT_PAS_ETRE_POURSUIVIES.getValue().equals(value)) {
            return LISTE_PERSONNE_NE_DEVANT_PAS_ETRE_POURSUIVIES;
        } else if (CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE.getValue().equals(value)) {
            return CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE;
        } else if (DECOMPTE_TRIMESTRIEL.getValue().equals(value)) {
            return DECOMPTE_TRIMESTRIEL;
        } else if (DECOMPTE_FINAL.getValue().equals(value)) {
            return DECOMPTE_FINAL;
        } else {
            throw new AnnonceSedexException("Subtype not found : " + value);
        }

    }

    public static String getSubTypeCSLibelle(String value) {
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        AMSousTypeMessageSedexCOLibellesSubside subtype = null;

        if (LISTE_PERSONNE_NE_DEVANT_PAS_ETRE_POURSUIVIES.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexCOLibellesSubside.LISTE_PERSONNE_NE_DEVANT_PAS_ETRE_POURSUIVIES;
        } else if (CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexCOLibellesSubside.CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE;
        } else if (DECOMPTE_TRIMESTRIEL.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexCOLibellesSubside.DECOMPTE_TRIMESTRIEL;
        } else if (DECOMPTE_FINAL.getValue().equals(value)) {
            subtype = AMSousTypeMessageSedexCOLibellesSubside.DECOMPTE_FINAL;
        } else {
            return "N/A";
        }

        return currentSession.getCodeLibelle(subtype.getValue());
    }

    private final String value;

    private AMMessagesSubTypesAnnonceSedexCO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}