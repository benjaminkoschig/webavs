package globaz.tucana.exception.process;

import globaz.tucana.exception.TUException;

public class TUUpdateForeignKeyImportException extends TUException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUUpdateForeignKeyImportException() {
        super();
    }

    public TUUpdateForeignKeyImportException(String _message) {
        super(_message);
    }

    public TUUpdateForeignKeyImportException(String _message, String _error) {
        super(_message, _error);
    }

    public TUUpdateForeignKeyImportException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n ERREUR : Problème lors de la mise à jour des clés étrangères");
        str.append("\n Erreur : ").append(getError());
        str.append("\n Message : ").append(getMessage());
        str.append("\n ------------------------------------------------ \n");
        return str.toString();
    }

}
