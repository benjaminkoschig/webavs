package globaz.tucana.process.message;

/**
 * Représentation d'un message process
 * 
 * @author fgo
 * 
 */
public class TUMessage {
    private String message = null;
    private String origine = null;
    private String type = null;

    /**
     * Constructeur
     */
    public TUMessage(String _message, String _typeMessage, String _origine) {
        message = _message;
        type = _typeMessage;
        origine = _origine;
    }

    /**
     * Récupère le message
     * 
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Récupère l'origine
     * 
     * @return
     */
    public String getOrigine() {
        return origine;
    }

    /**
     * Récupère le type de message
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ################################################ \n");
        str.append("\n Détail message");
        str.append("\n ------------------------------------------------ \n");
        str.append("\n Origine : ").append(origine);
        str.append("\n Message : ").append(message);
        str.append("\n Type : ").append(type);
        str.append("\n ------------------------------------------------ \n");
        return str.toString();
    }

}
