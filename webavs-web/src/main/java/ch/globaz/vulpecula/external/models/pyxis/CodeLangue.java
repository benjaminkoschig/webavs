package ch.globaz.vulpecula.external.models.pyxis;


/**
 * Gestion des langues de l'application
 * 
 * @since WebBMS 0.01.03
 */
public enum CodeLangue {
    FR(503001, "fr"),
    DE(503002, "de"),
    EN(503003, "en"),
    IT(503004, "it"),
    RM(503005, "rm");

    private int codeSysteme;
    private String codeIso;

    private CodeLangue(final int value, String codeIso) {
        codeSysteme = value;
        this.codeIso = codeIso;
    }

    /**
     * Retourne le code système représentant la langue
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

    /**
     * @return le code ISO de la langue
     */
    public String getCodeIsoLangue() {
        return codeIso;
    }

    /**
     * Construction de l'énumération à partir d'un code système.
     * Si le code passé en paramètre est NULL ou ne correspond pas à une langue, retourne par défaut FR.
     * 
     * @param le code système
     * @return le code langue {@link CodeLangue} ; par défaut FR
     * @exception IllegalArgumentException si le paramètre n'est pas un nombre entier.
     */
    public static final CodeLangue fromValue(String codeSysteme) {
        CodeLangue defaultCodeLangue = CodeLangue.FR;

        if (codeSysteme == null || codeSysteme.length() == 0) {
            return defaultCodeLangue;
        }

        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(codeSysteme);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de langue");
        }

        for (CodeLangue langue : CodeLangue.values()) {
            if (langue.codeSysteme == valueAsInt) {
                return langue;
            }
        }
        return defaultCodeLangue;
    }
}
