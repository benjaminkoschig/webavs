package globaz.osiris.db.ordres.sepa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class CACamt054MessageAbstract<T> {

    protected static final String RETURN_LINE = "\r\n ";

    protected String enteteLevel = null;
    protected Map<Level, List<String>> messages = null;

    public CACamt054MessageAbstract(final T object, final String numeroReference) {
        messages = new HashMap<Level, List<String>>();
        enteteLevel = getFormattedEnteteLevel(object, numeroReference);
    }

    protected boolean checkAcceptedLevel(final Level level) {
        return Level.INFO.equals(level) || Level.WARNING.equals(level) || Level.SEVERE.equals(level);
    }

    public void addMessage(final Level level, final String message) {
        if (!checkAcceptedLevel(level)) {
            return;
        }

        final List<String> localMessages = getMessageFromLevel(level);
        localMessages.add(message);

        messages.put(level, localMessages);
    }

    public List<String> getMessageFromLevel(final Level level) {
        List<String> messagesFromLevel = new ArrayList<String>();

        if (!checkAcceptedLevel(level)) {
            return messagesFromLevel;
        }

        if (messages.containsKey(level)) {
            messagesFromLevel = messages.get(level);
        }

        return messagesFromLevel;
    }

    /**
     * Savoir si on est en erreur (selon la m�thodologie de la classe d'impl�mentation).
     * 
     * @return True si en erreur, sinon False.
     */
    public abstract boolean isOnError();

    /**
     * Obtenir un message parlant pour l'utilisateur selon le degr� des messages et de l'ent�te. (selon la m�thodologie
     * de la classe d'impl�mentation)
     * 
     * @return Une cha�ne de caract�res (ne retourne jamais null).
     */
    public abstract String getFormattedMessages();

    /**
     * Obtenir un message d'ent�te format� (selon la m�thodologie de la classe d'impl�mentation)
     * 
     * @param object Un objet typ�.
     * @param numeroReference Le num�ro de r�f�rence qui fait fois.
     * @return Une cha�ne de caract�res (ne retourne jamais null).
     */
    public abstract String getFormattedEnteteLevel(final T object, final String numeroReference);
}
