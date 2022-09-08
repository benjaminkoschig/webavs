package ch.globaz.eform.constant;

import globaz.globall.db.BSession;

import java.util.Arrays;

public enum GFTypeDADossier {
    BLANK("", ""),
    SOLICITATION("82000001", "SOLICITAT"),
    SEND_TYPE("82000002", "SEND_TYPE"),
    RECEPTION("82000003", "RECEPTION");

    String codeSystem;
    String code;

    GFTypeDADossier(String codeSystem, String code) {
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

    public static GFTypeDADossier getByCodeSystem(String codeSystem) {
        return Arrays.stream(GFTypeDADossier.values())
                .filter(type -> type.codeSystem.equals(codeSystem))
                .findFirst()
                .orElse(BLANK);
    }

    public static GFTypeDADossier getByCode(String code) {
        return Arrays.stream(GFTypeDADossier.values())
                .filter(type -> type.code.equals(code))
                .findFirst()
                .orElse(BLANK);
    }
}
