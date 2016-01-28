package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexSearchModel;
import ch.globaz.al.business.services.models.tarif.CategorieTarifComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de persistance des données de
 * {@link ch.globaz.al.business.models.tarif.CategorieTarifComplexModel}
 * 
 * @author jts
 */
public class CategorieTarifComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        CategorieTarifComplexModelService {

    @Override
    public CategorieTarifComplexSearchModel search(CategorieTarifComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (searchModel == null) {
            throw new ALCategorieTarifModelException(
                    "CategorieTarifComplexModelServiceImpl#search : searchModel is null");
        }

        return (CategorieTarifComplexSearchModel) JadePersistenceManager.search(searchModel);
    }
}
