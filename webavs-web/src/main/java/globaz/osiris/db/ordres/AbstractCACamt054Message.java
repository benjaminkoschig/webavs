package globaz.osiris.db.ordres;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class AbstractCACamt054Message<T> {

    protected static final String RETURN_LINE = "\r\n ";

    protected String enteteLevel = null;
    protected Map<Level, List<String>> messages = null;

    public AbstractCACamt054Message(final T object) {
        messages = new HashMap<Level, List<String>>();
        enteteLevel = getFormattedEnteteLevel(object);
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
     * Savoir si on est en erreur (selon la méthodologie de la classe d'implémentation).
     * 
     * @return True si en erreur, sinon False.
     */
    public abstract boolean isOnError();

    /**
     * Obtenir un message parlant pour l'utilisateur selon le degré des messages et de l'entête. (selon la méthodologie
     * de la classe d'implémentation)
     * 
     * @return Une chaîne de caractères (ne retourne jamais null).
     */
    public abstract String getFormattedMessages();

    /**
     * Obtenir un message d'entête formaté (selon la méthodologie de la classe d'implémentation)
     * 
     * @param object Un objet typé.
     * @return Une chaîne de caractères (ne retourne jamais null).
     */
    public abstract String getFormattedEnteteLevel(final T object);
}
