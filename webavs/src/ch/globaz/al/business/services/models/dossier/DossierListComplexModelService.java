package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexSearchModel;

/**
 * Interface de déclaration des services de DossierDetailComplexModel
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.dossier.DossierListComplexModel
 * @see ch.globaz.al.business.models.dossier.DossierListComplexSearchModel
 */
public interface DossierListComplexModelService extends JadeApplicationService {

    /**
     * Charge le dossier correspondant à l'id passé en paramètre
     * 
     * @param idDossierListModel
     *            id du dossier à charger
     * @return Le dossier chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierListComplexModel read(String idDossierListModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les dossiers correspondant aux critères contenus dans le modèle de recherche passé en paramètre
     * 
     * @param dossierListComplexSearchModel
     *            modèle de recherche
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierListComplexSearchModel search(DossierListComplexSearchModel dossierListComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;
}
