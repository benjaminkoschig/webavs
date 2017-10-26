package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum PcaType implements CodeSystemEnum<PcaType> {
    VIELLESSE("64027001"),
    SURVIVANT("64027002"),
    INVALIDITE("64027003");

    private String value;

    PcaType(String value) {
        this.value = value;
    }

    public static PcaType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, PcaType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isAi() {
        return INVALIDITE.equals(this);
    }

    public boolean isAvs() {
        return VIELLESSE.equals(this) || SURVIVANT.equals(this);
    }
}
