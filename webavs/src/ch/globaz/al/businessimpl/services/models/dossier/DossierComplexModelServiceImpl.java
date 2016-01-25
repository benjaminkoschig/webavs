package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.exceptions.business.ALDossierBusinessException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierComplexModelException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.dossier.DossierComplexModelService;
import ch.globaz.al.businessimpl.checker.model.dossier.DossierComplexModelChecker;
import ch.globaz.al.businessimpl.checker.model.dossier.DossierModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Implémentation du service de gestion de la persistance des données des dossiers complexes
 * 
 * @author jts
 */
public class DossierComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements DossierComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierComplexModelService
     * #clone(ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel clone(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        DossierComplexModel newDossierComplexModel = new DossierComplexModel();

        // même allocataire lié au dossier par défaut / unique en DB
        newDossierComplexModel.setAllocataireComplexModel(dossierComplexModel.getAllocataireComplexModel());
        // même tiers bénéficiaire lié au dossier par défaut / unique en DB
        newDossierComplexModel.setTiersBeneficiaireModel(dossierComplexModel.getTiersBeneficiaireModel());
        // clone du dossier model / nouveau enregistrement en DB (crée quand
        // dossierComplexModel.create)
        newDossierComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().clone(
                dossierComplexModel.getDossierModel()));

        return newDossierComplexModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierComplexModelService
     * #create(ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel create(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException {
        if (dossierComplexModel == null) {
            throw new ALDossierComplexModelException(
                    "Unable to add model (dossierComplexModel) - the model passed is null!");
        }

        // validation
        DossierComplexModelChecker.validate(dossierComplexModel);

        // L'allocataire lié au dossier peut déjà exister, il faut donc
        // contrôler ça avant de créer
        // et si il existe on pas besoin de update car on peut rien modifier
        // depuis cet écran
        if (dossierComplexModel.getAllocataireComplexModel().isNew()) {
            dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireComplexModelService()
                    .create(dossierComplexModel.getAllocataireComplexModel()));
        }

        dossierComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().create(
                dossierComplexModel.getDossierModel()));

        // appel pour la propagation des données de la ged
        ALServiceLocator.getGedBusinessService().propagateAllocataire(dossierComplexModel.getDossierModel());

        // try {
        //
        // LibraServiceLocator.getJournalisationService().createJournalisationWithTestDossier(
        // dossierComplexModel.getId(), "Ouverture du dossier journalisation",
        // dossierComplexModel.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
        // ILIConstantesExternes.CS_DOMAINE_AF, true);
        //
        // } catch (LibraException e) {
        // throw new ALDossierBusinessException("DossierComplexModelServiceImpl#create() : LibraException error: " + e);
        // }

        return dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierComplexModelService
     * #delete(ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel delete(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (dossierComplexModel == null) {
            throw new ALDossierComplexModelException(
                    "Unable to remove model (dossierComplexModel) - the model passed is null!");
        }
        if (dossierComplexModel.isNew()) {
            throw new ALDossierComplexModelException(
                    "Unable to remove model (dossierComplexModel) - the model passed is new!");
        }

        // Attention: ordre à respecter :
        // 1. ctrl si alloc dans autres dossiers
        // 2. tentative de suppression dossier
        // 3. si nécessaire, tentative suppression alloc

        // 1. On va chercher si l'allocataire n'a pas d'autres dossiers
        DossierFkSearchModel dossierSearch = new DossierFkSearchModel();
        dossierSearch.setForIdAllocataire(dossierComplexModel.getAllocataireComplexModel().getId());

        boolean allocUseful = false;

        if (ALImplServiceLocator.getDossierFkModelService().count(dossierSearch) > 1) {
            allocUseful = true;
        }

        // 2. validation avant suppression
        DossierModelChecker.validateForDelete(dossierComplexModel.getDossierModel());

        dossierComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().delete(
                dossierComplexModel.getDossierModel()));

        // On clôt le dossier journalisation lié au dossier supprimé

        try {

            LibraServiceLocator.getJournalisationService().createJournalisationWithTestDossier(
                    dossierComplexModel.getId(), ALConstJournalisation.DOSSIER_MOTIF_JOURNALISATION_SUPPRIMER,
                    dossierComplexModel.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                    ILIConstantesExternes.CS_DOMAINE_AF, true);

        } catch (LibraException e) {
            throw new ALDossierBusinessException("DossierComplexModelServiceImpl#create() : LibraException error: " + e);
        }
        try {

            LibraServiceLocator.getDossierService().clotureDossier(dossierComplexModel.getId());

        } catch (LibraException e) {
            throw new ALDossierComplexModelException("Unable to close libra dossier (journalisation) : ", e);
        }

        // 3. On tente de supprimer l'allocataire seulement si il n'est pas
        // utilisé
        // dans d'autres dossiers
        if (!allocUseful) {
            dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireComplexModelService()
                    .delete(dossierComplexModel.getAllocataireComplexModel()));
        }

        return dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierComplexModelService
     * #initModel(ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel initModel(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        String idDossier = dossierComplexModel.getDossierModel().getIdDossier();
        String idAllocataire = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdAllocataire();

        if (!JadeStringUtil.isEmpty(idDossier)) {
            dossierComplexModel = read(idDossier);
        }

        if (dossierComplexModel.getDossierModel().isNew()) {
            // on set l'éventuel id de l'allocataire
            dossierComplexModel.getDossierModel().setIdAllocataire(idAllocataire);

            dossierComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().initModel(
                    dossierComplexModel.getDossierModel()));
        }

        if (dossierComplexModel.getAllocataireComplexModel().isNew()) {
            // on set l'idallocataire qui a pu être lié dans l'écran al0004
            dossierComplexModel.getAllocataireComplexModel().getAllocataireModel().setIdAllocataire(idAllocataire);
            dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireComplexModelService()
                    .initModel(dossierComplexModel.getAllocataireComplexModel()));

        }

        return dossierComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierComplexModelService #read(java.lang.String)
     */
    @Override
    public DossierComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDossier)) {
            throw new ALDossierModelException(
                    "Unable to read model (DossierComplexModel) - the id passed is not defined!");
        }
        DossierComplexModel dossierComplexModel = new DossierComplexModel();
        dossierComplexModel.setId(idDossier);

        return (DossierComplexModel) JadePersistenceManager.read(dossierComplexModel);
    }

    @Override
    public DossierComplexSearchModel search(DossierComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALDossierModelException("DossierComplexModelServiceImpl#search : searchModel is null");
        }

        return (DossierComplexSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DossierComplexSearchModel searchWithCsTranslated(DossierComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new ALDossierModelException("DossierComplexModelServiceImpl#search : searchModel is null");
        }
        searchModel.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_LIES);
        DossierComplexSearchModel search = (DossierComplexSearchModel) JadePersistenceManager.search(searchModel);
        JadeAbstractModel[] resultsArray = new JadeAbstractModel[search.getSize()];
        for (int i = 0; i < search.getSize(); i++) {
            DossierComplexModel current = (DossierComplexModel) search.getSearchResults()[i];

            current.getDossierModel().setActiviteAllocataire(
                    JadeCodesSystemsUtil.getCodeLibelle(current.getDossierModel().getActiviteAllocataire()));
            current.getDossierModel().setEtatDossier(
                    JadeCodesSystemsUtil.getCodeLibelle(current.getDossierModel().getEtatDossier()));
            current.getDossierModel().setStatut(
                    JadeCodesSystemsUtil.getCodeLibelle(current.getDossierModel().getStatut()));
            resultsArray[i] = current;

        }

        search.setSearchResults(resultsArray);
        return search;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierComplexModelService
     * #update(ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel update(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (dossierComplexModel == null) {
            throw new ALDossierModelException(
                    "Unable to update model (dossierComplexModel) - the model passed is null!");
        }
        if (dossierComplexModel.isNew()) {
            throw new ALDossierModelException("Unable to update model (dossierComplexModel) - the model passed is new!");
        }

        // validation
        DossierComplexModelChecker.validate(dossierComplexModel);

        // Si le dossier est mis à jour de direct en indirect, on passe aussi
        // ces droits en indirect
        DroitComplexSearchModel droits = new DroitComplexSearchModel();
        droits.setForIdDossier(dossierComplexModel.getId());
        droits = ALServiceLocator.getDroitComplexModelService().search(droits);
        if (JadeNumericUtil.isEmptyOrZero(dossierComplexModel.getDossierModel().getIdTiersBeneficiaire())) {
            for (int i = 0; i < droits.getSize(); i++) {

                ALServiceLocator.getDroitBusinessService().removeBeneficiaire(
                        ((DroitComplexModel) droits.getSearchResults()[i]).getDroitModel());

            }
        }

        dossierComplexModel.setAllocataireComplexModel(ALImplServiceLocator.getAllocataireComplexModelService().update(
                dossierComplexModel.getAllocataireComplexModel()));

        dossierComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().update(
                dossierComplexModel.getDossierModel()));

        // appel pour la propagation des données de la ged
        ALServiceLocator.getGedBusinessService().propagateAllocataire(dossierComplexModel.getDossierModel());

        return dossierComplexModel;
    }

}
