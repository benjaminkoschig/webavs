package ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RenteAvsAiType implements CodeSystemEnum<RenteAvsAiType> {

    RENTE_10("64006001"),
    RENTE_11("64006025"),
    RENTE_12("64006026"),
    RENTE_13("64006002"),
    RENTE_14("64006003"),
    RENTE_15("64006004"),
    RENTE_16("64006005"),
    RENTE_20("64006006"),
    RENTE_21("64006027"),
    RENTE_22("64006028"),
    RENTE_23("64006007"),
    RENTE_24("64006008"),
    RENTE_25("64006009"),
    RENTE_26("64006010"),
    RENTE_33("64006011"),
    RENTE_34("64006012"),
    RENTE_35("64006013"),
    RENTE_36("64006022"),
    RENTE_43("64006029"),
    RENTE_44("64006030"),
    RENTE_45("64006014"),
    RENTE_46("64006031"),
    RENTE_50("64006015"),
    RENTE_51("64006032"),
    RENTE_52("64006033"),
    RENTE_53("64006023"),
    RENTE_54("64006016"),
    RENTE_55("64006017"),
    RENTE_56("64006037"),
    RENTE_70("64006018"),
    RENTE_71("64006034"),
    RENTE_72("64006035"),
    RENTE_73("64006024"),
    RENTE_74("64006019"),
    RENTE_75("64006020"),
    RENTE_76("64006036"),
    SANS_RENTE("64006021"),
    SANS_RENTE_RENTE_IJAI("64006038"),
    SANS_RENTE_INVALIDITE("64027003"),
    SANS_RENTE_SURVIVANT("64027002"),
    SANS_RENTE_VIEILLESSE("64027001");

    private String value;

    RenteAvsAiType(String value) {
        this.value = value;
    }

    public static RenteAvsAiType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RenteAvsAiType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}
