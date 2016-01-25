package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service lié à la gestion de la persistance du modèle complexe d'annonce RAFAm
 * 
 * @author jts
 * 
 */
public class AnnonceRafamComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.annonces.rafam. AnnonceRafamComplexModelService#read(java.lang.String)
     */
    @Override
    public AnnonceRafamComplexModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException("AnnonceRafamComplexModel#read : idAnnonce is not an integer");
        }

        AnnonceRafamComplexModel model = new AnnonceRafamComplexModel();
        model.setId(idAnnonce);
        return (AnnonceRafamComplexModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.annonces.rafam. AnnonceRafamComplexModelService
     * #search(ch.globaz.al.business.models.annoncesRafam .AnnonceRafamComplexSearchModel)
     */
    @Override
    public AnnonceRafamComplexSearchModel search(AnnonceRafamComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new ALAnnonceRafamException("AnnonceRafamComplexModel#search : search is null");
        }

        return (AnnonceRafamComplexSearchModel) JadePersistenceManager.search(search);
    }
}
