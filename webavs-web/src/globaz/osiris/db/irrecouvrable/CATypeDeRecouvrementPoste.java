package globaz.osiris.db.irrecouvrable;

/**
 * Représente les différents types de recouvrementPoste possibles (employeur, salarié, simple).
 * 
 * @author sch
 * 
 */
public enum CATypeDeRecouvrementPoste {
    EMPLOYEUR("239001"),
    SALARIE("239002"),
    SIMPLE("239003");

    private String codeSystem;

    public static CATypeDeRecouvrementPoste getEnumFromCodeSystem(String codeSystem) {
        for (CATypeDeRecouvrementPoste type : CATypeDeRecouvrementPoste.values()) {
            if (type.getCodeSystem().equals(codeSystem)) {
                return type;
            }
        }

        throw new IllegalArgumentException(CATypeDeRecouvrementPoste.class.getName()
                + " : no enum founded for codeSystem(" + codeSystem + ")");
    }

    private CATypeDeRecouvrementPoste(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
