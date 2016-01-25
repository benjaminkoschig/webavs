package ch.globaz.al.business.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;

/**
 * Services liés à la persistance des annonces RAFAM
 * 
 * @author jts
 */
public interface AnnonceRafamErrorComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param search
     *            Modèle de recherche contenant les critères
     * @return nombre de copies correspondant aux critères de recherche
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public int count(AnnonceRafamErrorComplexSearchModel search) throws JadePersistenceException,
            JadeApplicationException;

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
    public AnnonceRafamErrorComplexSearchModel search(AnnonceRafamErrorComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException;
}
