package ch.globaz.corvus.process.dnra;

/**
 * Code de mutation
 * 0 Valable dans le fichier de concordance mensuel (fichier global)
 * 1 Valable dans le fichier des modifications quotidiennes (fichier incrémental)
 * 2 Inactivé
 * 3 Invalidé
 * 4 Flag de validité
 */
public enum TypeMutation {

    MENSUEL("0"),
    QUOTIDIEN("1"),
    INACTIVE("2"),
    INVALIDE("3");

    private String code;

    private TypeMutation(String code) {
        this.code = code;
    }

    public boolean isInactive() {
        return INACTIVE.equals(this);
    }

    public boolean isInvalide() {
        return INVALIDE.equals(this);
    }

    public static TypeMutation parse(String code) {
        if (MENSUEL.code.equals(code)) {
            return MENSUEL;
        } else if (QUOTIDIEN.code.equals(code)) {
            return QUOTIDIEN;
        } else if (INACTIVE.code.equals(code)) {
            return INACTIVE;
        } else if (INVALIDE.code.equals(code)) {
            return INVALIDE;
        }
        throw new IllegalArgumentException("The value [" + code + "] is not valid for the TypeMutation of type ["
                + TypeMutation.class.getName() + "]");
    }

}
