package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;

/**
 * Services liés à la persistance des dossier
 * 
 * @author PTA
 */
public interface DossierFkModelService extends JadeApplicationService {

    /**
     * Retourne le nombre de dossier trouvé par le modèle de recherche
     * 
     * @param dossierFkSearch
     *            modèle de recherche de dossier
     * @return nombre de dossiers correspondant aux critères de recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(DossierFkSearchModel dossierFkSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Effectue la recherche selon le modèle de recherche passé en paramètre
     * 
     * @param dossierFkSearch
     *            modèle de recherche de dossier
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierFkSearchModel search(DossierFkSearchModel dossierFkSearch) throws JadeApplicationException,
            JadePersistenceException;

}
