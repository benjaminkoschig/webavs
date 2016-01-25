package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.exceptions.business.ALDossierBusinessException;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.models.dossier.DossierActifComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierLieComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.dossier.LienDossierSearchModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALErrorsUtils;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Implémentation du service de gestion métier particulier en relation avec un dossier
 * 
 * @author PTA/GMO/JTS
 * 
 */
public class DossierBusinessServiceImpl extends ALAbstractBusinessServiceImpl implements DossierBusinessService {

    @Override
    public void ajouterLien(String idDossierPere, String idDossierFils, String typeLien)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idDossierPere)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#ajouterLien : idDossierPere is empty");
        }

        if (JadeStringUtil.isEmpty(idDossierFils)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#ajouterLien : idDossierFils is empty");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_LIEN_DOSSIER, typeLien)) {
                throw new ALDossierBusinessException("DossierBusinessServiceImpl#ajouterLien : " + typeLien
                        + " is not a valid link type");
            }
        } catch (Exception e) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#ajouterLien : unable to check typeLien'",
                    e);
        }
        // création du lien père-fils...
        LienDossierModel lienDossierModel1 = new LienDossierModel();
        lienDossierModel1.setIdDossierPere(idDossierPere);
        lienDossierModel1.setIdDossierFils(idDossierFils);
        lienDossierModel1.setTypeLien(typeLien);
        ALImplServiceLocator.getLienDossierModelService().create(lienDossierModel1);
        // ...et son inverse
        LienDossierModel lienDossierModel2 = new LienDossierModel();
        lienDossierModel2.setIdDossierPere(idDossierFils);
        lienDossierModel2.setIdDossierFils(idDossierPere);
        lienDossierModel2.setTypeLien(typeLien);
        ALImplServiceLocator.getLienDossierModelService().create(lienDossierModel2);

    }

    @Override
    public void changerTypeLien(String idLien, String newTypeLien) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idLien)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#changerTypeLien : idLien is empty");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_LIEN_DOSSIER, newTypeLien)) {
                throw new ALDossierBusinessException("DossierBusinessServiceImpl#changerTypeLien : " + newTypeLien
                        + " is not a valid link type");
            }
        } catch (Exception e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#changerTypeLien : unable to check newTypeLien'", e);
        }

        LienDossierModel lienToUpdate = ALImplServiceLocator.getLienDossierModelService().read(idLien);
        lienToUpdate.setTypeLien(newTypeLien);

        LienDossierModel lienInverseToUpdate = ALImplServiceLocator.getDossierBusinessService().getLienInverse(
                lienToUpdate);
        lienInverseToUpdate.setTypeLien(newTypeLien);
        ALImplServiceLocator.getLienDossierModelService().update(lienToUpdate);
        ALImplServiceLocator.getLienDossierModelService().update(lienInverseToUpdate);

    }

    @Override
    public DossierComplexModel copierDossier(DossierComplexModel refDossierComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        return this.copierDossier(refDossierComplexModel, ALCSDossier.ETAT_SUSPENDU);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService#
     * copierDossier(ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel copierDossier(DossierComplexModel refDossierComplexModel, String etatNouveauDossier)
            throws JadeApplicationException, JadePersistenceException {

        if (refDossierComplexModel == null) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#copierDossier : refDossierComplexModel is null");
        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ETAT, etatNouveauDossier)) {
                throw new ALDossierBusinessException("DossierBusinessServiceImpl#copierDossier : " + etatNouveauDossier
                        + " is not a valid etat value");
            }
        } catch (Exception e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#copierDossier : unable to check etatNouveauDossier'", e);
        }

        // On garde l'id du dossier de référence pour pouvoir rechercher les
        // commentaires liés et les droits liés
        String idRefDossier = refDossierComplexModel.getId();

        DossierComplexModel newDossierComplexModel = new DossierComplexModel();
        // Clonage du dossier
        newDossierComplexModel = ALServiceLocator.getDossierComplexModelService().clone(refDossierComplexModel);
        newDossierComplexModel.getDossierModel().setEtatDossier(etatNouveauDossier);
        // INFO:ne génére pas d'annonce ici car les droits n'existent pas encore
        ALServiceLocator.getDossierBusinessService().createDossier(newDossierComplexModel);

        // une fois le nouveau dossier créé on ajoute un lien entre les 2 dossiers
        ALServiceLocator.getDossierBusinessService().ajouterLien(idRefDossier,
                newDossierComplexModel.getDossierModel().getIdDossier(), ALCSDossier.LIEN_ALLOC);

        // appel pour la propagation des données de la ged
        ALServiceLocator.getGedBusinessService().propagateAllocataire(newDossierComplexModel.getDossierModel());

        // on recherche les commentaires du dossier référence et on les clone en
        // les liant au nouveau dossier (clone du référence)
        CommentaireSearchModel commSearch = new CommentaireSearchModel();
        commSearch.setForIdDossier(idRefDossier);
        commSearch = ALServiceLocator.getCommentaireModelService().search(commSearch);
        for (int i = 0; i < commSearch.getSize(); i++) {
            CommentaireModel currentComm = ALServiceLocator.getCommentaireModelService().clone(
                    ((CommentaireModel) commSearch.getSearchResults()[i]), newDossierComplexModel.getId());
            ALServiceLocator.getCommentaireModelService().create(currentComm);
        }

        // On recherche les droits liés au dossier de référence pour ensuite les
        // cloner
        DroitComplexSearchModel droitSearch = new DroitComplexSearchModel();
        droitSearch.setForIdDossier(idRefDossier);
        droitSearch = ALServiceLocator.getDroitComplexModelService().search(droitSearch);

        for (int i = 0; i < droitSearch.getSize(); i++) {

            DroitComplexModel currentDroitComplex = ALServiceLocator.getDroitComplexModelService().clone(
                    (DroitComplexModel) droitSearch.getSearchResults()[i], newDossierComplexModel.getId());

            // création du droit et des annonces RAFAM si nécessaire
            ALServiceLocator.getDroitBusinessService().createDroitEtEnvoyeAnnoncesRafam(currentDroitComplex, true);

        }

        return newDossierComplexModel;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# createDroitEtEnvoyeAnnoncesRafam
     * (ch.globaz.al.business.models.dossier.DossierComplexModel)
     * 
     * @deprecated
     */
    @Override
    public DossierComplexModel createDossier(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {

        if (dossier == null) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#createDroitEtEnvoyeAnnoncesRafam : dossier id null");
        }

        dossier = ALServiceLocator.getDossierComplexModelService().create(dossier);

        if (!dossier.getDossierModel().isNew() && !dossier.getAllocataireComplexModel().getAllocataireModel().isNew()) {
            // on journalise après avoir créé le dossier, car il faut un id dossier (id externe en journalisation)
            journaliserCreationDossier(dossier);
            // Si on est la FPV on effectue la propagation spécifique en GED
            if (ALConstCaisse.CAISSE_FPV.equals(ALServiceLocator.getParametersServices().getNomCaisse())) {
                ALServiceLocator.getGedBusinessService().propagateDossierForGEDFPV(dossier);
            } else {
                ALServiceLocator.getGedBusinessService().propagateAllocataire(dossier.getDossierModel());
            }
        }
        return dossier;
    }

    @Override
    public DossierComplexModel createDossier(DossierComplexModel dossier, DossierAgricoleComplexModel dossierAgricole)
            throws JadeApplicationException, JadePersistenceException {

        String idAllocataire = dossier.getAllocataireComplexModel().getAllocataireModel().getIdAllocataire();
        AgricoleSearchModel agricoleSearchModel = new AgricoleSearchModel();
        agricoleSearchModel.setForIdAllocataire(idAllocataire);
        agricoleSearchModel = ALImplServiceLocator.getAgricoleModelService().search(agricoleSearchModel);

        boolean isAgricoleContext = ALServiceLocator.getDossierBusinessService().isAgricole(
                dossier.getDossierModel().getActiviteAllocataire());

        if ((agricoleSearchModel.getSize() == 0) && isAgricoleContext) {

            if (ALCSDossier.ACTIVITE_PECHEUR.equals(dossier.getDossierModel().getActiviteAllocataire())) {
                dossierAgricole.getAllocataireAgricoleComplexModel().getAgricoleModel().setDomaineMontagne(false);
            }

            ALImplServiceLocator.getAgricoleModelService().create(
                    dossierAgricole.getAllocataireAgricoleComplexModel().getAgricoleModel());
        }

        dossier = this.createDossier(dossier);

        return dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# deleteDossierEtEnvoyeAnnoncesRafam
     * (ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel deleteDossier(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {

        return ALServiceLocator.getDossierComplexModelService().delete(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# getActivitesCategorieCotPers()
     */
    @Override
    public HashSet<String> getActivitesCategorieCotPers() {
        HashSet<String> set = new HashSet<String>();
        set.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
        set.add(ALCSDossier.ACTIVITE_INDEPENDANT);
        set.add(ALCSDossier.ACTIVITE_NONACTIF);
        set.add(ALCSDossier.ACTIVITE_PECHEUR);
        set.add(ALCSDossier.ACTIVITE_TSE);
        set.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);
        return set;
    }

    // TODO : vérifier type allocataire dans getActivitesCategorieCotPers et getActivitesToProcess
    @Override
    public HashSet<String> getActivitesToProcess(String typeCoti) throws JadeApplicationException {

        if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            throw new ALGenerationException("DossierBusinessServiceImpl#getActivitesToProcess : " + typeCoti
                    + " is not valid");
        }

        // TODO (lot 2) à remplacer par une récupération de code système
        // directement depuis la DB
        HashSet<String> set = new HashSet<String>();
        set.add(ALCSDossier.ACTIVITE_AGRICULTEUR);
        set.add(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE);
        set.add(ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE);
        set.add(ALCSDossier.ACTIVITE_INDEPENDANT);
        set.add(ALCSDossier.ACTIVITE_NONACTIF);
        set.add(ALCSDossier.ACTIVITE_PECHEUR);
        set.add(ALCSDossier.ACTIVITE_SALARIE);
        set.add(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE);
        set.add(ALCSDossier.ACTIVITE_TSE);
        set.add(ALCSDossier.ACTIVITE_VIGNERON);

        if (ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)) {
            return set;
        } else if (ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
            set.retainAll(getActivitesCategorieCotPers());
        } else if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
            set.removeAll(getActivitesCategorieCotPers());
        } else {
            throw new ALDroitBusinessException("DossierBusinessServiceImpl#getActivitesToProcess : " + typeCoti
                    + " is not suported");
        }

        return set;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.droit.DroitBusinessService# getDossierComplexForDroit
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DossierComplexModel getDossierComplexForDroit(DroitComplexModel droitComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        DossierComplexModel dossierComplex = ALServiceLocator.getDossierComplexModelService().read(
                droitComplexModel.getDroitModel().getIdDossier());

        if (dossierComplex.isNew()) {
            throw new ALDroitBusinessException(
                    "DroitBusinessServiceImpl#searchDossierComplexForEcheance : dossierComplex is new");
        }

        return dossierComplex;
    }

    @Override
    public DossierLieComplexSearchModel getDossiersLies(String idDossierPere) throws JadeApplicationException,
            JadePersistenceException {

        DossierLieComplexSearchModel search = new DossierLieComplexSearchModel();
        search.setForIdDossierPere(idDossierPere);
        search.setWhereKey("dossiersFils");
        return ALServiceLocator.getDossierLieComplexModelService().search(search);
    }

    @Override
    public String getGenreAssurance(String activiteAllocataire) throws JadeApplicationException {

        boolean activiteOk = false;

        try {
            activiteOk = JadeCodesSystemsUtil
                    .checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire);
        } catch (Exception e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#getGenreAssurance : unable to check 'activiteAllocataire'", e);
        }

        if (!activiteOk) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getGenreAssurance : "
                    + activiteAllocataire + " is not a valid activity type");
        }

        return (isParitaire(activiteAllocataire) ? ALCSAffilie.GENRE_ASSURANCE_PARITAIRE
                : ALCSAffilie.GENRE_ASSURANCE_INDEP);
    }

    @Override
    public JadeAbstractModel[] getIdDossiersActifs(String nssAllocataire, String idAffilie)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(nssAllocataire)) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#getIdDossiersActifs : nssAllocataire is null or empty");
        }

        DossierActifComplexSearchModel search = new DossierActifComplexSearchModel();
        search.setForNssAllocataire(nssAllocataire);
        search.setForEtatDossier(ALCSDossier.ETAT_ACTIF);
        search.setForNumAffilie(idAffilie);

        return (JadePersistenceManager.search(search)).getSearchResults();
    }

    @Override
    public LienDossierModel getLienInverse(LienDossierModel lienOriginal) throws JadeApplicationException,
            JadePersistenceException {
        LienDossierSearchModel searchInverse = new LienDossierSearchModel();
        searchInverse.setForIdDossierFils(lienOriginal.getIdDossierPere());
        searchInverse.setForIdDossierPere(lienOriginal.getIdDossierFils());

        searchInverse = ALImplServiceLocator.getLienDossierModelService().search(searchInverse);

        // il n'y a qu'un seul lien inverse, sinon données altérées
        if (searchInverse.getSize() > 1) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#getLienInverse : impossible de mettre à jour le lien, données altérées entre dossier n°"
                            + lienOriginal.getIdDossierPere() + " et dossier n°" + lienOriginal.getIdDossierFils());
        }
        return (LienDossierModel) searchInverse.getSearchResults()[0];

    }

    @Override
    public JadeAbstractModel[] getResultsAffilieAllocDossiers(String numAffilie, String nssAllocataire)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(nssAllocataire)) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#getIdDossiersActifs : nssAllocataire is null or empty");
        }

        DossierActifComplexSearchModel dossierSearch = new DossierActifComplexSearchModel();
        dossierSearch.setForNssAllocataire(nssAllocataire);
        dossierSearch.setForNumAffilie(numAffilie);

        return (JadePersistenceManager.search(dossierSearch)).getSearchResults();
    }

    @Override
    public String getTypeCotisation(DossierModel dossierModel) throws JadeApplicationException,
            JadePersistenceException {

        if (dossierModel == null) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getTypeCotisation : dossier is null");
        }
        if (isModePaiementDirect(dossierModel)) {
            return ALConstPrestations.TYPE_DIRECT;
        } else {
            String csTemplate = ALServiceLocator.getBusinessProcessusService().getAppliedTemplate(
                    JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3));

            if (ALServiceLocator.getBusinessProcessusService().isFacturationSeparee(csTemplate)) {
                if (ALServiceLocator.getDossierBusinessService().isParitaire(dossierModel.getActiviteAllocataire())) {
                    return ALConstPrestations.TYPE_COT_PAR;
                } else {
                    return ALConstPrestations.TYPE_COT_PERS;
                }
            } else {
                return ALConstPrestations.TYPE_INDIRECT_GROUPE;
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService#hasSentAnnonces(java.lang.String)
     */
    @Override
    public boolean hasSentAnnonces(String idDossier) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDossier)) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#hasSentAnnonces : idDossier is empty or zero");
        }

        DroitSearchModel search = new DroitSearchModel();
        search.setForIdDossier(idDossier);
        search = ALImplServiceLocator.getDroitModelService().search(search);

        if (search.getSize() == 0) {
            return false;
        } else {

            for (int i = 0; i < search.getSearchResults().length; i++) {
                if (ALImplServiceLocator.getAnnoncesRafamSearchService().hasSentAnnonces(
                        ((DroitModel) search.getSearchResults()[i]).getIdDroit())) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean isAffilie(DossierComplexModel dossier, String idTiers) throws JadeApplicationException,
            JadePersistenceException {
        if (dossier == null) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#isAffilie : dossier is null");
        }

        if (JadeStringUtil.isEmpty(idTiers)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#isAffilie : idTiers is null or empty");
        }

        String idTiersNumAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                dossier.getDossierModel().getNumeroAffilie());

        if (idTiers.equals(idTiersNumAffilie)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAgenceCommunale(String idTiers) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idTiers)) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#isAgenceCommunale : idTiers is null or empty");
        }

        AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService().read(idTiers);

        // FIXME remplacer valeur par la constante code système Tiers
        return (!admin.isNew() && "509031".equals(admin.getAdmin().getGenreAdministration()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# isAgricole(java.lang.String)
     */
    @Override
    public boolean isAgricole(String typeActivite) throws JadeApplicationException {

        if (typeActivite == null) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#isAgricole : typeActivite is null");
        }

        boolean isAgricole = (ALCSDossier.ACTIVITE_PECHEUR.equals(typeActivite)
                || ALCSDossier.ACTIVITE_AGRICULTEUR.equals(typeActivite)
                || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(typeActivite) || ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE
                .equals(typeActivite));

        return isAgricole;
    }

    @Override
    public boolean isAllocataire(DossierComplexModel dossier, String idTiers) throws JadeApplicationException,
            JadePersistenceException {
        if (dossier == null) {
            throw new ALDossierBusinessException();
        }
        if (JadeStringUtil.isEmpty(idTiers)) {
            throw new ALDossierBusinessException();
        }

        if (idTiers.equals(dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isModePaiementDirect(DossierModel dossier) {
        if ("0".equals(dossier.getIdTiersBeneficiaire())) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# isParitaire(java.lang.String)
     */
    @Override
    public boolean isParitaire(String typeActivite) throws JadeApplicationException {

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, typeActivite)) {
                throw new ALDossierBusinessException("DossierBusinessServiceImpl#getGenreAssurance : " + typeActivite
                        + " is not a valid activity type");
            }
        } catch (ALDossierBusinessException e) {
            throw e;
        }

        catch (Exception e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#isParitaire : unable to check 'activiteAllocataire'", e);
        }

        return !getActivitesCategorieCotPers().contains(typeActivite);
    }

    private void journaliserChangementEtat(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation) throws ALDossierBusinessException {
        // gestion journalisation
        JadeBusinessMessage[] logMessages = null;
        boolean manualClearLog = false;
        // Libra considère les message de niveau WARN comme des erreurs et empêche le commit de la transaction
        // Pour éviter ce problème les messages sont sauvegardé et retiré du log le temps de l'appel du service de
        // création de la journalisation puis sont réinjecté dans le log une fois l'appel terminé.

        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN) != null) {
            logMessages = JadeThread.logMessages();
            manualClearLog = true;
            JadeThread.logClear();
        }

        try {

            if (((etatDossierAvantModif != null) && !etatDossierAvantModif.equals(dossier.getDossierModel()
                    .getEtatDossier())) || (etatDossierAvantModif == null)) {
                // on prépare le dossier à la journalisation (création dossier journalisation si pas encore existant)
                prepareDossierJournalisation(dossier);
                LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarque(
                        dossier.getId(),
                        ALConstJournalisation.CHANGEMENT_ETAT_DOSSIER.concat(JadeCodesSystemsUtil
                                .getCodeLibelle(dossier.getDossierModel().getEtatDossier())), remarqueJournalisation,
                        true);

            }

            // TODO: gérer si etatDossierAvantModif == null => mise à jour avec état du modèle en mémoire mais remarque
            // différente?

        } catch (LibraException e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#updateDossierEtEnvoiAnnoncesRafam() : impossible de journaliser la mise à jour du dossier, error: "
                            + e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#updateDossierEtEnvoiAnnoncesRafam() : impossible de journaliser la mise à jour du dossier, error: "
                            + e.getMessage(), e);
        } finally {
            if (manualClearLog) {
                ALErrorsUtils.addMessages(logMessages);
            }
        }

    }

    private void journaliserCreationDossier(DossierComplexModel dossier) throws ALDossierBusinessException {
        // gestion journalisation
        JadeBusinessMessage[] logMessages = null;
        boolean manualClearLog = false;
        // Libra considère les message de niveau WARN comme des erreurs et empêche le commit de la transaction
        // Pour éviter ce problème les messages sont sauvegardé et retiré du log le temps de l'appel du service de
        // création de la journalisation puis sont réinjecté dans le log une fois l'appel terminé.

        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN) != null) {
            logMessages = JadeThread.logMessages();
            manualClearLog = true;
            JadeThread.logClear();
        }

        try {

            // on prépare le dossier à la journalisation (création dossier journalisation si pas encore existant)
            prepareDossierJournalisation(dossier);
            LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarque(
                    dossier.getId(),
                    ALConstJournalisation.CREATION_ETAT_DOSSIER.concat(JadeCodesSystemsUtil.getCodeLibelle(dossier
                            .getDossierModel().getEtatDossier())), "", true);

        } catch (LibraException e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#journaliserCreationDossier() : impossible de journaliser la création du dossier, error: "
                            + e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#journaliserCreationDossier() : impossible de journaliser la création du dossier, error: "
                            + e.getMessage(), e);
        } finally {
            if (manualClearLog) {
                ALErrorsUtils.addMessages(logMessages);
            }
        }

    }

    @Override
    public Set<String> listerEtatsOkGenerationGlobale() throws JadeApplicationException, JadePersistenceException {

        ParameterSearchModel searchEtatsGenerationGlobale = new ParameterSearchModel();
        searchEtatsGenerationGlobale.setForIdCleDiffere(ALConstParametres.DOSSIER_ETAT_GEN_GLOBALE);
        searchEtatsGenerationGlobale = ParamServiceLocator.getParameterModelService().search(
                searchEtatsGenerationGlobale);

        Set<String> collectionEtats = new HashSet<String>();

        for (int i = 0; i < searchEtatsGenerationGlobale.getSize(); i++) {
            ParameterModel currentResult = (ParameterModel) searchEtatsGenerationGlobale.getSearchResults()[i];
            // si il y a une virgule dans le paramètre récupéré, c'est que plusieurs valeurs sont introduites
            String[] valuesParam = currentResult.getValeurAlphaParametre().trim().split(",");
            for (String value : valuesParam) {
                collectionEtats.add(value);
            }

        }
        return collectionEtats;
    }

    @Override
    public Set<String> listerEtatsOkJournalise() throws JadeApplicationException, JadePersistenceException {
        ParameterSearchModel searchEtatsJournalise = new ParameterSearchModel();
        searchEtatsJournalise.setForIdCleDiffere(ALConstParametres.DOSSIER_ETAT_JOURNALISE);
        searchEtatsJournalise = ParamServiceLocator.getParameterModelService().search(searchEtatsJournalise);

        Set<String> collectionEtats = new HashSet<String>();

        for (int i = 0; i < searchEtatsJournalise.getSize(); i++) {
            ParameterModel currentResult = (ParameterModel) searchEtatsJournalise.getSearchResults()[i];
            // si il y a une virgule dans le paramètre récupéré, c'est que plusieurs valeurs sont introduites
            String[] valuesParam = currentResult.getValeurAlphaParametre().trim().split(",");
            for (String value : valuesParam) {
                collectionEtats.add(value);
            }
        }
        return collectionEtats;
    }

    @Override
    public Set<String> listerEtatsOkRafam() throws JadeApplicationException, JadePersistenceException {
        ParameterSearchModel searchEtatsRafam = new ParameterSearchModel();
        searchEtatsRafam.setForIdCleDiffere(ALConstParametres.DOSSIER_ETAT_RAFAM);
        searchEtatsRafam = ParamServiceLocator.getParameterModelService().search(searchEtatsRafam);

        Set<String> collectionEtats = new HashSet<String>();

        for (int i = 0; i < searchEtatsRafam.getSize(); i++) {
            ParameterModel currentResult = (ParameterModel) searchEtatsRafam.getSearchResults()[i];
            // si il y a une virgule dans le paramètre récupéré, c'est que plusieurs valeurs sont introduites
            String[] valuesParam = currentResult.getValeurAlphaParametre().trim().split(",");
            for (String value : valuesParam) {
                collectionEtats.add(value);
            }
        }
        return collectionEtats;
    }

    @Override
    public String nbreJourDebutMoisAF(String dateDebutDossier) throws JadePersistenceException,
            JadeApplicationException {
        // Contrôle du paramètre
        if (!JadeStringUtil.isBlankOrZero(dateDebutDossier) && !JadeDateUtil.isGlobazDate(dateDebutDossier)) {
            throw new ALDossierBusinessException("ValiditeNbrJourMoisAFServiceImpl#nbreJourDebutMoisAF:  "
                    + dateDebutDossier + " is not a globazDate valid");
        }

        String nombreDebutJourDossier = null;
        // si date de début n'est pas saisie ou si c'est le 1er jour mois
        if (JadeStringUtil.isBlank(dateDebutDossier) || ALDateUtils.isFirstDay(dateDebutDossier)) {
            nombreDebutJourDossier = ALConstEcheances.NBRE_JOUR_DOSSIER_ZERO;

        }
        // si date début pas le dernier jour du mois => en cours de mois
        else if (!ALDateUtils.isLastDay(dateDebutDossier)) {
            int nombreJourFinMois = (ALConstEcheances.NBRE_JOUR_MOIS_AF - JadeStringUtil.toInt(dateDebutDossier
                    .substring(0, 2))) + 1;
            nombreDebutJourDossier = Integer.toString(nombreJourFinMois);

        }
        // si on est le dernier jour du mois => 1 jour sauf février
        else if (ALDateUtils.isLastDay(dateDebutDossier)) {
            if (JadeStringUtil.equals(dateDebutDossier.substring(0, 2), ALConstEcheances.JOUR_FIN_FEVR, true)
                    || JadeStringUtil.equals(dateDebutDossier.substring(0, 2), ALConstEcheances.JOUR_FIN_FEVR_BISS,
                            true)) {
                int nombreJourFinMois = (ALConstEcheances.NBRE_JOUR_MOIS_AF - JadeStringUtil.toInt(dateDebutDossier
                        .substring(0, 2))) + 1;
                nombreDebutJourDossier = Integer.toString(nombreJourFinMois);
            } else {
                nombreDebutJourDossier = ALConstEcheances.NBRE_JOUR_UN;
            }

        }

        else {
            throw new ALDossierBusinessException(
                    "ValiditeNbrJourMoisAFServiceImpl#nbreJourDebutMoisAF: error case not considered");
        }

        return nombreDebutJourDossier;
    }

    @Override
    public String nbreJourFinMoisAF(String dateFinDossier) throws JadePersistenceException, JadeApplicationException {

        // Contrôle du paramètre
        if (!JadeStringUtil.isBlank(dateFinDossier) && !JadeDateUtil.isGlobazDate(dateFinDossier)) {
            throw new ALDossierBusinessException("ValiditeNbrJourMoisAFServiceImpl#nbreJourFinMoisAF:  "
                    + dateFinDossier + " is not a globazDate valid");
        }

        String nombreFinJourDossier = null;

        // si date de fin pas saisie ou dernier jour du mois => 0
        if (JadeStringUtil.isBlank(dateFinDossier) || ALDateUtils.isLastDay(dateFinDossier)) {
            nombreFinJourDossier = ALConstEcheances.NBRE_JOUR_DOSSIER_ZERO;

        }
        // sinon si pas le dernier jour =>
        else if (!ALDateUtils.isLastDay(dateFinDossier)) {
            int nombreJourFinMois = JadeStringUtil.toInt(dateFinDossier.substring(0, 2));
            nombreFinJourDossier = Integer.toString(nombreJourFinMois);

        } else {
            throw new ALDossierBusinessException(
                    "ValiditeNbrJourMoisAFServiceImpl#nbreJourFinMoisAF: error case not considered");
        }
        if (JadeStringUtil.toInt(nombreFinJourDossier) >= ALConstEcheances.NBRE_JOUR_MOIS_AF) {
            nombreFinJourDossier = ALConstEcheances.NBRE_JOUR_DOSSIER_ZERO;

        }

        return nombreFinJourDossier;
    }

    private void prepareDossierJournalisation(DossierComplexModel dossier)
            throws JadeApplicationServiceNotAvailableException, LibraException {

        int nbMsgInTransaction = 0;

        if (JadeThread.logMessages() != null) {
            nbMsgInTransaction = JadeThread.logMessages().length;
        }

        LibraServiceLocator.getDossierService().createDossier(
                dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                ILIConstantesExternes.CS_DOMAINE_AF, dossier.getId());

        // HACK Inforom527 sans nouveau service LIDossiersInterfaces/DossierServiceImpl: si on a récolte un nouveau
        // message, c'est que le dossier existait déjà (on le suppose en tout cas)
        // on ignore ce message car on a ce qu'on veut
        if ((JadeThread.logMessages() != null) && (JadeThread.logMessages().length > nbMsgInTransaction)
                && (nbMsgInTransaction == 0)) {
            JadeThread.logClear();
            BSessionUtil.getSessionFromThreadContext().getErrors()
                    .delete(0, BSessionUtil.getSessionFromThreadContext().getErrors().length());
            // on efface aussi les erreurs qu'il y a dans la transaction liée à la session car libra
            // enregistre les erreurs dans les 2, et enpêche les chargements Manager / entity plus tard dans la
            // transaction
            BSessionUtil
                    .getSessionFromThreadContext()
                    .getCurrentThreadTransaction()
                    .getErrors()
                    .delete(0,
                            BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().getErrors()
                                    .length());

        }

    }

    @Override
    public DossierModel radierDossier(DossierModel dossier, String dateRadiation, boolean updateNbJours,
            String remarqueJournalisation) throws JadeApplicationException, JadePersistenceException {
        return this.radierDossier(dossier, dateRadiation, updateNbJours, remarqueJournalisation, null);
    }

    @Override
    public DossierModel radierDossier(DossierModel dossier, String dateRadiation, boolean updateNbJours,
            String remarqueJournalisation, String reference) throws JadeApplicationException, JadePersistenceException {

        if ((dossier == null) || dossier.isNew()) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#radierDossier : dossier is null or new");
        }

        // Appel au service update du modèle
        DossierComplexModel dossierComplex = ALServiceLocator.getDossierComplexModelService().read(
                dossier.getIdDossier());
        // affectation de l'état du dossier à radié
        dossierComplex.getDossierModel().setEtatDossier(ALCSDossier.ETAT_RADIE);
        dossierComplex.getDossierModel().setFinValidite(dateRadiation);
        dossierComplex.getDossierModel().setFinActivite(dateRadiation);
        dossierComplex.getDossierModel().setDebutValidite("");
        dossierComplex.getDossierModel().setNbJoursDebut("0");
        if (!JadeStringUtil.isBlank(reference)) {
            dossierComplex.getDossierModel().setReference(reference);
        }
        // on passe null comme état dossier avant, pour qu'il y a de toute façon une journalisation changement d'état
        // => radié
        journaliserChangementEtat(dossierComplex, null, remarqueJournalisation);

        if (updateNbJours) {
            dossierComplex.getDossierModel().setNbJoursFin(
                    ALServiceLocator.getDossierBusinessService().nbreJourFinMoisAF(dateRadiation));
        }

        dossierComplex = ALServiceLocator.getDossierComplexModelService().update(dossierComplex);

        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.RADIATION, dossierComplex);

        return dossierComplex.getDossierModel();
    }

    @Override
    public DossierModel radierDossierHorloger(DossierModel dossier, String dateRadiation)
            throws JadeApplicationException, JadePersistenceException {

        if ((dossier == null) || dossier.isNew()) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#radierDossier : dossier is null or new");
        }

        dossier.setEtatDossier(ALCSDossier.ETAT_RADIE);
        dossier.setFinValidite(dateRadiation);
        dossier.setFinActivite(dateRadiation);
        dossier.setDebutValidite("");
        dossier.setNbJoursDebut("0");
        dossier.setNbJoursFin("0");

        dossier = ALServiceLocator.getDossierModelService().update(dossier);

        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.RADIATION,
                ALServiceLocator.getDossierComplexModelService().read(dossier.getIdDossier()));

        // TODO: journalisation ici
        // JadeBusinessMessage[] logMessages = null;

        // Libra considère les message de niveau WARN comme des erreurs et empêche le commit de la transaction
        // Pour éviter ce problème les messages sont sauvegardé et retiré du log le temps de l'appel du service de
        // création de la journalisation puis sont réinjecté dans le log une fois l'appel terminé.
        /*
         * if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN) != null) { logMessages =
         * JadeThread.logMessages(); JadeThread.logClear(); }
         * 
         * try { LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(
         * dossier.getId(), ALConstJournalisation.RADIATION_AUTO_DOSSIER,
         * "Dossier radié par radiation automatique, dernière prestation : " + date,
         * dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
         * ILIConstantesExternes.CS_DOMAINE_AF, true); } catch (LibraException e) { throw new
         * ALDossierBusinessException( "RadiationAutomatiqueServiceImpl#radier() : LibraException error: " +
         * e.getMessage(), e); } finally { ALLibraUtils.addMessages(logMessages); }
         */

        return dossier;
    }

    @Override
    public String readWidget_nbreJourDebutMoisAF(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException {

        String dateDebutValidite = values.get("dateDebutValidite").toString();
        try {
            return ALServiceLocator.getDossierBusinessService().nbreJourDebutMoisAF(dateDebutValidite);
        } catch (ALDossierBusinessException e) {
            return "-1";
        }
    }

    @Override
    public String readWidget_nbreJourFinMoisAF(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException {
        String dateFinValidite = values.get("dateFinValidite").toString();
        try {
            return ALServiceLocator.getDossierBusinessService().nbreJourFinMoisAF(dateFinValidite);
        } catch (ALDossierBusinessException e) {
            return "-1";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# retirerBeneficiaire
     * (ch.globaz.al.business.models.dossier.DossierComplexModel, boolean)
     */
    @Override
    public DossierComplexModel retirerBeneficiaire(DossierComplexModel dossierComplexModel, boolean keepDirect)
            throws JadeApplicationException {

        if (dossierComplexModel == null) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#retirerBeneficiaire : dossierComplexModel is null");
        }
        // Si on retire le tiers bénéficaire, on laisse quand même le
        // dossier en paiement direct (à l'alloc)
        if (keepDirect) {
            dossierComplexModel.getDossierModel().setIdTiersBeneficiaire(
                    dossierComplexModel.getAllocataireComplexModel().getPersonneEtendueComplexModel().getId());
        } else {
            dossierComplexModel.getDossierModel().setIdTiersBeneficiaire("0");
        }
        return dossierComplexModel;
    }

    @Override
    public void retirerLien(String idLien) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idLien)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#retirerLien : idLien is empty");
        }

        LienDossierModel lienDossierModel = ALImplServiceLocator.getLienDossierModelService().read(idLien);
        LienDossierModel lienInverse = ALImplServiceLocator.getDossierBusinessService()
                .getLienInverse(lienDossierModel);
        ALImplServiceLocator.getLienDossierModelService().delete(lienDossierModel);
        ALImplServiceLocator.getLienDossierModelService().delete(lienInverse);
    }

    @Override
    public DossierAttestationVersementComplexSearchModel searchDossierAttestation(String numAffilie, String numNss)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(numAffilie) && JadeStringUtil.isEmpty(numNss)) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#searchDossierAttestation : numAffilie anf numNSS are both empty or nulll");
        }
        DossierAttestationVersementComplexSearchModel dossierAttestationVersement = new DossierAttestationVersementComplexSearchModel();
        // si numNss est null on prend l'affilie
        if (JadeStringUtil.isEmpty(numNss)) {
            dossierAttestationVersement.setForNumAffilie(numAffilie);
        }
        // si le numAffilie est null on prend le nss
        else if (JadeStringUtil.isEmpty(numAffilie)) {
            dossierAttestationVersement.setForNssAllocataire(numNss);

        }
        // on prend les deux
        else {
            dossierAttestationVersement.setForNumAffilie(numAffilie);
            dossierAttestationVersement.setForNssAllocataire(numNss);
        }
        dossierAttestationVersement.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        (JadePersistenceManager.search(dossierAttestationVersement)).getSearchResults();

        // ALImplServiceLocator.getDossierBusinessService().(JadePersistenceManager.search(dossierAttestationVersement)).getSearchResults();

        return dossierAttestationVersement;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService# updateDossierEtEnvoyeAnnoncesRafam
     * (ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public DossierComplexModel updateDossier(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#updateDossierEtEnvoiAnnoncesRafam : dossier is null");
        }

        try {
            if ((etatDossierAvantModif != null)
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ETAT, etatDossierAvantModif)) {
                throw new ALDossierBusinessException(
                        "DossierBusinessServiceImpl#updateDossierEtEnvoiAnnoncesRafam : etatDossierAvantModif is not a valid value");
            }

        } catch (Exception e) {
            throw new ALDossierBusinessException(
                    "DossierBusinessServiceImpl#updateDossierEtEnvoiAnnoncesRafam : unable to check code system etatDossierAvantModif",
                    e);
        }

        journaliserChangementEtat(dossier, etatDossierAvantModif, remarqueJournalisation);
        dossier = ALServiceLocator.getDossierComplexModelService().update(dossier);

        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.MODIF_DOSSIER, dossier);

        return dossier;
    }

    @Override
    public DossierComplexModel updateDossier(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation, DossierAgricoleComplexModel dossierAgricole)
            throws JadeApplicationException, JadePersistenceException {
        DossierComplexModel dossierComplexModel = this.updateDossier(dossier, etatDossierAvantModif,
                remarqueJournalisation);

        boolean isAgricoleContext = ALServiceLocator.getDossierBusinessService().isAgricole(
                dossier.getDossierModel().getActiviteAllocataire());

        if (isAgricoleContext) {

            if (!dossierAgricole.getAllocataireAgricoleComplexModel().getAgricoleModel().isNew()) {

                if (ALCSDossier.ACTIVITE_PECHEUR.equals(dossier.getDossierModel().getActiviteAllocataire())) {
                    dossierAgricole.getAllocataireAgricoleComplexModel().getAgricoleModel().setDomaineMontagne(false);
                }

                ALImplServiceLocator.getAgricoleModelService().update(
                        dossierAgricole.getAllocataireAgricoleComplexModel().getAgricoleModel());
            }
        }

        return dossierComplexModel;
    }

    @Override
    public DossierComplexSearchModel getDossiersActifs(String date, String idTiersAllocataire)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { "date" }));
        }

        if (!JadeNumericUtil.isIntegerPositif(idTiersAllocataire)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.checkParam.common.param.NaN",
                            new String[] { "paramName", idTiersAllocataire }));
        }

        DossierComplexSearchModel search = new DossierComplexSearchModel();
        search.setForIdTiersAllocataire(idTiersAllocataire);
        search.setForDebutValidite(date);
        search.setForFinValidite(date);
        search.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_ACTIF);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        search = (DossierComplexSearchModel) JadePersistenceManager.search(search);

        return search;
    }

    @Override
    public int countDossiersActifs(String date, String idTiersAllocataire) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#countDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { "date" }));
        }

        if (!JadeNumericUtil.isIntegerPositif(idTiersAllocataire)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.checkParam.common.param.NaN",
                            new String[] { "paramName", idTiersAllocataire }));
        }

        DossierComplexSearchModel search = new DossierComplexSearchModel();
        search.setForIdTiersAllocataire(idTiersAllocataire);
        search.setForDebutValidite(date);
        search.setForFinValidite(date);
        search.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_ACTIF);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        int nbDossiers = JadePersistenceManager.count(search);

        return nbDossiers;
    }
}
