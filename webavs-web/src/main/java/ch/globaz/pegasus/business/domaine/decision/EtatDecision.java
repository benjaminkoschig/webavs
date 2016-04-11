package ch.globaz.pegasus.business.domaine.decision;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.pegasus.business.constantes.IPCDecision;

public enum EtatDecision implements CodeSystemEnum<EtatDecision> {

    PRE_VALIDE(IPCDecision.CS_ETAT_PRE_VALIDE),
    ENREGISTRE(IPCDecision.CS_ETAT_ENREGISTRE),
    VALIDE(IPCDecision.CS_ETAT_DECISION_VALIDE);

    private String value;

    EtatDecision(String csEtatDecision) {
        value = csEtatDecision;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static EtatDecision fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, EtatDecision.class);
    }

    public boolean isPreValide() {
        return PRE_VALIDE.equals(this);
    }

    public boolean isEnregistre() {
        return ENREGISTRE.equals(this);
    }

    public boolean isValide() {
        return VALIDE.equals(this);
    }
}
