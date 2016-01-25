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
     * Retourne le code syst�me repr�sentant la langue
     * 
     * @return String repr�sentant un code syst�me
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
     * Construction de l'�num�ration � partir d'un code syst�me.
     * Si le code pass� en param�tre est NULL ou ne correspond pas � une langue, retourne par d�faut FR.
     * 
     * @param le code syst�me
     * @return le code langue {@link CodeLangue} ; par d�faut FR
     * @exception IllegalArgumentException si le param�tre n'est pas un nombre entier.
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
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de langue");
        }

        for (CodeLangue langue : CodeLangue.values()) {
            if (langue.codeSysteme == valueAsInt) {
                return langue;
            }
        }
        return defaultCodeLangue;
    }
}
