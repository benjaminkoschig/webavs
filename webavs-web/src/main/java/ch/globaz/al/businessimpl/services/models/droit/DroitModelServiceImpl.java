/**
 *
 */
package ch.globaz.al.businessimpl.services.models.droit;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.exceptions.model.droit.ALDroitModelException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.droit.DroitModelService;
import ch.globaz.al.businessimpl.checker.model.droit.DroitModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.al.utils.ALEntityFieldChangeAnalyser;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * classe d'implémentation des services de DroitModel
 * 
 * @author PTA
 * 
 */
public class DroitModelServiceImpl extends ALAbstractBusinessServiceImpl implements DroitModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitModelService#clone(ch
     * .globaz.al.business.models.droit.DroitModel, java.lang.String)
     */
    @Override
    public DroitModel clone(DroitModel droitModel, String idDossier)
            throws JadeApplicationException, JadePersistenceException {
        DroitModel newDroitModel = new DroitModel();
        newDroitModel = droitModel;
        // en fait pas besoin de cloner, car on utilise plus le droitModel de
        // référence, on le laisse se faire manger par le garbage
        // newDroitModel = (DroitModel)
        // ALUtils.cloneSimpleModel(droitModel);
        // les champs relatifs a id droit et spy sont mis à zéro car nouveau
        // dossier
        newDroitModel.setId("");
        newDroitModel.setCreationSpy("");
        newDroitModel.setSpy("");
        newDroitModel.setIdDossier(idDossier);

        return newDroitModel;
    }

    @Override
    public DroitModel copie(DroitModel droitModel) throws JadeApplicationException {

        DroitModel droitAjoute = new DroitModel();

        if (droitModel == null) {
            throw new ALDroitModelException("Unable to copy droitModel- the model passed is null");
        }
        // copie des champs du droit
        droitAjoute.setDebutDroit(droitModel.getDebutDroit());
        droitAjoute.setEtatDroit(droitModel.getEtatDroit());
        droitAjoute.setFinDroitForcee(droitModel.getFinDroitForcee());
        droitAjoute.setIdDossier(droitModel.getIdDossier());
        droitAjoute.setIdEnfant(droitModel.getIdEnfant());
        droitAjoute.setIdTiersBeneficiaire(droitModel.getIdTiersBeneficiaire());
        droitAjoute.setImprimerEcheance(droitModel.getImprimerEcheance());
        droitAjoute.setMontantForce(droitModel.getMontantForce());
        droitAjoute.setMotifFin(droitModel.getMotifFin());
        droitAjoute.setMotifReduction(droitModel.getMotifReduction());
        droitAjoute.setTarifForce(droitModel.getTarifForce());
        droitAjoute.setTauxVersement(droitModel.getTauxVersement());
        droitAjoute.setTypeDroit(droitModel.getTypeDroit());
        droitAjoute.setStatutFamilial(droitModel.getStatutFamilial());
        droitAjoute.setForce(droitModel.getForce());
        droitAjoute.setSupplementActif(droitModel.getSupplementActif());
        droitAjoute.setSupplementFnb(droitModel.getSupplementFnb());
        droitAjoute.setAttestationAlloc(droitModel.getAttestationAlloc());
        return droitAjoute;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitModelService#count(ch
     * .globaz.al.business.models.droit.DroitSearch)
     */
    @Override
    public int count(DroitSearchModel droitSearch) throws JadePersistenceException, JadeApplicationException {

        if (droitSearch == null) {
            throw new ALDroitModelException("DroitModelServiceImpl#count : Unable to count, droitSearch is null");
        }

        return JadePersistenceManager.count(droitSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.DroitModelService#create(ch.globaz .al.business.model.droit.DroitModel)
     */
    @Override
    public DroitModel create(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException {
        if (droitModel == null) {
            throw new ALDroitModelException("unable to create Droit - the model passed is null");
        }
        // contrôle de validité métier et des données du Droit
        DroitModelChecker.validate(droitModel);

        // ajoute le Droit dans la persistance et le retourne
        DroitModel droit = (DroitModel) JadePersistenceManager.add(droitModel);

        if (ALProperties.DECISION_FILE_ATTENTE.getBooleanValue()) {
            checkChanges(droit);
        }

        return droit;
    }

    /**
     * Stock l'id du gestionnaire ayant rajouté un nouveau droit au dossier
     *
     * @param droit
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void checkChanges(DroitModel droit)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {
        String id = droit.getIdDossier();
        DossierModel dossier = ALServiceLocator.getDossierModelService().read(id);
        if (dossier == null || dossier.isNew()) {
            throw new ALDossierModelException("Unable to load DossierModel with id [" + id + "]");
        }
        dossier.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
        JadePersistenceManager.update(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.DroitModelService#delete(ch.globaz .al.business.model.droit.DroitModel)
     */
    @Override
    public DroitModel delete(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException {
        if (droitModel == null) {
            throw new ALDroitModelException("Unable to delete Droit-the model passed is null");
        }
        if (droitModel.isNew()) {
            throw new ALDroitModelException("Unable to delete Droit, th id passed is new");
        }

        DroitModelChecker.validateForDelete(droitModel);

        // suppression
        return (DroitModel) JadePersistenceManager.delete(droitModel);
    }

    @Override
    public List<DroitModel> findActifs(String idDossier, String date)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {
        List<DroitModel> droitsModels = new ArrayList<DroitModel>();
        DroitSearchModel searchModel = new DroitSearchModel();
        searchModel.setForIdDossier(idDossier);

        searchModel = ALImplServiceLocator.getDroitModelService().search(searchModel);
        for (int i = 0; i < searchModel.getSearchResults().length; i++) {
            DroitModel droit = (DroitModel) searchModel.getSearchResults()[i];
            if (ALServiceLocator.getDroitBusinessService().isDroitActif(droit, date)) {
                droitsModels.add(droit);
            }
        }
        return droitsModels;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitModelService#initModel
     * (ch.globaz.al.business.models.droit.DroitModel)
     */
    @Override
    public DroitModel initModel(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException {

        // valeurs par défaut
        droitModel.setTypeDroit(ALCSDroit.TYPE_ENF);
        droitModel.setDebutDroit("");
        droitModel.setFinDroitForcee("");
        droitModel.setDateAttestationEtude("");
        droitModel.setEtatDroit(ALCSDroit.ETAT_A);
        droitModel.setImprimerEcheance(true);
        droitModel.setMotifFin(ALCSDroit.MOTIF_FIN_ECH);
        droitModel.setMontantForce("");
        droitModel.setSupplementActif(true);
        droitModel.setSupplementFnb(false);

        return droitModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.DroitModelService#read(java.lang .String)
     */
    @Override
    public DroitModel read(String idDroitModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idDroitModel)) {
            throw new ALDroitModelException("Unable to read the droit- the id passed is null");
        }
        DroitModel droitModel = new DroitModel();
        droitModel.setId(idDroitModel);
        return (DroitModel) JadePersistenceManager.read(droitModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitModelService#search(ch.globaz.al.business.models.droit.
     * DroitSearchModel)
     */
    @Override
    public DroitSearchModel search(DroitSearchModel droitSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (droitSearchModel == null) {
            throw new ALDroitModelException(
                    "DroitModelServiceImpl#search: unable to search droits, searchModel passed is empty");
        }

        return (DroitSearchModel) JadePersistenceManager.search(droitSearchModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.droit.DroitModelService#update(ch.globaz .al.business.model.droit.DroitModel)
     */
    @Override
    public DroitModel update(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException {
        if (droitModel == null) {
            throw new ALDroitModelException("Unable to update the droit-the model passed is empty");
        }
        if (droitModel.isNew()) {
            throw new ALDroitModelException("unable to update the droit-th model passed is new");

        }
        // contrôle de validité des données
        DroitModelChecker.validate(droitModel);
        if (ALProperties.DECISION_FILE_ATTENTE.getBooleanValue()) {
            trigChangesParGestionnaire(droitModel);
        }

        return (DroitModel) JadePersistenceManager.update(droitModel);
    }

    /**
     * Le but de cette méthode est d'intercepté certains changement qui sont fait sur le droit et stocker l'id du
     * gestionnaire ayant réalisé les changements au niveau du dossier
     *
     * @param droitToUpdate
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void trigChangesParGestionnaire(DroitModel droitToUpdate)
            throws JadeApplicationException, JadePersistenceException {
        // Recherche le dossier stocké en DB pour comparer certain champs avec le dossier qui va être mis à jour
        String id = droitToUpdate.getId();
        DroitModel persistentDroit = read(id);
        if (persistentDroit == null || persistentDroit.isNew()) {
            throw new ALDroitModelException("Unable to load DroitModel with id [" + id + "]");
        }

        // contrôle si la date d'échéance à changé
        boolean dateFinDroitChange = ALEntityFieldChangeAnalyser.hasValueChanged(persistentDroit.getFinDroitForcee(),
                droitToUpdate.getFinDroitForcee());

        // contrôle si le motif à changé
        boolean motifChange = ALEntityFieldChangeAnalyser.hasValueChanged(persistentDroit.getMotifFin(),
                droitToUpdate.getMotifFin());

        // contrôle si le montant fixe à changé
        boolean montantFixeChange = ALEntityFieldChangeAnalyser.hasValueChanged(persistentDroit.getMontantForce(),
                droitToUpdate.getMontantForce());

        // contrôle si l'état du droit changé CSETAT
        boolean etatDroitChange = ALEntityFieldChangeAnalyser.hasValueChanged(persistentDroit.getEtatDroit(),
                droitToUpdate.getEtatDroit());

        boolean change = dateFinDroitChange || motifChange || montantFixeChange || etatDroitChange;

        if (change) {
            String idDossierModel = droitToUpdate.getIdDossier();
            DossierModel dossier = ALServiceLocator.getDossierModelService().read(idDossierModel);
            dossier.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
            JadePersistenceManager.update(dossier);
        }
    }
}
