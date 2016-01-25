package ch.globaz.al.utils;

import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import java.io.FileOutputStream;
import java.io.IOException;
import ch.globaz.al.business.exceptions.utils.ALUtilsException;

/**
 * classe fournissant des m�thodes de cr�ation d'un fichier CSV
 * 
 * @author PTA
 * 
 */
public class ALFileCSVUtils {
    /**
     * m�thode de cr�ation d'un fichier CSV
     * 
     * @param cleIdRecap identifiant de la r�cap
     * @param nomFichier nom du fichier
     * @param valeur contenu du fichier
     * @throws JadeApplicationException
     * 
     *             Exception lev�e en cas de probl�me lors de l'�criture du fichier
     */
    public static void createFichier(String nomFichier, Object cleIdRecap, Object valeur)
            throws JadeApplicationException {

        try {
            FileOutputStream fichier = new FileOutputStream(nomFichier);
            fichier.write(valeur.toString().getBytes());
            fichier.flush();
            fichier.close();

        } catch (IOException e) {
            throw new ALUtilsException("ALFileCSVUtils#createFichier : unable to create file", e);
        }

    }

    /**
     * M�thode qui retourne le nom du fichier
     * 
     * @param idRecap identifiant de la r�cap
     * @return String correspond au nom du fichier
     */
    public static String nomFichier(Object idRecap) {
        String cheminPersistence = Jade.getInstance().getPersistenceDir();
        StringBuffer sauvegardeTxt = new StringBuffer().append(cheminPersistence).append(idRecap).append(".csv");

        return sauvegardeTxt.toString();
    }

}
