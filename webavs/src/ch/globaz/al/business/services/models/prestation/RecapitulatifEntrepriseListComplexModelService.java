package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseListComplexSearchModel;

/**
 * Service de gestion de persistance des données du modèle <code>RecapitulatifEntrepriseListComplexSearchModel</code>
 * 
 * @author GMO
 * 
 */
public interface RecapitulatifEntrepriseListComplexModelService extends JadeApplicationService {

    /**
     * Recherche les récapitulatifs des entreprise correspondant aux critères définis dans le modèle de recherche
     * 
     * @param searchModel
     *            modèle de recherche selon les critères défini
     * @return le modèle de recherche contenant les résultats de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public RecapitulatifEntrepriseListComplexSearchModel search(
            RecapitulatifEntrepriseListComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;
}
