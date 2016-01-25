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
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            mod�le de recherche de dossier
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierAttestationVersementComplexSearchModel search(
            DossierAttestationVersementComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
