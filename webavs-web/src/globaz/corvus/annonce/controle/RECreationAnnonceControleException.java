package globaz.corvus.annonce.controle;

/**
 * Exception qui peut être lancé par les classes de contrôles légaux sur les annonces de rentes
 * 
 * @see RECreationAnnonce9EmeRevisionControleLegaux
 * @see RECreationAnnonce10EmeRevisionControleLegaux
 * @author lga
 * 
 */
public class RECreationAnnonceControleException extends Exception {

    private static final long serialVersionUID = 1L;

    public RECreationAnnonceControleException(Exception exception) {
        super(exception);
    }

    public RECreationAnnonceControleException(String message) {
        super(message);
    }
}
