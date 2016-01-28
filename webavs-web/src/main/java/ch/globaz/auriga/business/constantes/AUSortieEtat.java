package ch.globaz.auriga.business.constantes;

import ch.globaz.auriga.business.exceptions.AurigaTechnicalException;

public enum AUSortieEtat {
    COMPTABILISEE("952002"),
    NON_COMPTABILISEE("952001");

    private String codeSystem;

    public static AUSortieEtat getEnumFromCodeSystem(String codeSystem) {

        for (AUSortieEtat etat : AUSortieEtat.values()) {
            if (etat.getCodeSystem().equals(codeSystem)) {
                return etat;
            }
        }

        throw new AurigaTechnicalException(AUSortieEtat.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");

    }

    private AUSortieEtat(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(AUSortieEtat.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
