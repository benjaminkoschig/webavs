package ch.globaz.eform.constant;

import globaz.globall.db.BSession;

import java.util.Arrays;

public enum GFStatusEForm {
    BLANK("", ""),
    RECEIVE("81000001", "RECEIVE"),
    PROCESSING("81000002", "PROCESSING"),
    TREAT("81000003", "TREAT"),
    TO_VALIDAT("81000004", "TO_VALIDATE"),
    IN_ERROR("81000005", "IN_ERROR"),
    REJECTED("81000006", "REJECTED"),
    TREATY("81000007", "TREATY");

    String codeSystem;
    String code;


    GFStatusEForm(String codeSystem, String code) {
        this.codeSystem = codeSystem;
        this.code = code;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCode() {
        return code;
    }

    public String getDesignation(BSession session) {
        return session.getLabel(code);
    }

    public static GFStatusEForm getStatusByCodeSystem(String codeSystem) {
        return Arrays.stream(GFStatusEForm.values())
                .filter(status -> status.codeSystem.equals(codeSystem))
                .findFirst()
                .orElse(BLANK);
    }

    public static GFStatusEForm getStatusByCode(String code) {
        return Arrays.stream(GFStatusEForm.values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElse(BLANK);
    }
}
