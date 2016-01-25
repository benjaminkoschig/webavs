package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;

/**
 * 
 * @author pta
 * 
 */

public interface DossierAttestationComplexModelService extends JadeApplicationService {

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            modèle de recherche de dossier
     * @return Résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierAttestationVersementComplexSearchModel search(
            DossierAttestationVersementComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
