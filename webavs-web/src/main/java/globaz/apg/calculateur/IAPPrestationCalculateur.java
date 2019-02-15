package globaz.apg.calculateur;

import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import java.util.List;

public interface IAPPrestationCalculateur<E, T, V> {
    
    /**
     * Reçoit une liste d'object de domaine contenant toutes les infos utiles au calcul des prestations
     * 
     * @param calculateurPrestationData
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    // FIXME : Mouais, Object n'est pas top... Il doit être possible de typer, donc de factoriser les données en entrées
    // des différents calculateurs
    public List<E> calculerPrestation(List<T> donneesDomainCalcul) throws Exception;

    /**
     * Reçoit un POJO contenant l'ensemble des entités récupérées depuis la persistence en entrées pour créer une liste
     * d'objects de domaine pour le calcul
     * 
     * @param data
     * @param session
     * @return Object (->calculateurPrestationData)
     * @throws Exception
     */
    public List<T> persistenceToDomain(V donneesDepuisPersistance) throws Exception;

    /**
     * Convertis une liste d'objets de domain (les résultats du calculateur) vers un liste d'objects de persistence
     * 
     * @param resultat
     * @param session
     * @param transaction
     * @return Object (->Object calculateurPrestationData)
     * @throws Exception
     */
    public List<APPrestationCalculeeAPersister> domainToPersistence(List<E> resultat) throws Exception;

}
