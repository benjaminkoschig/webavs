package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.rafam.ErrorPeriodModel;
import ch.globaz.al.business.models.rafam.ErrorPeriodSearchModel;
import ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService;
import ch.globaz.al.businessimpl.checker.model.rafam.ErrorPeriodModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistence des annonces RAFAM
 * 
 * @author jts
 * 
 */
public class ErrorPeriodModelServiceImpl extends ALAbstractBusinessServiceImpl implements ErrorPeriodModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService#count(ch.globaz.al.business.models
     * .rafam.ErrorPeriodSearchModel)
     */
    @Override
    public int count(ErrorPeriodSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALAnnonceRafamException("ErreurErrorPeriodModelServiceImpl#count : search is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService#create(ch.globaz.al.business.models
     * .rafam.ErrorPeriodModel)
     */
    @Override
    public ErrorPeriodModel create(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALDossierModelException("Unable to add model (ErrorPeriodModel) - the model passed is null!");
        }

        ErrorPeriodModelChecker.validate(model);
        return (ErrorPeriodModel) JadePersistenceManager.add(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService#delete(ch.globaz.al.business.models
     * .rafam.ErrorPeriodModel)
     */
    @Override
    public ErrorPeriodModel delete(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException(
                    "ErreurErrorPeriodModelServiceImpl#delete : Unable to delete ErreurAnnoncesRafam the model passed is null");
        }
        if (model.isNew()) {
            throw new ALAnnonceRafamException(
                    "ErreurErrorPeriodModelServiceImpl#delete : Unable to delete ErreurAnnoncesRafam, the id passed is new");
        }

        ErrorPeriodModelChecker.validateForDelete(model);

        return (ErrorPeriodModel) JadePersistenceManager.delete(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService#read(java.lang.String)
     */
    @Override
    public ErrorPeriodModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException("ErreurErrorPeriodModelServiceImpl#read : idAnnonce is not an integer");
        }

        ErrorPeriodModel model = new ErrorPeriodModel();
        model.setId(idAnnonce);
        return (ErrorPeriodModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService#search(ch.globaz.al.business.models
     * .rafam.ErrorPeriodSearchModel)
     */
    @Override
    public ErrorPeriodSearchModel search(ErrorPeriodSearchModel search) throws JadeApplicationException,
            JadePersistenceException {

        if (search == null) {
            throw new ALAnnonceRafamException("ErreurErrorPeriodModelServiceImpl#search : search is null");
        }

        return (ErrorPeriodSearchModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.ErrorPeriodModelService#update(ch.globaz.al.business.models
     * .rafam.ErrorPeriodModel)
     */
    @Override
    public ErrorPeriodModel update(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException("ErrorPeriodModelServiceImpl#update : model is null");
        }

        if (model.isNew()) {
            throw new ALAnnonceRafamException("ErrorPeriodModelServiceImpl#update : model is new");

        }

        ErrorPeriodModelChecker.validate(model);
        return (ErrorPeriodModel) JadePersistenceManager.update(model);
    }
}