package globaz.osiris.db.irrecouvrable;

/**
 * Représente les différents types de lignes de poste possibles (employeur, salarié, simple).
 * 
 * @author bjo
 * 
 */
public enum CATypeLigneDePoste {
    EMPLOYEUR("239001"),
    SALARIE("239002"),
    SIMPLE("239003");

    private String codeSystem;

    public static CATypeLigneDePoste getEnumFromCodeSystem(String codeSystem) {
        for (CATypeLigneDePoste type : CATypeLigneDePoste.values()) {
            if (type.getCodeSystem().equals(codeSystem)) {
                return type;
            }
        }

        throw new IllegalArgumentException(CATypeLigneDePoste.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");
    }

    private CATypeLigneDePoste(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
