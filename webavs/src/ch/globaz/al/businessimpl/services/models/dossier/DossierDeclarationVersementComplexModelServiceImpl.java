package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.exceptions.model.prestation.ALDeclarationVersementModelException;
import ch.globaz.al.business.models.dossier.DossierDeclarationVersementComplexSearchModel;
import ch.globaz.al.business.services.models.dossier.DossierDeclarationVersementComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation des services liés à DossierDeclarationComplexModel
 * 
 * @author PTA
 * 
 */
public class DossierDeclarationVersementComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DossierDeclarationVersementComplexModelService {

    @Override
    public DossierDeclarationVersementComplexSearchModel search(
            DossierDeclarationVersementComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALDeclarationVersementModelException(
                    "DossierDeclarationVersementComplexModelServiceImpl#search : searchModel is null");
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return (DossierDeclarationVersementComplexSearchModel) JadePersistenceManager.search(searchModel);
    }

}
