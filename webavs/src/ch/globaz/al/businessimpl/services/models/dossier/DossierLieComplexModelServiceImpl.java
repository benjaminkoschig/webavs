package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALLienDossierModelException;
import ch.globaz.al.business.models.dossier.DossierLieComplexSearchModel;
import ch.globaz.al.business.services.models.dossier.DossierLieComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class DossierLieComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DossierLieComplexModelService {

    @Override
    public DossierLieComplexSearchModel search(DossierLieComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new ALLienDossierModelException("DossierLieComplexModelServiceImpl#search : searchModel is null");
        }

        return (DossierLieComplexSearchModel) JadePersistenceManager.search(searchModel);
    }

}
