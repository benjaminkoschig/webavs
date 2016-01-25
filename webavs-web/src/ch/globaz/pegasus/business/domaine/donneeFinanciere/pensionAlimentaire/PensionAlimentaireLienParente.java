package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum PensionAlimentaireLienParente implements CodeSystemEnum<PensionAlimentaireLienParente> {
    EX_MARI("64033001"),
    MARI("64033002"),
    EX_EPOUSE("64033003"),
    EPOUSE("64033004"),
    PERE("64033005"),
    MERE("64033006"),
    BEAU_PERE("64033007"),
    BELLE_MERE("64033008"),
    FILS("64033009"),
    FILLE("64033010"),
    AUTRES("64033011"),
    INDEFINIT("0");

    private String value;

    PensionAlimentaireLienParente(String value) {
        this.value = value;
    }

    public static PensionAlimentaireLienParente fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, PensionAlimentaireLienParente.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
