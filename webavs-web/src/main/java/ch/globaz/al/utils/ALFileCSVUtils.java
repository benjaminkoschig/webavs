package ch.globaz.al.utils;

import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import java.io.FileOutputStream;
import java.io.IOException;
import ch.globaz.al.business.exceptions.utils.ALUtilsException;

/**
 * classe fournissant des méthodes de création d'un fichier CSV
 * 
 * @author PTA
 * 
 */
public class ALFileCSVUtils {
    /**
     * méthode de création d'un fichier CSV
     * 
     * @param cleIdRecap identifiant de la récap
     * @param nomFichier nom du fichier
     * @param valeur contenu du fichier
     * @throws JadeApplicationException
     * 
     *             Exception levée en cas de problème lors de l'écriture du fichier
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
     * Méthode qui retourne le nom du fichier
     * 
     * @param idRecap identifiant de la récap
     * @return String correspond au nom du fichier
     */
    public static String nomFichier(Object idRecap) {
        String cheminPersistence = Jade.getInstance().getPersistenceDir();
        StringBuffer sauvegardeTxt = new StringBuffer().append(cheminPersistence).append(idRecap).append(".csv");

        return sauvegardeTxt.toString();
    }

}
