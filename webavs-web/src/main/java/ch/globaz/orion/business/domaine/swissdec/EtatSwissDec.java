package ch.globaz.orion.business.domaine.swissdec;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum EtatSwissDec implements CodeSystemEnum<EtatSwissDec> {

    A_VALIDE("11020001"),
    REJETE("11020001");

    private String value;

    public static final String CODE_FAMILLE = "GEBETSWD";

    EtatSwissDec(String csEtat) {
        value = csEtat;
    }

    public static EtatSwissDec fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, EtatSwissDec.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}