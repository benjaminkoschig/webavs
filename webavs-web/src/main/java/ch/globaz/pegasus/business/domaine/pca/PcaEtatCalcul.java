package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum PcaEtatCalcul implements CodeSystemEnum<PcaEtatCalcul> {

    OCTROYE("64061001"),
    OCTROY_PARTIEL("64061002"),
    REFUSE("64061003");

    private String value;

    PcaEtatCalcul(String value) {
        this.value = value;
    }

    public boolean isOctroiePartiel() {
        return PcaEtatCalcul.OCTROY_PARTIEL.equals(this);
    }

    public boolean isOctroie() {
        return PcaEtatCalcul.OCTROYE.equals(this);
    }

    public boolean isRefus() {
        return PcaEtatCalcul.REFUSE.equals(this);
    }

    public boolean isOctroieOuOctroiePartiel() {
        return isOctroie() || isOctroiePartiel();
    }

    public static PcaEtatCalcul fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, PcaEtatCalcul.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
