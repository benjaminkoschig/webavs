package globaz.helios.itext.list.utils;

import globaz.globall.db.BSession;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CGImpressionUtils {

    public static final String TYPE_IMPRESSION_PDF = "pdf";
    public static final String TYPE_IMPRESSION_XLS = "xls";

    /**
     * Retourne la référence à imprimer sur les "print" d'helios.
     * 
     * @param session
     * @param className
     *            La class d'impression.
     * @return
     */
    public static String getReference(BSession session, String className) {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");

        StringBuffer refBuffer = new StringBuffer(session.getLabel("REFERENCE") + " : ");
        refBuffer.append(className.substring(className.lastIndexOf('.') + 1));
        refBuffer.append(" (");
        refBuffer.append(formater.format(new Date()));
        refBuffer.append(")");
        refBuffer.append(" - ");
        refBuffer.append(session.getLabel("PRINT_USER") + " : ");
        refBuffer.append(session.getUserId());

        return refBuffer.toString();

    }
}
