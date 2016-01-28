package globaz.corvus.annonce.formatter;

/**
 * Exception pouvant �tre lanc�e par les formateurs d'annonce 9 et 10�me r�vision
 * 
 * @see RECreationAnnonce9EmeRevisionFormatter
 * @see RECreationAnnonce10EmeRevisionFormatter
 * @author lga
 * 
 */
public class RECreationAnnonceFormatterException extends Exception {

    private static final long serialVersionUID = 1L;

    public RECreationAnnonceFormatterException(Exception exception) {
        super(exception);
    }

    public RECreationAnnonceFormatterException(String message) {
        super(message);
    }
}
