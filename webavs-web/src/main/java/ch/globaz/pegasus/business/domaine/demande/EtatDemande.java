package ch.globaz.pegasus.business.domaine.demande;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum EtatDemande implements CodeSystemEnum<EtatDemande> {
    CS_EN_ATTENTE_JUSTIFICATIFS("64001001"),
    CS_EN_ATTENTE_CALCUL("64001002"),
    OCTROYE("64001003"),
    REOUVERT("64001009"),
    REFUSE("64001007"),
    RENONCE("64001006"),
    REVISION("64001008"),
    SUPPRIME("64001004"),
    TRANSFERE("64001005");

    private String value;

    EtatDemande(String csEtatDemande) {
        value = csEtatDemande;
    }

    public static EtatDemande fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, EtatDemande.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}
