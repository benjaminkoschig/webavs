package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.exception.JadeApplicationException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * Classe conteneur permettant la gestion des annonces des employeurs d�l�gu�s
 * 
 * @author jts
 * 
 */
public class MessagesEmployeursDeleguesContainer {

    /** instance unique de la classe */
    private static MessagesEmployeursDeleguesContainer instance = null;

    static {
        MessagesEmployeursDeleguesContainer.instance = new MessagesEmployeursDeleguesContainer();
    }

    /**
     * 
     * @return Retourne une instance de la classe
     */
    public static MessagesEmployeursDeleguesContainer getInstance() {
        return MessagesEmployeursDeleguesContainer.instance;
    }

    /** Map devant contenir les annonces */
    Map<Integer, ArrayList<Object>> mapAnnoncesEmployeursDelegue = new TreeMap<Integer, ArrayList<Object>>();

    /**
     * Constructeur priv� (utiliser la m�thode getInstance())
     */
    private MessagesEmployeursDeleguesContainer() {
    }

    /**
     * Ajoute une annonce dans le conteneur
     * 
     * @param annonce
     *            l'annonce � ajouter
     * @param numEmployeurDelegue
     *            num�ro de l'employeur d�l�gu�
     */
    public void addAnnonce(Object annonce, int numEmployeurDelegue) {

        if (!mapAnnoncesEmployeursDelegue.containsKey(numEmployeurDelegue)) {
            mapAnnoncesEmployeursDelegue.put(numEmployeurDelegue, new ArrayList<Object>());
        }

        mapAnnoncesEmployeursDelegue.get(numEmployeurDelegue).add(annonce);
    }

    /**
     * Vide la liste des annonces
     */
    public void clear() {
        mapAnnoncesEmployeursDelegue.clear();
    }

    /**
     * R�cup�re la liste des annonces du conteneur. Les annonces sont regroup�es par employeur d�l�gu�
     * 
     * @return La liste des annonces du conteneur
     */
    public Map<Integer, ArrayList<Object>> getMapAnnoncesEmployeursDelegue() {
        return mapAnnoncesEmployeursDelegue;
    }

    /**
     * R�cup�re le num�ro d'employeur d�l�gu� contenu dans le record number
     * 
     * @param recordNumber
     * @return
     * @throws JadeApplicationException
     *             Exception lev�e si le record number ne correspond pas � un employeur d�l�gu�
     */
    public int getNoEmployeurDelegue(String recordNumber) throws JadeApplicationException {

        if (recordNumber.length() == 16) {
            return Integer.parseInt(recordNumber.substring(0, 2));
        } else {
            throw new ALRafamException("MessagesEmployeursDeleguesContainer#getNoEmployeurDelegue : Le record number "
                    + recordNumber + " ne correspond pas � un employeur d�l�gu�");
        }
    }
}
