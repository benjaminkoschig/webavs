package globaz.itucana.exception;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe générique remontant toutes les erreurs remontées par l'interface
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public abstract class TUInterfaceException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Throwable subException = null;

    public TUInterfaceException() {
        super();
    }

    public TUInterfaceException(String message) {
        super(message);
    }

    public TUInterfaceException(String message, Throwable subException) {
        super(message + " : " + subException.toString());
        this.subException = subException;
    }

    public TUInterfaceException(Throwable subException) {
        super();
        this.subException = subException;
    }

    /**
     * Permet de récupérer l'exception "throwable"
     * 
     * @return
     */
    public Throwable getSubException() {
        return subException;
    }

    @Override
    public String toString() {
        StringBuffer strBuffer = new StringBuffer();

        if (!JadeStringUtil.isEmpty(getMessage())) {
            strBuffer.append("\n ******************************************************");
            strBuffer.append("\n Erreur générale sur l'interface");
            strBuffer.append("\n Exception : ").append(this.getClass().getName());
            strBuffer.append("\n Message : ").append(getMessage());
        }
        if (subException != null) {
            strBuffer.append("\n ******************************************************");
            strBuffer.append("\n détail");
            if (subException.getMessage() != null || !JadeStringUtil.isEmpty(subException.getMessage())) {
                strBuffer.append("\n description : ").append(subException.getMessage());
            }
            if (subException.toString() != null || !JadeStringUtil.isEmpty(subException.toString())) {
                strBuffer.append("\n erreur : ").append(subException.toString());
            }
        }
        return strBuffer.toString();
    }

}
