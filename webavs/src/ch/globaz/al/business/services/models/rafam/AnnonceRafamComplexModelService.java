package ch.globaz.al.business.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;

/**
 * Service lié à la gestion de la persistance du modèle complexe d'annonce RAFAm
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamComplexModelService extends JadeApplicationService {

    /**
     * Récupère les données de l'annonce correspondant à <code>idAnnonce</code>
     * 
     * @param idAnnonce
     *            Id de l'annonce à charger
     * @return Annonce chargée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamComplexModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les annonces correspondant aux critère contenus dans le modèle de recherche passé en paramètre
     * 
     * @param search
     *            modèle contenant les critères de recherche
     * @return résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamComplexSearchModel search(AnnonceRafamComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException;
}
