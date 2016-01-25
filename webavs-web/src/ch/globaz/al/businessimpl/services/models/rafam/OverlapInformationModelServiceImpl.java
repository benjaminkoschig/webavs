package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.rafam.OverlapInformationModel;
import ch.globaz.al.business.models.rafam.OverlapInformationSearchModel;
import ch.globaz.al.business.services.models.rafam.OverlapInformationModelService;
import ch.globaz.al.businessimpl.checker.model.rafam.OverlapInformationModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistence des annonces RAFAM
 * 
 * @author jts
 * 
 */
public class OverlapInformationModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        OverlapInformationModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.OverlapInformationModelService#count(ch.globaz.al.business.models
     * .rafam.OverlapInformationSearchModel)
     */
    @Override
    public int count(OverlapInformationSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALAnnonceRafamException("ErreurOverlapInformationModelServiceImpl#count : search is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.OverlapInformationModelService#create(ch.globaz.al.business.models
     * .rafam.OverlapInformationModel)
     */
    @Override
    public OverlapInformationModel create(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALDossierModelException(
                    "Unable to add model (OverlapInformationModel) - the model passed is null!");
        }

        OverlapInformationModelChecker.validate(model);
        return (OverlapInformationModel) JadePersistenceManager.add(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.OverlapInformationModelService#delete(ch.globaz.al.business.models
     * .rafam.OverlapInformationModel)
     */
    @Override
    public OverlapInformationModel delete(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException(
                    "ErreurOverlapInformationModelServiceImpl#delete : Unable to delete ErreurAnnoncesRafam the model passed is null");
        }
        if (model.isNew()) {
            throw new ALAnnonceRafamException(
                    "ErreurOverlapInformationModelServiceImpl#delete : Unable to delete ErreurAnnoncesRafam, the id passed is new");
        }

        OverlapInformationModelChecker.validateForDelete(model);

        return (OverlapInformationModel) JadePersistenceManager.delete(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.OverlapInformationModelService#read(java.lang.String)
     */
    @Override
    public OverlapInformationModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "ErreurOverlapInformationModelServiceImpl#read : idAnnonce is not an integer");
        }

        OverlapInformationModel model = new OverlapInformationModel();
        model.setId(idAnnonce);
        return (OverlapInformationModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.OverlapInformationModelService#search(ch.globaz.al.business.models
     * .rafam.OverlapInformationSearchModel)
     */
    @Override
    public OverlapInformationSearchModel search(OverlapInformationSearchModel search) throws JadeApplicationException,
            JadePersistenceException {

        if (search == null) {
            throw new ALAnnonceRafamException("ErreurOverlapInformationModelServiceImpl#search : search is null");
        }

        return (OverlapInformationSearchModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.rafam.OverlapInformationModelService#update(ch.globaz.al.business.models
     * .rafam.OverlapInformationModel)
     */
    @Override
    public OverlapInformationModel update(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException("OverlapInformationModelServiceImpl#update : model is null");
        }

        if (model.isNew()) {
            throw new ALAnnonceRafamException("OverlapInformationModelServiceImpl#update : model is new");

        }

        OverlapInformationModelChecker.validate(model);
        return (OverlapInformationModel) JadePersistenceManager.update(model);
    }
}