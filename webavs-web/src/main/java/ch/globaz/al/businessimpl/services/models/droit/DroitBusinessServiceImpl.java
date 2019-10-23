package ch.globaz.al.businessimpl.services.models.droit;

import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAction;
import ch.globaz.al.business.models.droit.*;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.common.domaine.Montant;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.droit.ALEnumMsgDroitPC;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.models.tarif.EcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.droit.DroitBusinessService;
import ch.globaz.al.businessimpl.ctrlexport.PrestationExportableController;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dossier.DossierRCListSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Implémentation des services métier liés au droit
 * 
 * @author gmo
 * 
 */
public class DroitBusinessServiceImpl implements DroitBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.droit.DroitBusinessService#
     * ajoutDroitMemeType(ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public void ajoutDroitMemeType(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException {

        if (droitComplexModel == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#ajoutDroitMemeType: droitComplexModel is null");

        }

        // le type de droit doit être enfant ou formation
        if ((JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_ENF, true) || JadeStringUtil
                .equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_FORM, true))) {

            DroitModel droitModel = droitComplexModel.getDroitModel();
            // rechercher le dossier lié au droit
            DossierComplexModel dossierComplex = ALServiceLocator.getDossierBusinessService()
                    .getDossierComplexForDroit(droitComplexModel);

            // récupérer la date de début de validité du premier droit
            String dateDebutValiditeDroit1 = droitModel.getDebutDroit();

            // récupérer la catégorie de résident
            String catResident = ALServiceLocator.getAllocataireBusinessService().getTypeResident(
                    dossierComplex.getAllocataireComplexModel().getAllocataireModel());
            // récupérer la catégorie de tarif
            String categorieTarif = null;
            if (!JadeStringUtil.isBlankOrZero(droitComplexModel.getDroitModel().getTarifForce())) {
                categorieTarif = droitComplexModel.getDroitModel().getTarifForce();
            } else if (!JadeStringUtil.isBlankOrZero(dossierComplex.getDossierModel().getTarifForce())) {
                categorieTarif = dossierComplex.getDossierModel().getTarifForce();

            } else {

                // récupération du canton de l'affilié
                String cantonAffilie = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                        ALServiceLocator.getAffiliationBusinessService()
                                .getAssuranceInfo(dossierComplex.getDossierModel(), droitModel.getDebutDroit())
                                .getCanton());

                // récupération du tarif du canton
                categorieTarif = ALImplServiceLocator.getCalculService().getTarifForCanton(cantonAffilie);
            }

            // récupération de la capacité d'exercer de l'enfant
            Boolean capExercer = droitComplexModel.getEnfantComplexModel().getEnfantModel().getCapableExercer();

            ArrayList<EcheanceComplexModel> listeEcheance = new ArrayList<EcheanceComplexModel>();
            // rechercher la liste des échéances (âge début et âge fin) pour le
            // droit
            listeEcheance = ALImplServiceLocator.getDatesEcheancePrivateService().getDebutFinValiditeEcheance(
                    droitModel.getTypeDroit(), categorieTarif, catResident, dateDebutValiditeDroit1, capExercer);

            if (listeEcheance.size() > 1) {

                // créer les droits si nécessaire
                setNewEcheance(droitComplexModel, listeEcheance, droitComplexModel.getEnfantComplexModel()
                        .getPersonneEtendueComplexModel().getPersonne().getDateNaissance());

            }
        }
    }

    /**
     * Vérifie si la date de fin de droit forcée à changé et active le booléen "imprimer échéance" si c'est le cas
     * 
     * @param droit
     *            Le droit à vérifier
     * 
     * @return le droit mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private DroitModel annoncerEcheanceSiNecessaire(DroitModel droit) throws JadeApplicationException,
            JadePersistenceException {
        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#annoncerEcheanceSiNecessaire : droit is null");
        }

        DroitModel droitInDB = ALImplServiceLocator.getDroitModelService().read(droit.getId());
        if (!JadeDateUtil.areDatesEquals(droitInDB.getFinDroitForcee(), droit.getFinDroitForcee())) {
            droit.setImprimerEcheance(true);
        }

        return droit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService#checkPCFamille(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ALEnumMsgDroitPC checkPCFamille(String nssAllocataire, String nssEnfant) throws JadeApplicationException,
            JadePersistenceException {

        // Recherche dans les PC
        DossierRCListSearch dossierSearch = new DossierRCListSearch();
        dossierSearch.setWhereKey(DossierRCListSearch.SEARCH_DEFINITION_RECHERCHE_FAMILLE);
        dossierSearch.setLikeNss(nssEnfant);
        dossierSearch.setLikeNom("");
        dossierSearch.setLikePrenom("");
        dossierSearch = PegasusServiceLocator.getDossierService().searchRCList(dossierSearch);

        if (0 != dossierSearch.getSize()) {
            return ALEnumMsgDroitPC.enfantBeneficiePC;
        }

        dossierSearch.setLikeNss(nssAllocataire);
        dossierSearch = PegasusServiceLocator.getDossierService().searchRCList(dossierSearch);
        if (0 != dossierSearch.getSize()) {
            return ALEnumMsgDroitPC.allocataireBeneficiePC;
        }

        return null;
    }

    @Override
    public void copierDroitAndUpdateAttestationDate(String idDroitSource, String dateAttestationDroitSource,
            String newDateDebut, String newDateFin) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idDroitSource)) {
            throw new DroitException("DroitBusinessServiceImple#copieDroit: idDroitSource n'est pas valide");
        }

        if (!JadeNumericUtil.isZeroValue(dateAttestationDroitSource)
                && !JadeDateUtil.isGlobazDate(dateAttestationDroitSource)) {
            throw new DroitException(
                    "DroitBusinessServiceImple#copieDroit: dateAttestationDroitSource n'est pas une date valide");
        }

        if (!JadeDateUtil.isGlobazDate(newDateDebut)) {
            throw new DroitException("DroitBusinessServiceImple#copieDroit: la date de début n'est pas une date valide");
        }
        if (!JadeDateUtil.isGlobazDate(newDateFin)) {
            throw new DroitException("DroitBusinessServiceImple#copieDroit: la date de fin n'est pas une date valide");
        }

        DroitComplexModel droitSource = ALServiceLocator.getDroitComplexModelService().read(idDroitSource);

        // Gestion du nouveau droit(copié)
        JadeBusinessMessage[] initialMessages = JadeThread.logMessages();
        int initialSize = (initialMessages == null) ? 0 : initialMessages.length;
        JadeThread.logClear();

        DroitComplexModel newDroit = ALServiceLocator.getDroitComplexModelService().clone(droitSource,
                droitSource.getDroitModel().getIdDossier());
        newDroit.getDroitModel().setDebutDroit(newDateDebut);

        // règles des dates échéances et attestation
        if (JadeDateUtil.isGlobazDate(dateAttestationDroitSource)) {
            // si date de fin saisie > date attestation
            if (JadeDateUtil.isDateAfter(newDateFin, dateAttestationDroitSource)) {
                newDroit.getDroitModel().setFinDroitForcee(dateAttestationDroitSource);
            } else {
                newDroit.getDroitModel().setFinDroitForcee(newDateFin);
            }
            newDroit.getDroitModel().setDateAttestationEtude(dateAttestationDroitSource);
        } else {
            String echeanceCalcule = ALServiceLocator.getDatesEcheanceService().getDateFinValiditeDroitCalculee(
                    newDroit);
            if (JadeDateUtil.isDateAfter(newDateFin, echeanceCalcule)) {
                newDroit.getDroitModel().setFinDroitForcee(echeanceCalcule);
            } else {
                newDroit.getDroitModel().setFinDroitForcee(newDateFin);
            }
        }

        newDroit = updateImprimerEcheanceState(newDroit);

        // ctrl chevauchement droit
        if (ALServiceLocator.getDroitBusinessService().hasChevauchementDate(newDroit.getDroitModel(), true)) {
            JadeThread.logError(DroitBusinessServiceImpl.class.getName(),
                    "al.droit.droitModel.copieDroitService.businessIntegrity.chevauchement");
        } else {
            // création du droit et des annonces RAFAM si nécessaire
            ALServiceLocator.getDroitBusinessService().createDroitEtEnvoyeAnnoncesRafam(newDroit, true);
        }
        JadeBusinessMessage[] newMessages = JadeThread.logMessages();
        JadeThread.logClear();

        if (newMessages != null) {
            for (JadeBusinessMessage message : newMessages) {
                // message.getLevel();

                JadeThread.logError(DroitBusinessServiceImpl.class.getName(),
                        "al.droit.droitModel.copieDroitService.businessIntegrity.prefixMsgError", new String[] {
                                JadeCodesSystemsUtil.getCodeLibelle(droitSource.getDroitModel().getTypeDroit()),
                                droitSource.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                                        .getDesignation2(), message.getContents(JadeThread.currentLanguage()) });
            }
        }

    }

    private DroitComplexModel updateImprimerEcheanceState(DroitComplexModel newDroit) {

        String echeanceDroit = newDroit.getDroitModel().getFinDroitForcee();
        String now = JadeDateUtil.getGlobazFormattedDate(new Date());

        boolean imprimerEcheance = JadeDateUtil.isDateBefore(now, echeanceDroit);
        newDroit.getDroitModel().setImprimerEcheance(imprimerEcheance);

        return newDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService#
     * copieToFormation(ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel copieToFormation(DroitComplexModel refDroitComplexModel) throws JadeApplicationException,
            JadePersistenceException {
        if (refDroitComplexModel == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#copieToFormation : droit is null");
        }

        if (!ALCSDroit.TYPE_ENF.equals(refDroitComplexModel.getDroitModel().getTypeDroit())) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#copieToFormation : droit source is not ENF");
        }

        // on enlève la coche du droit ENF de base
        refDroitComplexModel.getDroitModel().setImprimerEcheance(Boolean.FALSE);
        ALServiceLocator.getDroitComplexModelService().update(refDroitComplexModel);

        DroitComplexModel newDroitComplexModel = new DroitComplexModel();
        // Clonage du droit
        newDroitComplexModel = ALServiceLocator.getDroitComplexModelService().clone(refDroitComplexModel,
                refDroitComplexModel.getDroitModel().getIdDossier());

        newDroitComplexModel.getDroitModel().setTypeDroit(ALCSDroit.TYPE_FORM);
        newDroitComplexModel.getDroitModel().setMotifFin(ALCSDroit.MOTIF_FIN_ECH);

        newDroitComplexModel.getDroitModel().setImprimerEcheance(Boolean.TRUE);

        newDroitComplexModel.getDroitModel().setDebutDroit(
                ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(
                        refDroitComplexModel.getDroitModel().getFinDroitForcee()));
        newDroitComplexModel.getDroitModel().setFinDroitForcee("");

        newDroitComplexModel
                .getEnfantComplexModel()
                .getEnfantModel()
                .setAllocationNaissanceVersee(
                        refDroitComplexModel.getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee());
        newDroitComplexModel
                .getEnfantComplexModel()
                .getEnfantModel()
                .setTypeAllocationNaissance(
                        refDroitComplexModel.getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance());

        return newDroitComplexModel;
    }

    @Override
    public int countEnfantsInDroitsList(ArrayList<String> listIdDroits) throws JadeApplicationException,
            JadePersistenceException {

        if (listIdDroits == null) {
            throw new ALDroitBusinessException(
                    "DroitBusinessServiceImpl#countEnfantsInDroitsList : listIdDroits is null");
        }

        ArrayList<String> idEnfantsFound = new ArrayList<String>();
        for (String idDroit : listIdDroits) {
            DroitModel currentDroit = ALImplServiceLocator.getDroitModelService().read(idDroit);
            if (!JadeNumericUtil.isEmptyOrZero(currentDroit.getIdEnfant())
                    && !idEnfantsFound.contains(currentDroit.getIdEnfant())) {
                idEnfantsFound.add(currentDroit.getIdEnfant());
            }
        }

        return idEnfantsFound.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService# createDroitEtEnvoyeAnnoncesRafam
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel createDroitEtEnvoyeAnnoncesRafam(DroitComplexModel droit, boolean onlyDroitModel)
            throws JadeApplicationException, JadePersistenceException {
        if (onlyDroitModel) {

            DroitModel droitSimpleModel = ALImplServiceLocator.getDroitModelService().create(droit.getDroitModel());
            droit.setDroitModel(droitSimpleModel);

        } else {
            droit = ALServiceLocator.getDroitComplexModelService().create(droit);
        }
        if (!droit.getDroitModel().isNew()) {
            ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.CREATION, droit);

            // propagation pour la FPV
            if (ALConstCaisse.CAISSE_FPV.equals(ALServiceLocator.getParametersServices().getNomCaisse())) {
                ALServiceLocator.getGedBusinessService().propagateDroitForGEDFPV(droit);
            }
        }

        return droit;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.droit.DroitBusinessService# ctrlDroitExportabilite
     * (ch.globaz.al.business.models.droit.DroitComplexModel, java.lang.String)
     */
    @Override
    public boolean ctrlDroitExportabilite(DroitComplexModel droitComplexModel, String date)
            throws JadePersistenceException, JadeApplicationException {
        if (droitComplexModel == null) {
            throw new ALDroitBusinessException(
                    "DroitBusinessServiceImpl#ctrlDroitExportabilite : droitComplexModel is null");
        }
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDroitBusinessException(
                    "DroitBusinessServiceImpl#ctrlDroitExportabilite: date is not a globaz date");
        }

        DossierComplexModel dossierComplexModel = ALServiceLocator.getDossierComplexModelService().read(
                droitComplexModel.getDroitModel().getIdDossier());

        if (!PrestationExportableController.isCountriesLoaded()) {
            PrestationExportableController.loadEuropeCountries();
        }
        if (!PrestationExportableController.isRulesLoaded()) {
            PrestationExportableController.loadRules();
        }

        return new PrestationExportableController().control(dossierComplexModel, droitComplexModel, date);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService#
     * defineDebutDroit(ch.globaz.al.business.models.droit.DroitComplexModel, java.lang.String)
     */
    @Override
    public DroitComplexModel defineDebutDroit(DroitComplexModel droitComplexModel, String dateDebutDossier)
            throws JadeApplicationException, JadePersistenceException {
        if (droitComplexModel == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#defineDebutDroit : droitComplexModel is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebutDossier)) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#defineDebutDroit : " + dateDebutDossier
                    + " is not a valid date");
        }

        droitComplexModel.getDroitModel().setDebutDroit(
                ALServiceLocator.getDatesEcheanceService().getDateDebutValiditeDroit(droitComplexModel,
                        dateDebutDossier));

        return droitComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService# deleteDroitEtEnvoyeAnnoncesRafam
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel deleteDroit(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSent(droit.getId());

        return ALServiceLocator.getDroitComplexModelService().delete(droit);
    }

    @Override
    public boolean hasChevauchementDate(DroitModel droit, boolean sameDossier) throws JadeApplicationException,
            JadePersistenceException {
        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#hasChevauchementDate : droit is null");
        }

        DroitComplexSearchModel searchForChevauchement = new DroitComplexSearchModel();
        searchForChevauchement.setForDebutDroit(droit.getDebutDroit());
        searchForChevauchement.setForFinDroitForcee(droit.getFinDroitForcee());
        List<String> types = new ArrayList<String>();
        types.add(droit.getTypeDroit());
        searchForChevauchement.setInTypeDroit(types);

        searchForChevauchement.setForIdEnfant(droit.getIdEnfant());
        if (sameDossier) {
            searchForChevauchement.setForIdDossier(droit.getIdDossier());
        }
        searchForChevauchement.setWhereKey(DroitComplexSearchModel.SEARCH_DROIT_PLAGE_INCLUSE);
        searchForChevauchement = ALServiceLocator.getDroitComplexModelService().search(searchForChevauchement);
        // si on a trouvé un seul droit, on vérifie si c'est notre droit référence, si oui => pas d'autres droits
        // chevauchant trouvé => false
        if (searchForChevauchement.getSize() == 1) {
            if (droit.getIdDroit().equals(
                    ((DroitComplexModel) searchForChevauchement.getSearchResults()[0]).getDroitModel().getIdDroit())) {
                return false;
            } else {
                // le seul droit trouvé peut être différent, car droit référence pas forcément déjà en DB
                return true;
            }
        }
        return ALServiceLocator.getDroitComplexModelService().search(searchForChevauchement).getSize() > 0 ? true
                : false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService#hasPrestations(java.lang.String)
     */
    @Override
    public boolean hasPrestations(String idDroit) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#hasPrestations : droit is not defined");
        }

        DetailPrestationSearchModel dps = new DetailPrestationSearchModel();
        dps.setForIdDroit(idDroit);
        return ALImplServiceLocator.getDetailPrestationModelService().count(dps) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService#isDroitActif
     * (ch.globaz.al.business.models.droit.DroitModel, java.lang.String)
     */
    @Override
    public boolean isDroitActif(DroitModel droit, String date) throws JadeApplicationException {

        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#isDroitActif : droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#isDroitActif : " + date
                    + " is not a valid date");
        }

        return ((ALCSDroit.ETAT_A.equals(droit.getEtatDroit()) || ALCSDroit.ETAT_G.equals(droit.getEtatDroit())) && ((JadeStringUtil
                .isEmpty(droit.getFinDroitForcee()) && !JadeDateUtil.isDateBefore(date, droit.getDebutDroit())) || (!JadeStringUtil
                .isEmpty(droit.getFinDroitForcee()) && ALDateUtils.isDateBetween(date,
                ALDateUtils.getDateDebutMois(droit.getDebutDroit()),
                ALDateUtils.getDateFinMois(droit.getFinDroitForcee())))));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.droit.DroitBusinessService#isDroitInactif(ch.globaz.al.business.models.
     * droit.DroitModel)
     */
    @Override
    public boolean isDroitInactif(DroitModel droit) throws JadeApplicationException {

        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#isDroitInactif : droit is null");
        }

        return ALCSDroit.ETAT_G.equals(droit.getEtatDroit());
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.droit.DroitBusinessService# isEcheanceOverLimiteLegale
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public boolean isEcheanceOverLimiteLegale(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        boolean result = false;

        if (ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                || ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit())) {
            String dateNaissance = droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                    .getDateNaissance();

            String echeance = droit.getDroitModel().getFinDroitForcee();

            String moisNaissance = dateNaissance.substring(3, 5);
            String moisEcheance = echeance.substring(3, 5);
            // si echeance - naissance ==16 et que moisNaissance != moisEcheance
            // => mois suivant les 16 ans de l'enfant (sans le test sur les
            // mois,
            // on aura le mois de ses 16 ans et on doit laisser passer
            if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit()) && (JadeDateUtil.getNbYearsBetween(
                    dateNaissance, echeance, JadeDateUtil.YEAR_MONTH_COMPARISON) == 16))
                    && !moisNaissance.equals(moisEcheance)) {

                result = true;

            }
            // si echeance - naissance >16
            else if (ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                    && (JadeDateUtil.getNbYearsBetween(dateNaissance, echeance, JadeDateUtil.YEAR_MONTH_COMPARISON) > 16)) {
                result = true;
            }
            // si echeance - naissance ==24 et que moisNaissance != moisEcheance
            // => mois suivant les 25 ans de l'enfant (sans le test sur les
            // mois,
            // on aura le mois de ses 25 ans et on doit laisser passer
            else if (ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit())
                    && (JadeDateUtil.getNbYearsBetween(droit.getEnfantComplexModel().getPersonneEtendueComplexModel()
                            .getPersonne().getDateNaissance(), droit.getDroitModel().getFinDroitForcee(),
                            JadeDateUtil.YEAR_MONTH_COMPARISON) == 25) && !moisNaissance.equals(moisEcheance)) {

                result = true;

            } else if (ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit())
                    && (JadeDateUtil.getNbYearsBetween(dateNaissance, echeance, JadeDateUtil.YEAR_MONTH_COMPARISON) > 25)) {
                result = true;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.droit.DroitBusinessService# isFormationAnticipee
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public boolean isFormationAnticipee(DroitComplexModel droit) throws JadeApplicationException {

        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#isFormationAnticipee : droit is null");
        }

        if (!ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit())) {
            return false;
        } else {
            return JadeDateUtil.isDateBefore(droit.getDroitModel().getDebutDroit(), ALServiceLocator
                    .getDatesEcheanceService().getDateFinValiditeDroitCalculeeFormAnticipe(droit));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.droit.DroitBusinessService#isMontantForceZero(ch.globaz.al.business.models
     * .droit.DroitModel)
     */
    @Override
    public boolean isMontantForceZero(DroitModel droit) {
        return droit.getForce() && JadeNumericUtil.isZeroValue(droit.getMontantForce());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService#isTypeMenage
     * (ch.globaz.al.business.models.droit.DroitModel)
     */
    @Override
    public boolean isTypeMenage(DroitModel droit) throws JadeApplicationException {

        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#isTypeMenage : droit is null");
        }

        return ALCSDroit.TYPE_MEN.equals(droit.getTypeDroit());
    }

    @Override
    public ArrayList<AnnonceRafamModel> readWidget_apercuAnnoncesRafam(HashMap<?, ?> values)
            throws JadeApplicationException, JadePersistenceException {

        // Préparation de l'arraylist
        ArrayList<AnnonceRafamModel> annoncesForDroit = new ArrayList<AnnonceRafamModel>();

        String dateDebut = values.get("dateDebut").toString();
        String dateFin = values.get("dateFin").toString();
        String dateNaissance = values.get("dateNaissance").toString();
        String idDroit = values.get("idDroit").toString();

        DroitComplexModel droit = ALServiceLocator.getDroitComplexModelService().read(idDroit);
        droit.getDroitModel().setDebutDroit(dateDebut);
        droit.getDroitModel().setFinDroitForcee(dateFin);
        // FIXME:pas nécessaire car par éditable
        // droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().setDateNaissance(dateNaissance);

        // TODO: ne doit pas effacer celles à transmettre, car si on annule ensuite, il faut que à transmettre soit
        // toujours là
        // donc service creerAnnonces temporaires (enregistrés)
        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.MODIF_DROIT,
                RafamEtatAnnonce.ENREGISTRE, droit);

        // recherche sur les annonces créées pour obtenir tous les types d'annonces (ENF, NAIS par ex)
        AnnonceRafamSearchModel searchJustCreated = new AnnonceRafamSearchModel();
        searchJustCreated.setForEtatAnnonce(RafamEtatAnnonce.ENREGISTRE.getCS());
        searchJustCreated.setForIdDroit(idDroit);
        searchJustCreated = ALServiceLocator.getAnnonceRafamModelService().search(searchJustCreated);

        ArrayList<RafamFamilyAllowanceType> typeDroitsCreated = new ArrayList<RafamFamilyAllowanceType>();

        // on stocke les annonces créées et les types de droits de ces annonces
        for (int i = 0; i < searchJustCreated.getSize(); i++) {

            AnnonceRafamModel currentAnnonce = ((AnnonceRafamModel) searchJustCreated.getSearchResults()[i]);
            annoncesForDroit.add(currentAnnonce);
            typeDroitsCreated.add(RafamFamilyAllowanceType.getFamilyAllowanceType(currentAnnonce.getGenrePrestation()));
        }

        ArrayList<String> etats = new ArrayList<String>();

        etats.add(RafamEtatAnnonce.RECU.getCS());
        etats.add(RafamEtatAnnonce.SUSPENDU.getCS());
        etats.add(RafamEtatAnnonce.TRANSMIS.getCS());
        etats.add(RafamEtatAnnonce.VALIDE.getCS());
        // on récupère la dernière annonce active recue,suspendue,transmise ou validée du droit pour chaque type de
        // prestation générée dans les annonces "enregistrées"
        for (RafamFamilyAllowanceType currentType : typeDroitsCreated) {
            annoncesForDroit.add(ALImplServiceLocator.getAnnoncesRafamSearchService().getLastActive(idDroit,
                    currentType, etats));
        }

        return annoncesForDroit;

    }

    @Override
    public void readWidget_deleteAnnoncesTemporaires(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException {

        String idDroit = values.get("idDroit").toString();

        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteForEtat(idDroit, RafamEtatAnnonce.ENREGISTRE);

    }

    @Override
    public void readWidget_validerAnnoncesTemporaires(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException {
        // TODO: mettre le recordNumber sélectionné à l'annonce correspondante (0 si nouvelle)
        // - appeler service
        // ALServiceLocator.getAnnonceRafamCreationService().transmettreAnnoncesTemporaires(idDroit);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService# removeBeneficiaire (ch . globaz .al .
     * business . models . droit . DroitModel )
     */
    @Override
    public void removeBeneficiaire(DroitModel droit) throws JadeApplicationException, JadePersistenceException {

        if (!(droit instanceof DroitModel)) {
            throw new ALDroitBusinessException(
                    "DroitBusinessServiceImpl#removeBeneficiaire : The model is not an instance of DroitModel");
        }

        // On modifie le droit que si nécessaire
        if (!JadeNumericUtil.isEmptyOrZero(droit.getIdTiersBeneficiaire())) {
            droit.setIdTiersBeneficiaire("0");

            droit = ALImplServiceLocator.getDroitModelService().update(droit);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.droit.DroitBusinessService# setDateFinDroitForce
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel setDateFinDroitForce(DroitComplexModel model) throws JadeApplicationException {

        if (model == null) {
            throw new ALDroitBusinessException("DroitBusinessSerrviceImpl#updateDateFinDroitForce : model is null");
        }

        String echeanceCalculee = ALServiceLocator.getDatesEcheanceService().getDateFinValiditeDroitCalculee(model);

        // si la date de fin de droit est vide, on la met à jour
        if (JadeStringUtil.isEmpty(model.getDroitModel().getFinDroitForcee())) {
            model.getDroitModel().setFinDroitForcee(echeanceCalculee);
        }

        return model;
    }

    /**
     * Méthode qui crée les droits selon l'âge des échéances
     * 
     * @param droitModel
     *            droit créer initialement
     * @param dateNaissanceEnfant
     *            date de naissance de l'enfant
     * @param listEcheancesAge
     *            liste d'échéance des âge pour les tarifs des droits
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void setNewEcheance(DroitComplexModel droitModel, ArrayList<EcheanceComplexModel> listEcheancesAge,
            String dateNaissanceEnfant) throws JadeApplicationException, JadePersistenceException {

        if (droitModel == null) {
            throw new ALDroitBusinessException("DroitBusinessSerrviceImpl#setNewEcheance : droitModel is null");
        }

        Iterator<EcheanceComplexModel> iter = listEcheancesAge.iterator();
        EcheanceComplexModel echeance = new EcheanceComplexModel();

        while (iter.hasNext()) {
            // calcul de l'échéance pour le premier "droit" de la liste des
            // échéances
            echeance = iter.next();
            // calcul de l'échéance(echeanceCalcule)pour le droit passé en
            // paramètre
            String finValiditeEcheance = ALImplServiceLocator.getDatesEcheancePrivateService()
                    .calculFinValiditeEcheance(droitModel.getDroitModel().getDebutDroit(), echeance,
                            dateNaissanceEnfant);
            // nouveau droit
            DroitModel newDroit = new DroitModel();

            // si la date de l'échéance forcée est postérieure à l'échéance
            // calculée
            if (JadeDateUtil.isDateAfter(droitModel.getDroitModel().getFinDroitForcee(), finValiditeEcheance)) {
                newDroit = ALImplServiceLocator.getDroitModelService().copie(droitModel.getDroitModel());

                // affecter la date d'échéanceForce au nouveau droit par le
                // calcul
                newDroit.setFinDroitForcee(finValiditeEcheance);

                // affectation du motif de fin droit pour le nouveau droit

                newDroit.setMotifFin(ALCSDroit.MOTIF_FIN_CTAR);

                // on vérifie que le droit n'existe pas encore

                DroitSearchModel searchDroit = new DroitSearchModel();

                searchDroit.setForIdDossier(newDroit.getIdDossier());
                searchDroit.setForDateEcheanceForcee(newDroit.getFinDroitForcee());
                searchDroit.setForIdEnfant(newDroit.getIdEnfant());
                searchDroit.setForTypeDroit(newDroit.getTypeDroit());

                if (ALImplServiceLocator.getDroitModelService().count(searchDroit) == 0) {

                    // si existe pas on le crée, on le crée seulement si on le crés seulement si la date du
                    // début du
                    // droit est antérieure à la date de fin du droit

                    if (JadeDateUtil.isDateBefore(newDroit.getDebutDroit(), newDroit.getFinDroitForcee())) {
                        newDroit = ALImplServiceLocator.getDroitModelService().create(newDroit);

                        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.CREATION,
                                ALServiceLocator.getDroitComplexModelService().read(newDroit.getIdDroit()));

                        // on modifie la date du début de validité du droitModel
                        // (droit initial)
                        droitModel.getDroitModel().setDebutDroit(
                                ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(
                                        newDroit.getFinDroitForcee()));
                    } else {
                        droitModel.getDroitModel().setDebutDroit(
                                ALServiceLocator.getDossierModelService()
                                        .read(droitModel.getDroitModel().getIdDossier()).getDebutValidite());
                    }

                    // modification de l'échéance du droit initial
                    droitModel.getDroitModel().setMotifFin(ALCSDroit.MOTIF_FIN_ECH);
                    // update du droit initial

                    if (droitModel.isNew()) {
                        droitModel.setDroitModel(ALImplServiceLocator.getDroitModelService().create(
                                droitModel.getDroitModel()));

                        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(
                                RafamEvDeclencheur.CREATION,
                                ALServiceLocator.getDroitComplexModelService().read(
                                        droitModel.getDroitModel().getIdDroit()));
                    } else {
                        droitModel.setDroitModel(ALImplServiceLocator.getDroitModelService().update(
                                droitModel.getDroitModel()));
                        // ev Decl création car cas de modification d'un droit qui vient d'être créé
                        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(
                                RafamEvDeclencheur.CREATION,
                                ALServiceLocator.getDroitComplexModelService().read(
                                        droitModel.getDroitModel().getIdDroit()));
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitBusinessService# updateDroitEtEnvoyeAnnoncesRafam
     * (ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public DroitComplexModel updateDroitEtEnvoyeAnnoncesRafam(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        if (droit == null) {
            throw new ALDroitBusinessException("DroitBusinessServiceImpl#updateDateFinDroitForce : droit is null");
        }
        // si modification d'échéance on reconsidère le droit à annoncer
        droit.setDroitModel(annoncerEcheanceSiNecessaire(droit.getDroitModel()));

        droit = ALServiceLocator.getDroitComplexModelService().update(droit);

        ALServiceLocator.getAnnonceRafamCreationService().creerAnnoncesSelonPrecedent(RafamEvDeclencheur.MODIF_DROIT, null, droit);

        return droit;
    }

}