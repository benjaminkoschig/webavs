package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.exception.JadeApplicationException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * Classe conteneur permettant la gestion des annonces des employeurs délégués
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
     * Constructeur privé (utiliser la méthode getInstance())
     */
    private MessagesEmployeursDeleguesContainer() {
    }

    /**
     * Ajoute une annonce dans le conteneur
     * 
     * @param annonce
     *            l'annonce à ajouter
     * @param numEmployeurDelegue
     *            numéro de l'employeur délégué
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
     * Récupère la liste des annonces du conteneur. Les annonces sont regroupées par employeur délégué
     * 
     * @return La liste des annonces du conteneur
     */
    public Map<Integer, ArrayList<Object>> getMapAnnoncesEmployeursDelegue() {
        return mapAnnoncesEmployeursDelegue;
    }

    /**
     * Récupère le numéro d'employeur délégué contenu dans le record number
     * 
     * @param recordNumber
     * @return
     * @throws JadeApplicationException
     *             Exception levée si le record number ne correspond pas à un employeur délégué
     */
    public int getNoEmployeurDelegue(String recordNumber) throws JadeApplicationException {

        if (recordNumber.length() == 16) {
            return Integer.parseInt(recordNumber.substring(0, 2));
        } else {
            throw new ALRafamException("MessagesEmployeursDeleguesContainer#getNoEmployeurDelegue : Le record number "
                    + recordNumber + " ne correspond pas à un employeur délégué");
        }
    }
}
