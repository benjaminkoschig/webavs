package globaz.tucana.process.message;

import java.util.ArrayList;

/**
 * Classe container de messages
 * 
 * @author fgo
 * 
 */
public class TUMessagesContainer {
    private ArrayList message = null;

    /**
     * Constructeur
     */
    public TUMessagesContainer() {
        super();
        message = new ArrayList();
    }

    /**
     * Permet d'ajouter un message
     * 
     * @param newMessage
     * @param newTypeMessage
     * @param newOrigine
     */
    public void addMessage(String newMessage, String newTypeMessage, String newOrigine) {
        message.add(new TUMessage(newMessage, newTypeMessage, newOrigine));
    }

    /**
     * Récuèpère la liste des messages
     * 
     * @return
     */
    public ArrayList getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return message.isEmpty();
    }
}
