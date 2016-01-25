package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierListComplexModelException;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.services.models.dossier.AffilieListComplexModelService;

/**
 * Implémentation du service de recherche des affiliés
 * 
 * @author jts
 * 
 */
public class AffilieListComplexModelServiceImpl implements AffilieListComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.AffilieModelService#search
     * (ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel)
     */
    @Override
    public AffilieListComplexSearchModel search(AffilieListComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (searchModel == null) {
            throw new ALDossierListComplexModelException("AffilieModelServiceImpl#search : searchModel is null");
        }
        return (AffilieListComplexSearchModel) JadePersistenceManager.search(searchModel);
    }
}