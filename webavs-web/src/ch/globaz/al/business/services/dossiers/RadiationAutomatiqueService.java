package ch.globaz.al.business.services.dossiers;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexModel;
import ch.globaz.al.business.models.prestation.radiation.PrestationRadiationDossierComplexSearchModel;
import ch.globaz.al.businessimpl.services.dossiers.RadiationAutomatiqueResult;

/**
 * Service permettant la gestion de la radiation automatique de dossiers
 * 
 * @author jts
 * 
 */
public interface RadiationAutomatiqueService extends JadeApplicationService {

    /**
     * Charge la liste une liste contenant les informations de la dernière prestation de chaque dossier actif
     * 
     * @return Liste des prestations
     * @throws Exception
     */
    public PrestationRadiationDossierComplexSearchModel loadLastPrestations(String periode) throws Exception;

    /**
     * Radie le dossier en paramètre. Après radiation le dossier est journalisé
     * 
     * @param prest
     * @return le dossier radié
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public RadiationAutomatiqueResult radierDossier(PrestationRadiationDossierComplexModel prest)
            throws JadeApplicationException, JadePersistenceException;
}
