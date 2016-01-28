/**
 * 
 */
package ch.globaz.amal.businessimpl.services.annonce;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.pyxis.constantes.IConstantes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.annonce.AnnonceService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class AnnonceServiceImpl implements AnnonceService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.annonce.AnnonceService#createAnnoncesSimulationJobs(java.lang.String,
     * java.util.List)
     */
    @Override
    public void createAnnoncesSimulationJobs(String idTiersGroupe, List<String> idTiersCM) {

        // ---------------------------------------------------------------
        // 1) Récupération d'informations utiles
        // ---------------------------------------------------------------

        // Récupération de la date du jour
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String csDateDuJourShort = sdf.format(date);
        // Récupération des informations utilisateur
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        String currentUser = currentSession.getUserId();

        // ------------------------------------------------------------------------
        // 2) si groupe, récupérations des idtiers individuels des caisses-maladies
        // ------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(idTiersGroupe)) {
            idTiersCM = new ArrayList<String>();
            CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
            cmSearch.setForIdTiersGroupe(idTiersGroupe);
            cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
            cmSearch.setWhereKey("rcListeOnlyActifs");
            cmSearch.setDefinedSearchSize(0);
            try {
                cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                for (int iCm = 0; iCm < cmSearch.getSize(); iCm++) {
                    CaisseMaladie currentCM = (CaisseMaladie) cmSearch.getSearchResults()[iCm];
                    if (!idTiersCM.contains(currentCM.getIdTiersCaisse())) {
                        idTiersCM.add(currentCM.getIdTiersCaisse());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ------------------------------------------------------------------------
        // 3) création des jobs
        // ------------------------------------------------------------------------
        // Création des jobs pour chaque cm, status in progress
        // Ce sont des récipiens vides, que le process annonce mettra à jour
        Iterator<String> cmIterator = idTiersCM.iterator();
        while (cmIterator.hasNext()) {
            String currentIdTiersCM = cmIterator.next();
            // Recherche si un job annonce, in progress, id tiers est déjà existant
            SimpleControleurJobSearch searchJob = new SimpleControleurJobSearch();
            searchJob.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
            searchJob.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
            searchJob.setForSubTypeJob(currentIdTiersCM);
            try {
                searchJob = AmalImplServiceLocator.getSimpleControleurJobService().search(searchJob);
                if (searchJob.getSize() == 0) {
                    SimpleControleurJob newJob = new SimpleControleurJob();
                    newJob.setDateJob(csDateDuJourShort);
                    newJob.setDescriptionJob("Annonce CM : " + currentIdTiersCM);
                    newJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
                    newJob.setSubTypeJob(currentIdTiersCM);
                    newJob.setTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
                    newJob.setUserJob(currentUser);
                    try {
                        AmalImplServiceLocator.getSimpleControleurJobService().create(newJob);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.annonce.AnnonceService#getAnnoncesToCreate(java.lang.String,
     * java.util.List, java.lang.String)
     */
    @Override
    public HashMap<String, List<String>> getAnnoncesToCreate(String idTiersGroupe, List<String> idTiersCM,
            String anneeHistorique) {

        HashMap<String, List<String>> returnedMap = new HashMap<String, List<String>>();

        // ------------------------------------------------------------------------
        // 0) si groupe, récupérations des idtiers individuels des caisses-maladies
        // ------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(idTiersGroupe)) {
            idTiersCM = new ArrayList<String>();
            CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
            cmSearch.setForIdTiersGroupe(idTiersGroupe);
            cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
            cmSearch.setWhereKey("rcListeOnlyActifs");
            cmSearch.setDefinedSearchSize(0);
            try {
                cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                for (int iCm = 0; iCm < cmSearch.getSize(); iCm++) {
                    CaisseMaladie currentCM = (CaisseMaladie) cmSearch.getSearchResults()[iCm];
                    if (!idTiersCM.contains(currentCM.getIdTiersCaisse())) {
                        idTiersCM.add(currentCM.getIdTiersCaisse());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // ------------------------------------------------------------------------
        // 1) Récupération de tous les documents qui génère une annonce
        // ------------------------------------------------------------------------
        List<String> docGenereAnnonce = new ArrayList<String>();

        ParametreModelComplexSearch modelSearch = new ParametreModelComplexSearch();
        modelSearch.setWhereKey("basic");
        modelSearch.setDefinedSearchSize(0);
        try {
            modelSearch = AmalServiceLocator.getParametreModelService().search(modelSearch);
            for (int iModele = 0; iModele < modelSearch.getSize(); iModele++) {
                ParametreModelComplex paramModel = (ParametreModelComplex) modelSearch.getSearchResults()[iModele];
                if (paramModel.getSimpleParametreModel().getCodeAnnonceCaisse()) {
                    docGenereAnnonce.add(paramModel.getFormuleList().getDefinitionformule().getCsDocument());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (docGenereAnnonce.size() < 1) {
            return returnedMap;
        }

        // --------------------------------------------------
        // 2) récupération des id detail famille à traiter
        // --------------------------------------------------
        boolean needTreatment = true;
        int iOffset = 0;
        SimpleDetailFamilleSearch currentSearch = new SimpleDetailFamilleSearch();
        currentSearch.setForAnnonceCaisseMaladie(false);
        currentSearch.setForRefus(false);
        currentSearch.setForCodeActif(true);
        currentSearch.setForDateAnnonceCaisseMaladie("0");
        currentSearch.setInNoModeles(docGenereAnnonce);
        currentSearch.setInNoCaisseMaladies(idTiersCM);
        if (!JadeStringUtil.isEmpty(anneeHistorique)) {
            currentSearch.setForAnneeHistorique(anneeHistorique);
        }
        currentSearch.setDefinedSearchSize(1000);
        try {
            currentSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(currentSearch);
        } catch (Exception ex) {
            ex.printStackTrace();
            needTreatment = false;
        }

        try {
            while (needTreatment) {
                for (JadeAbstractModel model : currentSearch.getSearchResults()) {

                    SimpleDetailFamille currentSubside = (SimpleDetailFamille) model;

                    // ---------------------------------------------------------------------------
                    // 3a) Malgré la première requête, contrôler que nous ne prenions pas des montants
                    // inférieurs ou égal à 0
                    // ---------------------------------------------------------------------------
                    try {
                        float fSubside = Float.parseFloat(currentSubside.getMontantContributionAvecSupplExtra());
                        if (fSubside <= 0.0) {
                            continue;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // ---------------------------------------------------------------------------
                    // 3b) Malgré la première requête, contrôler que nous ne prenions pas des
                    // subsides avec dates de début et de fin antérieures
                    // ---------------------------------------------------------------------------
                    try {
                        // contrôle sur les dates : date de début doit être avant date de fin
                        if ((currentSubside.getFinDroit() != null)
                                && ((currentSubside.getDebutDroit() != null) && !JadeStringUtil
                                        .isBlankOrZero(currentSubside.getFinDroit()))) {
                            if (!currentSubside.getDebutDroit().equals(currentSubside.getFinDroit())) {
                                if (!JadeDateUtil.isDateMonthYearBefore(currentSubside.getDebutDroit(),
                                        currentSubside.getFinDroit())) {
                                    continue;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    // ---------------------------------------------------------------------------
                    // 4) Organisation du résultat en hashmap<idTiersCaisse, List<idDetailFamille>
                    // ---------------------------------------------------------------------------
                    List<String> subsidesParCaisse = returnedMap.get(currentSubside.getNoCaisseMaladie());
                    if (subsidesParCaisse == null) {
                        subsidesParCaisse = new ArrayList<String>();
                    } else {
                        returnedMap.remove(currentSubside.getNoCaisseMaladie());
                    }
                    // Recherche de subside d'une même personne, même année à annoncer
                    // inclus également le subside courant
                    List<String> subsidesAdditionnels = getIdSubsidesToAnnounce(currentSubside.getId(),
                            docGenereAnnonce);
                    Iterator<String> subsidesAdditionnelsIterator = subsidesAdditionnels.iterator();
                    while (subsidesAdditionnelsIterator.hasNext()) {
                        // Recherche des autres annonces à effectuer pour le même membre de famille
                        String idSubsideAdditionnel = subsidesAdditionnelsIterator.next();
                        try {
                            SimpleDetailFamille currentSubsideAdditionnel = AmalImplServiceLocator
                                    .getSimpleDetailFamilleService().read(idSubsideAdditionnel);
                            // Même caisse maladie, ok traitement normal
                            if (currentSubsideAdditionnel.getNoCaisseMaladie().equals(
                                    currentSubside.getNoCaisseMaladie())) {
                                if (!subsidesParCaisse.contains(idSubsideAdditionnel)) {
                                    subsidesParCaisse.add(idSubsideAdditionnel);
                                }
                            } else {
                                // Autre caisse maladie
                                List<String> subsidesParCaisseAdditionnel = returnedMap.get(currentSubsideAdditionnel
                                        .getNoCaisseMaladie());
                                if (subsidesParCaisseAdditionnel == null) {
                                    subsidesParCaisseAdditionnel = new ArrayList<String>();
                                } else {
                                    returnedMap.remove(currentSubsideAdditionnel.getNoCaisseMaladie());
                                }
                                if (!subsidesParCaisseAdditionnel.contains(idSubsideAdditionnel)) {
                                    subsidesParCaisseAdditionnel.add(idSubsideAdditionnel);
                                }
                                returnedMap.put(currentSubsideAdditionnel.getNoCaisseMaladie(),
                                        subsidesParCaisseAdditionnel);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    returnedMap.put(currentSubside.getNoCaisseMaladie(), subsidesParCaisse);
                }
                // Suite du traitement
                // ---------------------------------------------------------------------------
                iOffset += currentSearch.getSize();
                currentSearch.setOffset(iOffset);
                if (currentSearch.isHasMoreElements()) {
                    needTreatment = true;
                    currentSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(currentSearch);
                } else {
                    needTreatment = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedMap;
    }

    /**
     * Recherche des subsides du même membre de famille, avec la même année
     * qui doivent également être annoncés
     * 
     * @param idDetailFamille
     * @return
     */
    private List<String> getIdSubsidesToAnnounce(String idDetailFamille, List<String> docGenereAnnonce) {
        // Création et ajout du subside à annoncer
        List<String> idSubsides = new ArrayList<String>();
        idSubsides.add(idDetailFamille);
        // Recherche des autres annonces à effectuer pour le même membre de famille
        try {
            SimpleDetailFamille currentSubside = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    idDetailFamille);
            SimpleDetailFamilleSearch currentSearch = new SimpleDetailFamilleSearch();
            currentSearch.setDefinedSearchSize(0);
            currentSearch.setForCodeActif(true);
            currentSearch.setForAnneeHistorique(currentSubside.getAnneeHistorique());
            currentSearch.setForIdFamille(currentSubside.getIdFamille());
            currentSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(currentSearch);
            for (int iSearch = 0; iSearch < currentSearch.getSize(); iSearch++) {
                SimpleDetailFamille searchedSubside = (SimpleDetailFamille) currentSearch.getSearchResults()[iSearch];
                if (!idSubsides.contains(searchedSubside.getIdDetailFamille())) {
                    if (searchedSubside.getRefus() == false) {
                        if (docGenereAnnonce.contains(searchedSubside.getNoModeles())) {
                            idSubsides.add(searchedSubside.getIdDetailFamille());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return idSubsides;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.annonce.AnnonceService#getIdTiersCMAnnonceInProgress()
     */
    @Override
    public List<String> getIdTiersCMAnnonceInProgress() {

        List<String> returnCM = new ArrayList<String>();

        SimpleControleurJobSearch searchJob = new SimpleControleurJobSearch();
        searchJob.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
        searchJob.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
        searchJob.setDefinedSearchSize(0);
        try {
            searchJob = AmalImplServiceLocator.getSimpleControleurJobService().search(searchJob);
            for (int iJob = 0; iJob < searchJob.getSize(); iJob++) {
                SimpleControleurJob currentJob = (SimpleControleurJob) searchJob.getSearchResults()[iJob];
                returnCM.add(currentJob.getSubTypeJob());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnCM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.annonce.AnnonceService#getIdTiersCMSimulationInProgress()
     */
    @Override
    public List<String> getIdTiersCMSimulationInProgress() {
        // Pour l'instant, empêche la création d'une simulation tant qu'une annonce est en cours
        // et vis-versa
        return getIdTiersCMAnnonceInProgress();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.annonce.AnnonceService#writeAnnoncesInTables()
     */
    @Override
    public List<String> writeAnnoncesInTables(HashMap<String, List<String>> annoncesToCreate) {

        List<SimpleControleurJob> jobToDelete = new ArrayList<SimpleControleurJob>();
        List<String> errorsToReturn = new ArrayList<String>();
        // ---------------------------------------------------------------------------
        // 1) Récupération des idjobs par caisses maladie MACTLJOB (idjob)
        // ---------------------------------------------------------------------------
        SimpleControleurJobSearch searchJob = new SimpleControleurJobSearch();
        searchJob.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
        searchJob.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
        searchJob.setDefinedSearchSize(0);
        try {
            searchJob = AmalImplServiceLocator.getSimpleControleurJobService().search(searchJob);
            for (int iJob = 0; iJob < searchJob.getSize(); iJob++) {
                SimpleControleurJob currentJob = (SimpleControleurJob) searchJob.getSearchResults()[iJob];

                List<String> subsidesToAnnounce = annoncesToCreate.get(currentJob.getSubTypeJob());
                if (subsidesToAnnounce == null) {
                    // Pas de subsides à annoncer pour cette caisse maladie - on supprime le job
                    // AmalImplServiceLocator.getSimpleControleurJobService().delete(currentJob);
                    jobToDelete.add(currentJob);
                } else {
                    if (subsidesToAnnounce.size() <= 0) {
                        // Pas de subsides à annoncer pour cette caisse maladie - on supprime le job
                        // AmalImplServiceLocator.getSimpleControleurJobService().delete(currentJob);
                        jobToDelete.add(currentJob);
                    }
                    Iterator<String> iteratorSubsides = subsidesToAnnounce.iterator();
                    while (iteratorSubsides.hasNext()) {
                        String currentSubsideId = iteratorSubsides.next();
                        SimpleDetailFamille currentSubside = AmalImplServiceLocator.getSimpleDetailFamilleService()
                                .read(currentSubsideId);
                        // ---------------------------------------------------------------------------
                        // 2) Création des annonces MAANNCAI (idanncai)
                        // ---------------------------------------------------------------------------
                        SimpleAnnonce currentAnnonce = new SimpleAnnonce();
                        currentAnnonce.setIdDetailFamille(currentSubsideId);
                        currentAnnonce.setAnneeHistorique(currentSubside.getAnneeHistorique());
                        currentAnnonce.setAnnonceCaisseMaladie(true);
                        currentAnnonce.setCodeActif(currentSubside.getCodeActif());
                        currentAnnonce.setCodeForcer(currentSubside.getCodeForcer());
                        currentAnnonce.setCodeTraitement(currentSubside.getCodeTraitement());
                        currentAnnonce.setCodeTraitementDossier(currentSubside.getCodeTraitementDossier());
                        currentAnnonce.setDateAvisRIP(currentJob.getDateJob());
                        currentAnnonce.setDateEnvoiAnnonce(currentSubside.getDateEnvoi());
                        currentAnnonce.setDateModification(currentSubside.getDateModification());
                        currentAnnonce.setDateReceptionDemande(currentSubside.getDateRecepDemande());
                        currentAnnonce.setDebutDroit(currentSubside.getDebutDroit());
                        currentAnnonce.setFinDroit(currentSubside.getFinDroit());
                        currentAnnonce.setMontantContribution(currentSubside.getMontantContributionAvecSupplExtra());
                        currentAnnonce.setMontantContributionAnnuelle(currentSubside.getMontantContribAnnuelle());
                        currentAnnonce.setMontantExact(currentSubside.getMontantExact());
                        currentAnnonce.setMontantPrime(currentSubside.getMontantPrimeAssurance());
                        currentAnnonce.setNoAssure(currentSubside.getNoAssure());
                        currentAnnonce.setNoCaisseMaladie(currentSubside.getNoCaisseMaladie());
                        currentAnnonce.setNoModele(currentSubside.getNoModeles());
                        currentAnnonce.setNumeroLot(currentSubside.getNoLot());
                        currentAnnonce.setRefuse(currentSubside.getRefus());
                        currentAnnonce.setSupplementExtraordinaire(currentSubside.getSupplExtra());
                        currentAnnonce.setTauxEnfantCharge(currentSubside.getTauxEnfantCharge());
                        currentAnnonce.setTypeAvisRIP(currentSubside.getTypeAvisRIP());
                        currentAnnonce.setTypeDemande(currentSubside.getTypeDemande());
                        currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().create(currentAnnonce);
                        // ---------------------------------------------------------------------------
                        // 3) Inscription au dossier (O et date)
                        // ---------------------------------------------------------------------------
                        currentSubside.setDateAnnonceCaisseMaladie(currentAnnonce.getDateEnvoiAnnonce());
                        currentSubside.setDateAvisRIP(currentAnnonce.getDateAvisRIP());
                        currentSubside.setAnnonceCaisseMaladie(currentAnnonce.getAnnonceCaisseMaladie());
                        currentSubside = AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                        // ---------------------------------------------------------------------------
                        // 4) Création des status envois MACTLSTS - SENT directement
                        // ---------------------------------------------------------------------------
                        SimpleControleurEnvoiStatus currentStatus = new SimpleControleurEnvoiStatus();
                        currentStatus.setIdAnnonce(currentAnnonce.getId());
                        currentStatus.setIdEnvoi("-1");
                        currentStatus.setIdJob(currentJob.getId());
                        currentStatus.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
                        currentStatus.setTypeEnvoi(IAMCodeSysteme.AMDocumentType.ANNONCE.getValue());
                        currentStatus = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().create(
                                currentStatus);
                        // ---------------------------------------------------------------------------
                        // 5) Inscription dans journalisation
                        // ---------------------------------------------------------------------------
                        // try{
                        // // Création de la journalisation LIBRA
                        // // -----------------------------------
                        // FamilleContribuable currentFamille =
                        // AmalServiceLocator.getFamilleContribuableService().read(currentSubside.getId());
                        // if (!JadeStringUtil.isBlankOrZero(currentFamille.getSimpleFamille().getIdTier())) {
                        // try {
                        // LibraServiceLocator.getJournalisationService()
                        // .createJournalisationAvecRemarqueWithTestDossier(currentFamille.getSimpleFamille().getIdFamille(),
                        // "Annonce caisse-maladie ", "", currentFamille.getSimpleFamille().getIdTier(),
                        // ILIConstantesExternes.CS_DOMAINE_AMAL, true);
                        // } catch (LibraException e) {
                        // e.printStackTrace();
                        // }
                        // }
                        // }catch(Exception ex2){
                        // ex2.printStackTrace();
                        // }
                        if (JadeThread.logHasMessages()) {
                            JadeBusinessMessage[] allMessages = JadeThread.logMessages();
                            for (int iMessage = 0; iMessage < allMessages.length; iMessage++) {
                                String currentMessage = allMessages[iMessage].getContents(JadeThread.currentLanguage());
                                errorsToReturn.add("Subside Id : " + currentSubsideId + " : " + currentMessage);
                            }
                            JadeThread.logClear();
                        }
                    }
                }
                try {
                    JadeThread.commitSession();
                } catch (Exception ex1) {
                    JadeLogger.error(null, "Error commiting session in writeAnnoncesInTables : " + ex1.toString());
                    ex1.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Suppression des jobs vides
        Iterator<SimpleControleurJob> jobToDeleteIterator = jobToDelete.iterator();
        while (jobToDeleteIterator.hasNext()) {
            SimpleControleurJob currentJob = jobToDeleteIterator.next();
            try {
                AmalImplServiceLocator.getSimpleControleurJobService().delete(currentJob);
            } catch (Exception ex) {
                JadeLogger.error(null, "Error deleting job in writeAnnoncesInTables :" + ex.toString());
            }
        }
        try {
            JadeThread.commitSession();
        } catch (Exception ex) {
            JadeLogger.error(null, "Error commit session in writeAnnoncesInTables :" + ex.toString());
        }

        // ---------------------------------------------------------------------------
        // 5) Restera les job à passer au status SENT après la génération des fichiers >> FAIT DANS LE PROCESS
        // ---------------------------------------------------------------------------
        return errorsToReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.annonce.AnnonceService#writeAnnoncesInTables(java.lang.String,
     * java.util.List, java.lang.String)
     */
    @Override
    public void writeAnnoncesInTables(String idTiersGroupe, List<String> idTiersCM, String anneeHistorique) {
        HashMap<String, List<String>> annoncesToCreate = getAnnoncesToCreate(idTiersGroupe, idTiersCM, anneeHistorique);
        this.writeAnnoncesInTables(annoncesToCreate);
    }

}
