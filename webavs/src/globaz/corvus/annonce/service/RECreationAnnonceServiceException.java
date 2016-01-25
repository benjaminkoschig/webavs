package globaz.corvus.annonce.service;

public class RECreationAnnonceServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public RECreationAnnonceServiceException(Exception exception) {
        super(exception);
    }

    public RECreationAnnonceServiceException(String message) {
        super(message);
    }
}
