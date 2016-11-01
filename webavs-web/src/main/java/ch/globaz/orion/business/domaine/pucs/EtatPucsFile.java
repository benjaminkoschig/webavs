package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum EtatPucsFile implements CodeSystemEnum<EtatPucsFile> {

    A_VALIDE("11020001"),
    REJETE("11020002"),
    A_TRAITER("11020003"),
    COMPTABILISE("*****");

    private String value;

    public static final String CODE_FAMILLE = "GEBETSWD";

    EtatPucsFile(String csEtat) {
        value = csEtat;
    }

    public static EtatPucsFile fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, EtatPucsFile.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}