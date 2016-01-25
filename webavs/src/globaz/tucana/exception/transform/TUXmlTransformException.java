package globaz.tucana.exception.transform;

/**
 * Erreur levée en cas de problème de transformation TagLib
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUXmlTransformException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUXmlTransformException() {
        super();
    }

    /**
     * @param arg0
     */
    public TUXmlTransformException(String arg0) {
        super(arg0);
    }

}
