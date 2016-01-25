package ch.globaz.aries.business.constantes;

import ch.globaz.aries.business.exceptions.AriesTechnicalException;

public enum ARSortieEtat {
    COMPTABILISEE("963002"),
    NON_COMPTABILISEE("963001");

    private String codeSystem;

    public static ARSortieEtat getEnumFromCodeSystem(String codeSystem) {

        for (ARSortieEtat etat : ARSortieEtat.values()) {
            if (etat.getCodeSystem().equals(codeSystem)) {
                return etat;
            }
        }

        throw new AriesTechnicalException(ARSortieEtat.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");

    }

    private ARSortieEtat(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(ARSortieEtat.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
