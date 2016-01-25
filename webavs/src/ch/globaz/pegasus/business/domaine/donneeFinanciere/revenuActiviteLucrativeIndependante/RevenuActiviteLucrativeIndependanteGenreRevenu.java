package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RevenuActiviteLucrativeIndependanteGenreRevenu implements
        CodeSystemEnum<RevenuActiviteLucrativeIndependanteGenreRevenu> {
    GENRE_REVENU_ACT_LUCR_INDEP_NON_AGRICOLE("64038001"),
    GENRE_REVENU_ACT_LUCR_AGRICOLE_FORESTIER("64038002");

    private String value;

    RevenuActiviteLucrativeIndependanteGenreRevenu(String value) {
        this.value = value;
    }

    public static RevenuActiviteLucrativeIndependanteGenreRevenu fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RevenuActiviteLucrativeIndependanteGenreRevenu.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
