package ch.globaz.eform.constant;

import globaz.globall.db.BSession;

import java.util.Arrays;

public enum GFSexeDaDossier {
    HOMME("516001", "HOMME"),
    FEMME("516002", "FEMME");

    private String codeSystem;
    private String label;

    GFSexeDaDossier(String codeSystem, String label) {
        this.codeSystem = codeSystem;
        this.label = label;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getDesignation(BSession session) {
        return session.getLabel(label);
    }

    public static GFSexeDaDossier getByCodeSystem(String codeSystem) {
        return Arrays.stream(GFSexeDaDossier.values())
                .filter(sexe -> sexe.codeSystem.equals(codeSystem))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Code system du sexe non reconnu!"));
    }
}
