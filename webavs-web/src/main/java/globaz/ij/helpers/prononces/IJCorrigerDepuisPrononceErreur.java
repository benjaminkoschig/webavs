package globaz.ij.helpers.prononces;

import globaz.ij.helpers.prononces.IJCorrigerDepuisPrononce.CorrigerPrononceErreur;
import java.util.List;

/**
 * 
 * @author rco Crée le 25.09.2013 Modifié le 30.09.2013
 * 
 */
public class IJCorrigerDepuisPrononceErreur {

    /**
     * 
     * @param erreur
     * @param message
     * @return
     */
    public static String composerMessageErreur(CorrigerPrononceErreur erreur, String message) {
        List<String> listeErreur = erreur.getAndClearListeDateMsgErreur();

        message = message.replace("{0}", listeErreur.get(0));
        message = message.replace("{1}", listeErreur.get(1));
        message = message.replace("{2}", listeErreur.get(2));
        return message;
    }
}
