package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ErrorPeriodSearchModel;
import ch.globaz.al.business.models.rafam.OverlapInformationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService;
import ch.globaz.al.businessimpl.checker.model.rafam.ErreurAnnonceRafamModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistence des annonces RAFAM
 * 
 * @author jts
 * 
 */
public class ErreurAnnonceRafamModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        ErreurAnnonceRafamModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService#count(ch.globaz.al.business.models
     * .rafam.ErreurAnnonceRafamSearchModel)
     */
    @Override
    public int count(ErreurAnnonceRafamSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALAnnonceRafamException("ErreurErreurAnnonceRafamModelServiceImpl#count : search is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService#create(ch.globaz.al.business.models
     * .rafam.ErreurAnnonceRafamModel)
     */
    @Override
    public ErreurAnnonceRafamModel create(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALDossierModelException(
                    "Unable to add model (ErreurAnnonceRafamModel) - the model passed is null!");
        }

        ErreurAnnonceRafamModelChecker.validate(model);
        return (ErreurAnnonceRafamModel) JadePersistenceManager.add(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService#delete(ch.globaz.al.business.models
     * .rafam.ErreurAnnonceRafamModel)
     */
    @Override
    public ErreurAnnonceRafamModel delete(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException(
                    "ErreurErreurAnnonceRafamModelServiceImpl#delete : Unable to delete ErreurAnnoncesRafam the model passed is null");
        }
        if (model.isNew()) {
            throw new ALAnnonceRafamException(
                    "ErreurErreurAnnonceRafamModelServiceImpl#delete : Unable to delete ErreurAnnoncesRafam, the id passed is new");
        }

        ErrorPeriodSearchModel periodSearch = new ErrorPeriodSearchModel();
        periodSearch.setForIdErreurAnnonce(model.getIdAnnonce());
        JadePersistenceManager.delete(periodSearch);

        OverlapInformationSearchModel overlapSearch = new OverlapInformationSearchModel();
        overlapSearch.setForIdErreurAnnonce(model.getIdAnnonce());
        JadePersistenceManager.delete(overlapSearch);

        ErreurAnnonceRafamModelChecker.validateForDelete(model);

        return (ErreurAnnonceRafamModel) JadePersistenceManager.delete(model);
    }

    @Override
    public void deleteForIdAnnonce(String idAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "ErreurErreurAnnonceRafamModelServiceImpl#deleteForIdAnnonce : idAnnonce is not an integer");
        }

        ErreurAnnonceRafamSearchModel erreurSearch = new ErreurAnnonceRafamSearchModel();
        erreurSearch.setForIdAnnonce(idAnnonce);
        erreurSearch = ALServiceLocator.getErreurAnnonceRafamModelService().search(erreurSearch);

        for (int i = 0; i < erreurSearch.getSize(); i++) {
            ALServiceLocator.getErreurAnnonceRafamModelService().delete(
                    (ErreurAnnonceRafamModel) erreurSearch.getSearchResults()[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService#read(java.lang.String)
     */
    @Override
    public ErreurAnnonceRafamModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "ErreurErreurAnnonceRafamModelServiceImpl#read : idAnnonce is not an integer");
        }

        ErreurAnnonceRafamModel model = new ErreurAnnonceRafamModel();
        model.setId(idAnnonce);
        return (ErreurAnnonceRafamModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService#search(ch.globaz.al.business.models
     * .rafam.ErreurAnnonceRafamSearchModel)
     */
    @Override
    public ErreurAnnonceRafamSearchModel search(ErreurAnnonceRafamSearchModel search) throws JadeApplicationException,
            JadePersistenceException {

        if (search == null) {
            throw new ALAnnonceRafamException("ErreurErreurAnnonceRafamModelServiceImpl#search : search is null");
        }

        return (ErreurAnnonceRafamSearchModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.ErreurAnnonceRafamModelService#update(ch.globaz.al.business.models
     * .rafam.ErreurAnnonceRafamModel)
     */
    @Override
    public ErreurAnnonceRafamModel update(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException("ErreurAnnonceRafamModelServiceImpl#update : model is null");
        }

        if (model.isNew()) {
            throw new ALAnnonceRafamException("ErreurAnnonceRafamModelServiceImpl#update : model is new");

        }

        ErreurAnnonceRafamModelChecker.validate(model);
        return (ErreurAnnonceRafamModel) JadePersistenceManager.update(model);
    }
}