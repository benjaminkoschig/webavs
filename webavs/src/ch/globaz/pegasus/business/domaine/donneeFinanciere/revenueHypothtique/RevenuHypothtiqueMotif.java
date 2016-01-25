package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RevenuHypothtiqueMotif implements CodeSystemEnum<RevenuHypothtiqueMotif> {
    MOTIF_REVENU_HYPO_14A_OPC("64034001"),
    MOTIF_REVENU_HYPO_14B_OPC("64034002"),
    MOTIF_REVENU_HYPO_GAIN_RESONN_EXIGIBLE_AI("64034003"),
    MOTIF_REVENU_HYPO_ESTIMATION_OFS_ESS("64034004"),
    MOTIF_REVENU_HYPO_SELON_DERNIER_REVENU("64034005"),
    MOTIF_REVENU_HYPO_AUTRES("64034006");

    private String value;

    RevenuHypothtiqueMotif(String value) {
        this.value = value;
    }

    public static RevenuHypothtiqueMotif fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RevenuHypothtiqueMotif.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
