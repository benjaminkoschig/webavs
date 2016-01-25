package globaz.tucana.process;

import globaz.framework.util.FWMessage;
import globaz.jade.common.Jade;
import globaz.tucana.process.message.TUMessage;
import globaz.tucana.process.message.TUMessagesContainer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class TUMessageAttach {
    public static final String EXTENTION_CSV = "csv";
    public static final String EXTENTION_TXT = "txt";
    public static final String EXTENTION_XLS = "xls";

    /**
     * Permet de créer un fichier joint à partir d'un stringBuffer <b>utiliser les constantes EXTENTION de la classe<b>
     * 
     * @param strBuffer
     * @param extension
     * @return
     */
    public static String build(StringBuffer strBuffer, String extension) {
        // définition de l'emplacement du fichier et de son nom
        StringBuffer sauvegardeTxt = nomFichier(extension);
        TUMessagesContainer messages = new TUMessagesContainer();
        return creeDocAttache(messages, sauvegardeTxt, strBuffer);
    }

    /**
     * Permet de générer un fichier joint contenant des message
     * 
     * @param messages
     * @param extension
     *            l'extension du fichier <b>utiliser les constantes EXTENTION de la classe<b>
     * @return
     */
    public static String build(TUMessagesContainer messages, String extension) {
        // définition de l'emplacement du fichier et de son nom
        StringBuffer sauvegardeTxt = nomFichier(extension);

        // préparation de la lecture du container de message et du strBuffer
        // recevant le contenu du message
        StringBuffer strBuffer = new StringBuffer();
        Iterator it = messages.getMessage().iterator();
        TUMessage message = null;
        while (it.hasNext()) {
            message = (TUMessage) it.next();
            strBuffer.append(message.toString());
        }
        return creeDocAttache(messages, sauvegardeTxt, strBuffer);

    }

    private static String creeDocAttache(TUMessagesContainer messages, StringBuffer sauvegardeTxt, StringBuffer str) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(sauvegardeTxt.toString());
        } catch (FileNotFoundException e3) {
            messages.addMessage(e3.toString(), FWMessage.ERREUR, "Erreur création fichier joint");
        }
        try {
            fos.write(str.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e4) {
            messages.addMessage(e4.toString(), FWMessage.ERREUR, "Erreur création fichier joint");
        }
        return sauvegardeTxt.toString();
    }

    /**
     * Retourne le nom d'un fichier
     * 
     * @param extension
     * @return
     */
    private static StringBuffer nomFichier(String extension) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
        StringBuffer sauvegardeTxt = new StringBuffer(Jade.getInstance().getHomeDir()).append("logs/")
                .append(format.format(Calendar.getInstance().getTime())).append(".").append(extension);
        return sauvegardeTxt;
    }

}
