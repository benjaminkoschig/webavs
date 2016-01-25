package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.models.prestation.DeclarationVersementDetailleComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services <code>DeclarationVersementDetailleComplexModel</code>
 * 
 * @author PTA
 * 
 */
public class DeclarationVersementDetailleComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DeclarationVersementDetailleComplexModelService {

    @Override
    public DeclarationVersementDetailleSearchComplexModel search(
            DeclarationVersementDetailleSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {

        if (searchModel == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleComplexModelServiceImpl#search : searchModel is null");
        }

        return (DeclarationVersementDetailleSearchComplexModel) JadePersistenceManager.search(searchModel);
    }
}
