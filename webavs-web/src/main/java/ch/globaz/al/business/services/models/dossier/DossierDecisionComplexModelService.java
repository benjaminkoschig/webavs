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
     * R�cup�re les donn�es du dossier correspondant � <code>idDossier</code>
     * 
     * @param idDossier
     *            Id du dossier � charger
     * @return Le mod�le du dossier charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierDecisionComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException;

}
