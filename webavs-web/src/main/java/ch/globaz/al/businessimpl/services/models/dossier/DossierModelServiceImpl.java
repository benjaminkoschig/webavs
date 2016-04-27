package ch.globaz.al.businessimpl.services.models.dossier;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.models.dossier.CopieSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.services.models.dossier.DossierModelService;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.dossier.DossierModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.al.utils.ALEntityFieldChangeAnalyser;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;

/**
 * Implémentation du service de gestion de la persistance des données des dossiers
 * 
 * @author jts
 */
public class DossierModelServiceImpl extends ALAbstractBusinessServiceImpl implements DossierModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#clone
     * (ch.globaz.al.business.models.dossier.DossierModel)
     */
    @Override
    public DossierModel clone(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException {

        DossierModel newDossierModel = new DossierModel();
        newDossierModel = dossierModel;
        // pas besoin de "cloner", car on n'utilise plus le
        // dossierModel de
        // référence, on le laisse se faire manger par le garbage
        // les champs relatifs a id dossier et spy sont mis à zéro car nouveau
        // dossier
        newDossierModel.setId("");
        newDossierModel.setCreationSpy("");
        newDossierModel.setSpy("");

        return newDossierModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#create
     * (ch.globaz.al.business.models.dossier.DossierModel)
     */
    @Override
    public DossierModel create(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException {

        if (dossierModel == null) {
            throw new ALDossierModelException("Unable to add model (dossierModel) - the model passed is null!");
        }
        // Valide le modèle
        DossierModelChecker.validate(dossierModel);
        // L'ajoute en persistence
        return (DossierModel) JadePersistenceManager.add(dossierModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#delete
     * (ch.globaz.al.business.models.dossier.DossierModel)
     */
    @Override
    public DossierModel delete(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException {
        if (dossierModel == null) {
            throw new ALDossierModelException("Unable to remove model (dossierModel) - the model passed is null!");
        }
        if (dossierModel.isNew()) {
            throw new ALDossierModelException("Unable to remove model (dossierModel) - the model passed is new!");
        }

        DossierModelChecker.validateForDelete(dossierModel);

        if (!ALAbstractChecker.hasError()) {

            // suppression des commentaires
            CommentaireSearchModel commSearch = new CommentaireSearchModel();
            commSearch.setForIdDossier(dossierModel.getIdDossier());
            JadePersistenceManager.delete(commSearch);

            // suppression des copies
            CopieSearchModel copieSearch = new CopieSearchModel();
            copieSearch.setForIdDossier(dossierModel.getIdDossier());
            JadePersistenceManager.delete(copieSearch);

            // recherche des droits
            DroitSearchModel droitSearch = new DroitSearchModel();
            droitSearch.setForIdDossier(dossierModel.getIdDossier());
            droitSearch = ALImplServiceLocator.getDroitModelService().search(droitSearch);

            // suppression des annonces RAFam (non-transmises) pour chaque droit
            for (JadeAbstractModel droit : droitSearch.getSearchResults()) {
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSent(((DroitModel) droit).getIdDroit());
                ALImplServiceLocator.getDroitModelService().delete((DroitModel) droit);
            }
        }

        // Le supprime en DB
        return (DossierModel) JadePersistenceManager.delete(dossierModel);
    }

    @Override
    public DossierModel initModel(DossierModel dossierModel) throws JadeApplicationException {

        if (dossierModel == null) {
            throw new ALDossierModelException("DossierModelServiceImpl#initModel : dossierModel is null");
        }

        // valeurs par défaut
        dossierModel.setActiviteAllocataire(ALCSDossier.ACTIVITE_SALARIE);
        // dossierModel.setEtatDossier(ALCSDossier.ETAT_SUSPENDU);
        dossierModel.setImprimerDecision(Boolean.TRUE);
        dossierModel.setStatut(ALCSDossier.STATUT_N);
        dossierModel.setUniteCalcul(ALCSDossier.UNITE_CALCUL_MOIS);
        dossierModel.setDebutValidite("");
        dossierModel.setFinValidite("");
        dossierModel.setDebutActivite("");
        dossierModel.setFinActivite("");
        dossierModel.setTauxOccupation("100.0");
        dossierModel.setCategorieProf("ECAPRO");
        dossierModel.setTauxVersement("100.0");
        dossierModel.setMontantForce("");
        dossierModel.setMotifReduction(ALCSDossier.MOTIF_REDUC_COMP);
        dossierModel.setRetenueImpot(false);
        dossierModel.setReference(JadeThread.currentUserName());
        dossierModel.setProfessionAccessoire(false);

        // Valide le modèle
        // DossierModelChecker.validate(dossierModel);

        return dossierModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#read (java.lang.String)
     */
    @Override
    public DossierModel read(String idDossierModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idDossierModel)) {
            throw new ALDossierModelException("Unable to read model (dossierModel) - the id passed is not defined!");
        }
        DossierModel dossierModel = new DossierModel();
        dossierModel.setId(idDossierModel);
        return (DossierModel) JadePersistenceManager.read(dossierModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#search
     * (ch.globaz.al.business.models.dossier.DossierSearchModel)
     */
    @Override
    public DossierSearchModel search(DossierSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new ALDossierModelException("DossierModelServiceImpl#search : searchModel is null");
        }

        return (DossierSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#update
     * (ch.globaz.al.business.models.dossier.DossierModel)
     */
    @Override
    public DossierModel update(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException {
        if (dossierModel == null) {
            throw new ALDossierModelException("Unable to update model (dossierModel) - the model passed is null!");
        }
        if (dossierModel.isNew()) {
            throw new ALDossierModelException("Unable to update model (dossierModel) - the model passed is new!");
        }
        // Valide l'integrity
        DossierModelChecker.validate(dossierModel);

        // Doit être fait avant la sauvegarde afin de détecter les changes
        if (ALProperties.DECISION_FILE_ATTENTE.getBooleanValue()) {
            checkChanges(dossierModel);
        }

        return (DossierModel) JadePersistenceManager.update(dossierModel);
    }

    /**
     * Le but de cette méthode est d'intercepté certains changement qui sont fait sur le dossier et stocker l'id du
     * gestionnaire ayant réalisé les changements
     *
     * @param dossierToUpdate
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void checkChanges(DossierModel dossierToUpdate) throws JadeApplicationException, JadePersistenceException {
        // Recherche le dossier stocké en DB pour comparer certain champs avec le dossier qui va être mis à jour
        String id = dossierToUpdate.getId();
        DossierModel persistentDossier = read(id);
        if (persistentDossier == null || persistentDossier.isNew()) {
            throw new ALDossierModelException("Unable to load DossierModel with id [" + id + "]");
        }

        // contrôle si la date de validité à changé
        boolean dateDebutValiditeChange = ALEntityFieldChangeAnalyser
                .hasValueChanged(persistentDossier.getDebutValidite(), dossierToUpdate.getDebutValidite());
        boolean dateFinValiditeChange = ALEntityFieldChangeAnalyser.hasValueChanged(persistentDossier.getFinValidite(),
                dossierToUpdate.getFinValidite());

        if (dateDebutValiditeChange || dateFinValiditeChange) {
            dossierToUpdate.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
        }

    }

}
