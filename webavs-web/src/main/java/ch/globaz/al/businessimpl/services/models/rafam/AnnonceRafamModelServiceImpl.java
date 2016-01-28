package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamModelService;
import ch.globaz.al.businessimpl.checker.model.rafam.AnnonceRafamModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistence des annonces RAFAM
 * 
 * @author jts
 * 
 */
public class AnnonceRafamModelServiceImpl extends ALAbstractBusinessServiceImpl implements AnnonceRafamModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService #
     * count(ch.globaz.al.business.models.rafam.AnnoncesRafamSearchModel )
     */
    @Override
    public int count(AnnonceRafamSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALAnnonceRafamException("AnnonceRafamModelServiceImpl#count : search is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService
     * #create(ch.globaz.al.business.models.rafam.AnnoncesRafamModel)
     */
    @Override
    public AnnonceRafamModel create(AnnonceRafamModel model) throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALDossierModelException("Unable to add model (dossierModel) - the model passed is null!");
        }

        AnnonceRafamModelChecker.validate(model);

        if (JadeNumericUtil.isEmptyOrZero(model.getEcheanceDroit())
                || JadeDateUtil.isDateBefore(ALConstRafam.DATE_FIN_MINIMUM, model.getEcheanceDroit())
                || (RafamEvDeclencheur.RADIATION.getCS().equals(model.getEvDeclencheur()) && !RafamTypeAnnonce
                        .getRafamTypeAnnonce(model.getTypeAnnonce()).equals(RafamTypeAnnonce._68A_CREATION))) {

            model = (AnnonceRafamModel) JadePersistenceManager.add(model);

            if (!model.isNew() && JadeNumericUtil.isEmptyOrZero(model.getRecordNumber())) {
                model.setRecordNumber(model.getId());
                model = (AnnonceRafamModel) JadePersistenceManager.update(model);
            }
        }

        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService
     * #delete(ch.globaz.al.business.models.rafam.AnnoncesRafamModel)
     */
    @Override
    public AnnonceRafamModel delete(AnnonceRafamModel model) throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamModelServiceImpl#delete : Unable to delete annoncesRafam the model passed is null");
        }
        if (model.isNew()) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamModelServiceImpl#delete : Unable to delete annoncesRafam, the id passed is new");
        }

        ALServiceLocator.getErreurAnnonceRafamModelService().deleteForIdAnnonce(model.getIdAnnonce());

        AnnonceRafamModelChecker.validateForDelete(model);

        return (AnnonceRafamModel) JadePersistenceManager.delete(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService #read(java.lang.String)
     */
    @Override
    public AnnonceRafamModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException("AnnonceRafamModelServiceImpl#read : idAnnonce is not an integer");
        }

        AnnonceRafamModel model = new AnnonceRafamModel();
        model.setId(idAnnonce);
        return (AnnonceRafamModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService #
     * search(ch.globaz.al.business.models.rafam.AnnoncesRafamSearchModel )
     */
    @Override
    public AnnonceRafamSearchModel search(AnnonceRafamSearchModel search) throws JadeApplicationException,
            JadePersistenceException {

        if (search == null) {
            throw new ALAnnonceRafamException("AnnonceRafamModelServiceImpl#search : search is null");
        }

        return (AnnonceRafamSearchModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.rafam.AnnoncesRafamModelService
     * #update(ch.globaz.al.business.models.rafam.AnnoncesRafamModel)
     */
    @Override
    public AnnonceRafamModel update(AnnonceRafamModel model) throws JadeApplicationException, JadePersistenceException {

        if (model == null) {
            throw new ALAnnonceRafamException("AnnonceRafamModelServiceImpl#update : model is null");
        }

        if (model.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamModelServiceImpl#update : model is new");

        }

        AnnonceRafamModelChecker.validate(model);
        return (AnnonceRafamModel) JadePersistenceManager.update(model);
    }

}
