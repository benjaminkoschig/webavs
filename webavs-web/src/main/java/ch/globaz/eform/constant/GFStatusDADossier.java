package ch.globaz.eform.constant;

import globaz.globall.db.BSession;

import java.util.Arrays;

public enum GFStatusDADossier {
    BLANK("", ""),
    TO_SEND("82000011", "TO_SEND"),
    SEND("82000012", "SEND"),
    WAITING("82000013", "WAITING"),
    TREAT("82000014", "TREAT"),
    ENDED("82000015", "ENDED");

    String codeSystem;
    String code;

    GFStatusDADossier(String codeSystem, String code) {
        this.codeSystem = codeSystem;
        this.code = code;
    }

    public String getDesignation(BSession session) {
        return session.getLabel(code);
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCode() {
        return code;
    }

    public static GFStatusDADossier getStatusByCodeSystem(String codeSystem) {
        return Arrays.stream(GFStatusDADossier.values())
                .filter(status -> status.codeSystem.equals(codeSystem))
                .findFirst()
                .orElse(BLANK);
    }

    public static GFStatusDADossier getStatusByCode(String code) {
        return Arrays.stream(GFStatusDADossier.values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElse(BLANK);
    }
}
