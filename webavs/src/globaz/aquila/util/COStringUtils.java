package globaz.aquila.util;

import java.text.MessageFormat;

/**
 * <h1>Description</h1>
 * <p>
 * Encore une classe de gestion spécifique des chaînes.
 * </p>
 * 
 * @author vre
 */
public class COStringUtils {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes.
     * </p>
     * <p>
     * En attendant qu'elle soit dans le framework.
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }

        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

}
