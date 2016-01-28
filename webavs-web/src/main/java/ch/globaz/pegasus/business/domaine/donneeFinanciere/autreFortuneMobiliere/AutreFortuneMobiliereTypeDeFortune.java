package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum AutreFortuneMobiliereTypeDeFortune implements CodeSystemEnum<AutreFortuneMobiliereTypeDeFortune> {
    TABLEAUX("64040001"),
    BIJOUX("64040002"),
    METAUX_PRECIEUX("64040003"),
    TIMBRES("64040004"),
    AUTRES("64040005");

    private String value;

    AutreFortuneMobiliereTypeDeFortune(String value) {
        this.value = value;
    }

    public static AutreFortuneMobiliereTypeDeFortune fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, AutreFortuneMobiliereTypeDeFortune.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}
