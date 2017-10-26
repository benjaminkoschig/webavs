package ch.globaz.pegasus.business.domaine.decision;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.pegasus.business.constantes.IPCDecision;

public enum TypeDecision implements CodeSystemEnum<TypeDecision> {

    ADAPTATION_APRES_CALCUL(IPCDecision.CS_TYPE_ADAPTATION_AC, true),
    OCTROI_APRES_CALCUL(IPCDecision.CS_TYPE_OCTROI_AC, true),
    PARTIEL_APRES_CALCUL(IPCDecision.CS_TYPE_PARTIEL_AC, true),
    REFUS_APRES_CALCUL(IPCDecision.CS_TYPE_REFUS_AC, true),
    REFUS_SANS_CALCUL(IPCDecision.CS_TYPE_REFUS_SC, false),
    SUPPRESSION_SANS_CALCUL(IPCDecision.CS_TYPE_SUPPRESSION_SC, false);

    private String value;
    private boolean isApresCalcul;

    TypeDecision(String csTypeDecision, boolean isApresCalcul) {
        value = csTypeDecision;
        this.isApresCalcul = isApresCalcul;
    }

    public static TypeDecision fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, TypeDecision.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isRefus() {
        return isRefusApresCalcul() || isRefusSansCalcul();
    }

    public boolean isSuppression() {
        return SUPPRESSION_SANS_CALCUL.equals(this);
    }

    public boolean isAdaptation() {
        return ADAPTATION_APRES_CALCUL.equals(this);
    }

    public boolean isOctroi() {
        return OCTROI_APRES_CALCUL.equals(this);
    }

    public boolean isOctroiOrPartiel() {
        return isOctroi() || isPartiel();
    }

    public boolean isPartiel() {
        return PARTIEL_APRES_CALCUL.equals(this);
    }

    public boolean isRefusApresCalcul() {
        return REFUS_APRES_CALCUL.equals(this);
    }

    public boolean isRefusSansCalcul() {
        return REFUS_SANS_CALCUL.equals(this);
    }

    public boolean isTypeApresCalcul() {
        return isApresCalcul;
    }

}
