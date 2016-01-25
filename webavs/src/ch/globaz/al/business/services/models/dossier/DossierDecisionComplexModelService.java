package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;

/**
 * Service de gestion de la persistance d'un dossier decision complex
 * 
 * @author PTA
 * 
 */
public interface DossierDecisionComplexModelService extends JadeApplicationService {

    /**
     * Récupère les données du dossier correspondant à <code>idDossier</code>
     * 
     * @param idDossier
     *            Id du dossier à charger
     * @return Le modèle du dossier chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierDecisionComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException;

}
