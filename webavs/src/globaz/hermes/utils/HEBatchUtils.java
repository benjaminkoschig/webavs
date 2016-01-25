/*
 * Créé le 6 févr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEBatchUtils {
    public static String getCurrentTime() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()) + " "
                + DateFormat.getTimeInstance(DateFormat.DEFAULT).format(new Date()) + ":";
    }
}
