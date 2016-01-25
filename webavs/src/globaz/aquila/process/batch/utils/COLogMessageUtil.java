package globaz.aquila.process.batch.utils;

import java.text.MessageFormat;

public class COLogMessageUtil {

    /**
     * remplace dans message {n} par args[n].
     * 
     * @param message
     *            DOCUMENT ME!
     * @param args
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @see MessageFormat
     */
    public static String formatMessage(StringBuffer message, Object[] args) {
        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar) == '\'')
                    && ((idChar == message.length() - 1) || (message.charAt(idChar + 1) != '\''))) {
                message.insert(idChar, '\'');
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
        return MessageFormat.format(message.toString(), args);
    }

}
