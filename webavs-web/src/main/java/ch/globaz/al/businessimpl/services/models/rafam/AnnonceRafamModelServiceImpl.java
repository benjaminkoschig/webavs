package ch.globaz.al.businessimpl.services.models.rafam;

import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamModelService;
import ch.globaz.al.businessimpl.checker.model.rafam.AnnonceRafamModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

/**
 * Implï¿½mentation du service de gestion de la persistence des annonces RAFAM
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

        if (model.getDelegated()) {
            model = createAnnonceDelege(model);
        } else {

            AnnonceRafamModel lastAnnonce = null;
            lastAnnonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getLastActive(model.getIdDroit(),
                    RafamFamilyAllowanceType.getFamilyAllowanceType(model.getGenrePrestation()));

            String lastAnnonceErreurRecordNumber = lastAnnonce.getRecordNumber();

            if (JadeNumericUtil.isEmptyOrZero(model.getEcheanceDroit())
                    || JadeDateUtil.isDateBefore(ALConstRafam.DATE_FIN_MINIMUM, model.getEcheanceDroit())
                    || (RafamEvDeclencheur.RADIATION.getCS().equals(model.getEvDeclencheur()) && !RafamTypeAnnonce
                            .getRafamTypeAnnonce(model.getTypeAnnonce()).equals(RafamTypeAnnonce._68A_CREATION))) {

                model = (AnnonceRafamModel) JadePersistenceManager.add(model);

                if ((!model.isNew() && JadeNumericUtil.isEmptyOrZero(model.getRecordNumber()))) {
                    model.setRecordNumber(model.getId());
                    model = (AnnonceRafamModel) JadePersistenceManager.update(model);
                }
                if (lastAnnonce.getEtat() != null
                        && RafamEtatAnnonce.ARCHIVE
                                .equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(lastAnnonce.getEtat()))
                        && !lastAnnonce.getDelegated()) {
                    AnnonceRafamErrorComplexSearchModel errors = ALServiceLocator.getAnnoncesRafamErrorBusinessService()
                            .getErrorsForAnnonce(lastAnnonce.getIdAnnonce());
                    for (int i = 0; i < errors.getSize(); i++) {
                        AnnonceRafamErrorComplexModel errorRafam = (AnnonceRafamErrorComplexModel) errors
                                .getSearchResults()[i];
                        if (errorRafam.getErreurAnnonceRafamModel().getCode().equals("208")
                                || errorRafam.getErreurAnnonceRafamModel().getCode().equals("209")) {
                            model.setRecordNumber(model.getId());
                            model = (AnnonceRafamModel) JadePersistenceManager.update(model);
                            if (errorRafam.getErreurAnnonceRafamModel().getCode().equals("208")) {
                                AnnonceRafamSearchModel searchresult = ALImplServiceLocator
                                        .getAnnoncesRafamSearchService()
                                        .getAnnoncesForRecordNumber(lastAnnonceErreurRecordNumber);
                                for (int j = 0; j < searchresult.getSize(); j++) {
                                    AnnonceRafamModel annonceAArchive = (AnnonceRafamModel) searchresult
                                            .getSearchResults()[j];
                                    annonceAArchive.setEtat(RafamEtatAnnonce.ARCHIVE.getCS());
                                    annonceAArchive = (AnnonceRafamModel) JadePersistenceManager
                                            .update(annonceAArchive);
                                }

                            }
                            ALImplServiceLocator.getAnnonceRafamBusinessService()
                                    .deleteRefusees(lastAnnonce.getRecordNumber());
                            break;
                        }
                    }

                }
            }
        }

        return model;
    }

    /**
     * Nouvelle méthode créée suite à la modification de la méthode "create" pour la demande INFOROM - S190201_008
     *
     * @param model
     * @return La nouvelle annonce rafam delege
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private AnnonceRafamModel createAnnonceDelege(AnnonceRafamModel model)
            throws JadeApplicationException, JadePersistenceException {

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
    public AnnonceRafamSearchModel search(AnnonceRafamSearchModel search)
            throws JadeApplicationException, JadePersistenceException {

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
