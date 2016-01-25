package globaz.pegasus.utils;

import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;

public class BusinessExceptionHandler {

    public static String getErrorMessage(String idMessage, String[] parameters) {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), idMessage, parameters);
    }

    public static String getErrorMessages(String... idMessages) {

        StringBuffer result = new StringBuffer();

        for (String idMessage : idMessages) {
            if (result.length() > 0) {
                result.append("<br />");
            }
            result.append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), idMessage, null));
        }

        return result.toString();
    }

}
