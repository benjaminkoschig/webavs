package ch.globaz.exceptions;

/**
 * Représente les différentes langues possibles pour la localisation des
 * exceptions.
 * 
 */
public enum ExceptionLanguage {

    DEFAULT("fr"),
    FR("fr"),
    DE("de"),
    IT("it");

    private String language;

    private ExceptionLanguage(final String language) {
        this.language = language;
    }

    /**
     * Retourne une instance de <code>ExceptionLanguage</code> correspondant à
     * la langue passée en paramètre.
     * 
     * @param text
     *            un code iso de langue
     * @return l'instance de <code>ExceptionLanguage</code> correspondante
     * @throws IllegalArgumentException
     *             si aucune langue correspondante n'existe ou si le paramètre
     *             est <code>null</code>
     */
    public static ExceptionLanguage fromString(final String text) {
        if (text != null) {
            for (ExceptionLanguage lang : ExceptionLanguage.values()) {
                if (text.equalsIgnoreCase(lang.language)) {
                    return lang;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return language;
    }

}
