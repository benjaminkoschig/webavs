package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum PcaGenre implements CodeSystemEnum<PcaGenre> {
    DOMICILE("64026001"),
    HOME("64026002");

    private String value;

    PcaGenre(String value) {
        this.value = value;
    }

    public static PcaGenre fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, PcaGenre.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isDomicile() {
        return equals(DOMICILE);
    }

    public boolean isHome() {
        return equals(HOME);
    }
}
