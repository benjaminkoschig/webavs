/*
 * Cr�� le 6 f�vr. 07
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hermes.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author ald Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class HEBatchUtils {
    public static String getCurrentTime() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()) + " "
                + DateFormat.getTimeInstance(DateFormat.DEFAULT).format(new Date()) + ":";
    }
}
