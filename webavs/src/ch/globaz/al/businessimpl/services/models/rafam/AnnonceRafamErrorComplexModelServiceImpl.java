package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamErrorComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistence des annonces RAFAM
 * 
 * @author jts
 * 
 */
public class AnnonceRafamErrorComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamErrorComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService #
     * count(ch.globaz.al.business.models.rafam.AnnoncesRafamSearchModel )
     */
    @Override
    public int count(AnnonceRafamErrorComplexSearchModel search) throws JadePersistenceException,
            JadeApplicationException {

        if (search == null) {
            throw new ALAnnonceRafamException("AnnonceRafamErrorComplexModelServiceImpl#count : search is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService #
     * search(ch.globaz.al.business.models.rafam.AnnoncesRafamSearchModel )
     */
    @Override
    public AnnonceRafamErrorComplexSearchModel search(AnnonceRafamErrorComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException {

        if (search == null) {
            throw new ALAnnonceRafamException("AnnonceRafamErrorComplexModelServiceImpl#search : search is null");
        }

        return (AnnonceRafamErrorComplexSearchModel) JadePersistenceManager.search(search);
    }
}
