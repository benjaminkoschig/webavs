package globaz.babel.utils;

public class TexteIntrouvalbeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TexteIntrouvalbeException(String cleCatalogue, int niveau, int position) {
        super("Texte introuvable dans le catalogue" + cleCatalogue + " à sur le niveau: " + niveau
                + " et la position: " + position);
    }
}
