package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.services.models.dossier.DossierDecisionComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistance des données des dossiers décisions complexes
 * 
 * @author PTA
 * 
 */
public class DossierDecisionComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DossierDecisionComplexModelService {

    @Override
    public DossierDecisionComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDossier)) {
            throw new ALDossierModelException(
                    "Unable to read model (DossierDecisionComplexModel) - the id passed is not defined!");
        }
        DossierDecisionComplexModel dossierDecisionComplexModel = new DossierDecisionComplexModel();
        dossierDecisionComplexModel.setId(idDossier);

        return (DossierDecisionComplexModel) JadePersistenceManager.read(dossierDecisionComplexModel);
    }

}
